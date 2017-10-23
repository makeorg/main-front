package org.make.services.user

import java.time.LocalDate

import org.make.client.MakeApiClient
import org.make.client.models.UserResponse
import org.make.core.URI._
import org.make.front.facades.I18n
import org.make.front.models.{OperationId, User}
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object UserService extends ApiService {

  override val resourceName: String = "user"

  def getUserById(id: String): Future[Option[User]] =
    MakeApiClient.get[UserResponse](resourceName / id).map(user => Option(User(user))).recover { case _: Exception => None }

  def registerUser(email: String,
                   password: String,
                   firstName: String,
                   profession: Option[String],
                   age: Option[Int],
                   postalCode: Option[String],
                   operation: Option[OperationId]): Future[User] = {

    val headers = MakeApiClient.defaultHeaders ++ operation.map(op => MakeApiClient.operationHeader -> op.value)
    MakeApiClient
      .post[UserResponse](
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
        ).toString,
        headers = headers
      )
      .map(User.apply)
  }

  def login(username: String, password: String): Future[User] = {
    MakeApiClient.authenticate(username, password).flatMap {
      case true  => getCurrentUser()
      case false => throw NoTokenException()
    }
  }

  def getCurrentUser(): Future[User] = {
    MakeApiClient.get[UserResponse](resourceName / "me").map(User.apply)
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
      .post[UserResponse](
        resourceName / "reset-password" / "request-reset",
        data = Map("email" -> email).toString
      )
      .map { _ => }
  }

  def subscribeToNewsletter(email: String): Future[Unit] = {
    Future.successful {}
    MakeApiClient
      .post[UserResponse](resourceName / "newsletter", data = Map("email" -> email).toString)
      .map { _ =>
        }
  }

  def resetPasswordCheck(userId: String, resetToken: String): Future[Boolean] = {
    MakeApiClient
      .post[UserResponse](resourceName / "reset-password" / "check-validity" / userId / resetToken)
      .map { _ =>
        true
      }
      .recoverWith {
        case _ => Future.successful(false)
      }
  }

  def resetPasswordChange(userId: String, resetToken: String, password: String): Future[Boolean] = {
    MakeApiClient
      .post[UserResponse](
        resourceName / "reset-password" / "change-password" / userId,
        data = Map("resetToken" -> resetToken, "password" -> password).toString
      )
      .map { _ =>
        true
      }
      .recoverWith {
        case _ => Future.successful(false)
      }
  }

  def validateAccount(userId: String, verificationToken: String, operation: Option[OperationId]): Future[Unit] = {
    val headers = MakeApiClient.defaultHeaders ++ operation.map(op => MakeApiClient.operationHeader -> op.value)

    MakeApiClient.post[UserResponse](resourceName / userId / "validate" / verificationToken, headers = headers).map { _ =>
      {}
    }
  }

  def logout(): Future[Unit] =
    MakeApiClient.logout()

  final case class NoTokenException(message: String = I18n.t("errors.noToken")) extends Exception(message)
  final case class UserNotfoundException(message: String = I18n.t("errors.userNotFound")) extends Exception(message)

}
