/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.services.user

import org.make.client.MakeApiClient
import org.make.client.models.UserResponse
import org.make.core.URI._
import org.make.front.facades.I18n
import org.make.front.models.{OperationId, User}
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSON

object UserService extends ApiService {

  override val resourceName: String = "user"

  def getUserById(id: String): Future[Option[User]] =
    MakeApiClient.get[UserResponse](resourceName / id).map(user => Option(User(user))).recover {
      case _: Exception => None
    }

  def registerUser(email: String,
                   password: String,
                   firstName: String,
                   profession: Option[String],
                   age: Option[Int],
                   postalCode: Option[String],
                   operationId: Option[OperationId],
                   language: String,
                   country: String): Future[User] = {

    val headers = MakeApiClient.getDefaultHeaders ++ operationId.map(op => MakeApiClient.operationHeader -> op.value)
    MakeApiClient
      .post[UserResponse](
        resourceName,
        data = JSON.stringify(
          JsRegisterUserRequest(
            RegisterUserRequest(
              email = email,
              firstName = firstName,
              password = password,
              profession = profession,
              postalCode = postalCode,
              dateOfBirth = age.map(age => s"${new js.Date().getUTCFullYear() - age}-01-01"),
              country = country,
              language = language
            )
          )
        ),
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

  def loginGoogle(token: String, country: String, language: String, operationId: Option[OperationId]): Future[User] = {
    MakeApiClient
      .authenticateSocial(
        provider = "google",
        token = token,
        operationId = operationId,
        country = country,
        language = language
      )
      .flatMap {
        case true  => getCurrentUser()
        case false => throw NoTokenException()
      }
  }

  def loginFacebook(token: String,
                    country: String,
                    language: String,
                    operationId: Option[OperationId]): Future[User] = {

    MakeApiClient
      .authenticateSocial(
        provider = "facebook",
        token = token,
        operationId = operationId,
        country = country,
        language = language
      )
      .flatMap {
        case true  => getCurrentUser()
        case false => throw NoTokenException()
      }
  }

  def resetPasswordRequest(email: String): Future[Unit] = {
    MakeApiClient
      .post[UserResponse](
        resourceName / "reset-password" / "request-reset",
        data = JSON.stringify(js.Dictionary("email" -> email))
      )
      .map { _ =>
        }
  }

  def subscribeToNewsletter(email: String): Future[Unit] = {
    Future.successful {}
    MakeApiClient
      .post[js.Object](resourceName / "newsletter", data = JSON.stringify(js.Dictionary("email" -> email)))
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
        data = JSON.stringify(js.Dictionary("resetToken" -> resetToken, "password" -> password))
      )
      .map { _ =>
        true
      }
      .recoverWith {
        case _ => Future.successful(false)
      }
  }

  def validateAccount(userId: String, verificationToken: String, operationId: Option[OperationId]): Future[Unit] = {
    val headers = MakeApiClient.getDefaultHeaders ++ operationId.map(op => MakeApiClient.operationHeader -> op.value)

    MakeApiClient.post[UserResponse](resourceName / userId / "validate" / verificationToken, headers = headers).map {
      _ =>
        {}
    }
  }

  def logout(): Future[Unit] =
    MakeApiClient.logout()

  final case class NoTokenException(message: String = I18n.t("error-message.no-token")) extends Exception(message)
  final case class UserNotfoundException(message: String = I18n.t("error-message.user-not-found"))
      extends Exception(message)

}
