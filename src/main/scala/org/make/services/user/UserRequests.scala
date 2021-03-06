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

import org.make.front.models.{OperationId, QuestionId}

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
                                     postalCode: Option[String] = None,
                                     gender: Option[String],
                                     socioProfessionalCategory: Option[String],
                                     optInPartner: Option[Boolean] = None,
                                     operationId: Option[OperationId],
                                     questionId: Option[QuestionId],
                                     optIn: Option[Boolean] = None)
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
  val gender: js.UndefOr[String]
  val socioProfessionalCategory: js.UndefOr[String]
  val optInPartner: js.UndefOr[Boolean]
  val operationId: js.UndefOr[String]
  val questionId: js.UndefOr[String]
  val optIn: js.UndefOr[Boolean]
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
        postalCode = registerUserRequest.postalCode.orUndefined,
        gender = registerUserRequest.gender.orUndefined,
        socioProfessionalCategory = registerUserRequest.socioProfessionalCategory.orUndefined,
        optInPartner = registerUserRequest.optInPartner.orUndefined,
        operationId = registerUserRequest.operationId.map(_.value).orUndefined,
        questionId = registerUserRequest.questionId.map(_.value).orUndefined,
        optIn = registerUserRequest.optIn.orUndefined
      )
      .asInstanceOf[JsRegisterUserRequest]
  }
}

final case class UpdateUserRequest(firstName: Option[String] = None,
                                   lastName: Option[String] = None,
                                   organisationName: Option[String] = None,
                                   dateOfBirth: Option[String] = None,
                                   profession: Option[String] = None,
                                   postalCode: Option[String] = None,
                                   phoneNumber: Option[String] = None,
                                   description: Option[String] = None,
                                   optInNewsletter: Option[Boolean],
                                   language: Option[String],
                                   country: Option[String],
                                   gender: Option[String] = None,
                                   socioProfessionalCategory: Option[String] = None)
@js.native
trait JsUpdateUserRequest extends js.Object {
  val firstName: js.UndefOr[String]
  val lastName: js.UndefOr[String]
  val organisationName: js.UndefOr[String]
  val dateOfBirth: js.UndefOr[String]
  val profession: js.UndefOr[String]
  val postalCode: js.UndefOr[String]
  val phoneNumber: js.UndefOr[String]
  val optInNewsletter: js.UndefOr[Boolean]
  val language: js.UndefOr[String]
  val country: js.UndefOr[String]
  val description: js.UndefOr[String]
  val gender: js.UndefOr[String]
  val socioProfessionalCategory: js.UndefOr[String]
}

object JsUpdateUserRequest {
  def apply(updateUserRequest: UpdateUserRequest): JsUpdateUserRequest = {
    js.Dynamic
      .literal(
        firstName = updateUserRequest.firstName.orUndefined,
        lastName = updateUserRequest.lastName.orUndefined,
        organisationName = updateUserRequest.organisationName.orUndefined,
        dateOfBirth = updateUserRequest.dateOfBirth.orUndefined,
        profession = updateUserRequest.profession.orUndefined,
        postalCode = updateUserRequest.postalCode.orUndefined,
        phoneNumber = updateUserRequest.phoneNumber.orUndefined,
        optInNewsletter = updateUserRequest.optInNewsletter.orUndefined,
        country = updateUserRequest.country.orUndefined,
        language = updateUserRequest.language.orUndefined,
        description = updateUserRequest.description.orUndefined,
        gender = updateUserRequest.gender.orUndefined,
        socioProfessionalCategory = updateUserRequest.socioProfessionalCategory.orUndefined
      )
      .asInstanceOf[JsUpdateUserRequest]
  }
}
