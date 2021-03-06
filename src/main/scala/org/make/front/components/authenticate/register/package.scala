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

package org.make.front.components.authenticate

import org.make.client.ValidationError
import org.make.core.validation.PasswordConstraint
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{User => UserModel}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.concurrent.Future
import scala.scalajs.js

package object register {
  case class RegisterState(fields: Map[String, String],
                           errors: Map[String, String],
                           additionalFields: Seq[SignUpField],
                           disableSubmit: Boolean) {
    def hasError(field: String): Boolean = {
      val maybeErrorMessage = errors.get(field)
      maybeErrorMessage.isEmpty || maybeErrorMessage.contains("")
    }
  }

  object RegisterState {
    val empty =
      RegisterState(fields = Map.empty, errors = Map.empty, additionalFields = Seq.empty, disableSubmit = false)
  }

  case class RegisterProps(note: String,
                           register: Map[String, String] => Future[UserModel],
                           trackingContext: TrackingContext,
                           trackingParameters: Map[String, String],
                           trackingInternalOnlyParameters: Map[String, String],
                           additionalFields: Seq[SignUpField],
                           language: String)

  def getErrorsMessagesFromApiErrors(errors: js.Array[ValidationError]): js.Array[(String, String)] = {
    errors.map {
      case ValidationError("email", Some(message)) if message.contains("already exist") =>
        "email" -> I18n.t("authenticate.register.error-message.already-exists")
      case ValidationError("email", Some(message)) if message.contains("required") =>
        "email" -> I18n.t("authenticate.inputs.email.empty-field-error-message")
      case ValidationError("email", _) =>
        "email" -> I18n.t("authenticate.inputs.email.format-error-message")
      case ValidationError("password", Some(message)) if message.contains("required") =>
        "password" -> I18n.t("authenticate.inputs.password.empty-field-error-message")
      case ValidationError("password", Some(message)) if message.contains("Wrong password") =>
        "password" -> I18n.t("user-profile.delete-account.password.error")
      case ValidationError("password", _) =>
        "password" -> I18n.t(
          "authenticate.inputs.password.format-error-message",
          Replacements("min" -> PasswordConstraint.min.toString)
        )
      case ValidationError("firstName", Some(message)) if message.contains("required") =>
        "firstName" -> I18n.t("authenticate.inputs.first-name.empty-field-error-message")
      case ValidationError("age", _) =>
        "age" -> I18n.t("authenticate.inputs.age.format-error-message")
      case ValidationError("postalCode", _) =>
        "postalCode" -> I18n.t("authenticate.inputs.postal-code.format-error-message")
      case ValidationError("profession", _) =>
        "profession" -> I18n.t("authenticate.inputs.job.format-error-message")
      case ValidationError("gender", _) =>
        "gender" -> I18n.t("authenticate.inputs.gender.format-error-message")
      case ValidationError("socioProfessionalCategory", _) =>
        "socioProfessionalCategory" -> I18n.t("authenticate.inputs.csp.format-error-message")
      case ValidationError(_, _) =>
        "global" -> I18n.t("authenticate.error-message")
    }
  }

  sealed trait SignUpField
  object SignUpField {
    case object FirstName extends SignUpField
    case object Age extends SignUpField
    case object Job extends SignUpField
    case object PostalCode extends SignUpField
    case object Gender extends SignUpField
    case object Csp extends SignUpField
    case class PartnerOptInLabel(language: String, label: String)
    case class PartnerOptIn(labels: Seq[PartnerOptInLabel]) extends SignUpField
    case object HiddenOptOut extends SignUpField
  }
}
