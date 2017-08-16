package org.make.services.user

import io.circe.generic.auto._
import io.circe.syntax._
import org.make.client.DefaultMakeApiClientComponent
import org.make.core.URI._
import org.make.core.{CirceClassFormatters, CirceFormatters}
import org.make.front.models.User
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

    def register(user: User): Future[User] =
      client.post[User](resourceName, data = RegisterUserRequest(user).asJson.pretty(ApiService.printer))

    //TODO: Receive form request instead of user.
    def login(user: User): Future[Boolean] =
      client.authenticate(user.email, user.hashedPassword.get).map(_ => true).recover { case _: Exception => false }
  }
}
