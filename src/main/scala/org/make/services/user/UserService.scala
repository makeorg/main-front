package org.make.services.user

import java.time.LocalDate

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.client.MakeApiClient
import org.make.core.URI._
import org.make.core.{CirceClassFormatters, CirceFormatters}
import org.make.front.facades.I18n
import org.make.front.models.User
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object UserService extends ApiService with CirceClassFormatters with CirceFormatters {

  override val resourceName: String = "user"

  def getUserById(id: String): Future[Option[User]] =
    MakeApiClient.get[User](resourceName / id).recover { case _: Exception => None }

  def registerUser(email: String,
                   password: String,
                   firstName: String,
                   profession: Option[String],
                   age: Option[Int],
                   postalCode: Option[String]): Future[User] = {

    MakeApiClient
      .post[User](
        resourceName,
        data = RegisterUserRequest(
          email = email,
          firstName = firstName,
          password = password,
          profession = profession,
          postalCode = postalCode,
          dateOfBirth = age.map(age => {
            LocalDate.of(LocalDate.now.getYear - age, 1, 1)
          })
        ).asJson.pretty(ApiService.printer)
      )
      .map(_.get)
  }

  def login(username: String, password: String): Future[User] = {
    MakeApiClient.authenticate(username, password).flatMap {
      case true  => getCurrentUser()
      case false => throw NoTokenException()
    }
  }

  def getCurrentUser(): Future[User] = {
    MakeApiClient.get[User](resourceName / "me").map(_.get)
  }

  def loginGoogle(token: String): Future[User] = {
    MakeApiClient.authenticateSocial("google", token).flatMap {
      case true  => getCurrentUser()
      case false => throw NoTokenException()
    }
  }
  def loginFacebook(token: String): Future[User] = {
    MakeApiClient.authenticateSocial("facebook", token).flatMap {
      case true  => getCurrentUser()
      case false => throw NoTokenException()
    }
  }

  def resetPasswordRequest(email: String): Future[Unit] = {
    MakeApiClient
      .post[Unit](
        resourceName / "reset-password" / "request-reset",
        data = Map("email" -> email).asJson.pretty(ApiService.printer)
      )
      .map { _ =>
        }
  }

  def resetPasswordCheck(userId: String, resetToken: String): Future[Boolean] = {
    MakeApiClient
      .post[Unit](resourceName / "reset-password" / "check-validity" / userId / resetToken)
      .map { _ =>
        true
      }
      .recoverWith {
        case _ => Future.successful(false)
      }
  }

  def resetPasswordChange(userId: String, resetToken: String, password: String): Future[Boolean] = {
    MakeApiClient
      .post[Unit](
        resourceName / "reset-password" / "change-password" / userId,
        data = Map("resetToken" -> resetToken, "password" -> password).asJson.pretty(ApiService.printer)
      )
      .map { _ =>
        true
      }
      .recoverWith {
        case _ => Future.successful(false)
      }
  }

  def validateAccount(userId: String, verificationToken: String): Future[Unit] = {
    MakeApiClient.post[Unit](resourceName / userId / "validate" / verificationToken).map { _ =>
      }
  }

  def logout(): Future[Unit] =
    MakeApiClient.logout()

  final case class NoTokenException(message: String = I18n.t("errors.noToken")) extends Exception(message)
  final case class UserNotfoundException(message: String = I18n.t("errors.userNotFound")) extends Exception(message)

}
