package org.make.services.user

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

trait UserServiceComponent {
  def userService: UserService = new UserService()

  class UserService
      extends ApiService
      with CirceClassFormatters
      with CirceFormatters
      with DefaultMakeApiClientComponent {

    override val resourceName: String = "user"

    def getUserById(id: String): Future[Option[User]] =
      client.get[User](resourceName / id).map(Some(_)).recover { case _: Exception => None }

    def registerByUsernameAndPasswordAndFirstName(username: String, password: String, firstName: String): Future[User] =
      client.post[User](
        resourceName,
        data = RegisterUserRequest(username, password, firstName = Some(firstName)).asJson.pretty(ApiService.printer)
      )

    def login(username: String, password: String): Future[User] = {
      client.authenticate(username, password).flatMap {
        case true  => client.get[User](resourceName / "me")
        case false => throw NoTokenException()
      }
    }

    def loginGoogle(token: String): Future[User] = {
      client.authenticateSocial("google", token).flatMap {
        case true  => client.get[User](resourceName / "me")
        case false => throw NoTokenException()
      }
    }
    def loginFacebook(token: String): Future[User] = {
      client.authenticateSocial("facebook", token).flatMap {
        case true  => client.get[User](resourceName / "me")
        case false => throw NoTokenException()
      }
    }

    def logout(): Future[Unit] =
      client.logout()

  }
}
