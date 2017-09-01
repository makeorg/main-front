package org.make.services.user

import java.time.LocalDate

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.client.DefaultMakeApiClientComponent
import org.make.core.URI._
import org.make.core.{CirceClassFormatters, CirceFormatters}
import org.make.front.facades.I18n
import org.make.front.models.User
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class NoTokenException(message: String = I18n.t("errors.noToken")) extends Exception(message)
case class UserNotfoundException(message: String = I18n.t("errors.userNotFound")) extends Exception(message)

trait UserServiceComponent {
  def apiBaseUrl: String
  def userService: UserService = new UserService(apiBaseUrl)

  class UserService(override val apiBaseUrl: String)
      extends ApiService
      with CirceClassFormatters
      with CirceFormatters
      with DefaultMakeApiClientComponent {

    override val resourceName: String = "user"

    def getUserById(id: String): Future[Option[User]] =
      client.get[User](resourceName / id).recover { case _: Exception => None }

    def registerUser(username: String, password: String, firstName: String): Future[User] =
      client.post[User](
        resourceName,
        data = RegisterUserRequest(username, password, firstName = firstName).asJson.pretty(ApiService.printer)
      ).map(_.get)

    def registerUser(username: String,
                     password: String,
                     firstName: String,
                     dateOfBirth: LocalDate,
                     profession: String,
                     postalCode: String): Future[User] =
      client.post[User](
        resourceName,
        data = RegisterUserRequest(
          username,
          password,
          firstName = firstName,
          dateOfBirth = Some(dateOfBirth),
          profession = Some(profession),
          postalCode = Some(postalCode)
        ).asJson.pretty(ApiService.printer)
      ).map(_.get)

    def login(username: String, password: String): Future[User] = {
      client.authenticate(username, password).flatMap {
        case true  => client.get[User](resourceName / "me").map(_.get)
        case false => throw NoTokenException()
      }
    }

    def loginGoogle(token: String): Future[User] = {
      client.authenticateSocial("google", token).flatMap {
        case true  => client.get[User](resourceName / "me").map(_.get)
        case false => throw NoTokenException()
      }
    }
    def loginFacebook(token: String): Future[User] = {
      client.authenticateSocial("facebook", token).flatMap {
        case true  => client.get[User](resourceName / "me").map(_.get)
        case false => throw NoTokenException()
      }
    }

    def recoverPassword(email: String): Future[Unit] = {
      client.post[Unit](
        "user" / "reset-password",
        data = Map("email" -> email).asJson.pretty(ApiService.printer)
      ).map {
        _ =>
      }
    }

    def logout(): Future[Unit] =
      client.logout()

  }
}
