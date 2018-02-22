package org.make.services.user

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

final case class RegisterUserRequest(email: String,
                                     password: String,
                                     firstName: String,
                                     language: String,
                                     country: String,
                                     lastName: Option[String] = None,
                                     dateOfBirth: Option[String] = None,
                                     profession: Option[String] = None,
                                     postalCode: Option[String] = None)
@js.native
trait JsRegisterUserRequest extends js.Object {
  val email: String
  val password: String
  val firstName: String
  val language: String
  val country: String
  val lastName: js.UndefOr[String]
  val dateOfBirth: js.UndefOr[String]
  val profession: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
}

object JsRegisterUserRequest {
  def apply(registerUserRequest: RegisterUserRequest): JsRegisterUserRequest = {
    js.Dynamic
      .literal(
        email = registerUserRequest.email,
        password = registerUserRequest.password,
        firstName = registerUserRequest.firstName,
        country = registerUserRequest.country,
        language = registerUserRequest.language,
        lastName = registerUserRequest.lastName.orUndefined,
        dateOfBirth = registerUserRequest.dateOfBirth.map(_.toString).orUndefined,
        profession = registerUserRequest.profession.orUndefined,
        postalCode = registerUserRequest.postalCode.orUndefined
      )
      .asInstanceOf[JsRegisterUserRequest]
  }
}
