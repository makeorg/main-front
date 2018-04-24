package org.make.front.components.authenticate

import org.make.client.ValidationError
import org.make.core.validation.PasswordConstraint
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{User => UserModel}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.concurrent.Future
import scala.scalajs.js

package object register {
  case class RegisterState(fields: Map[String, String], errors: Map[String, String]) {
    def hasError(field: String): Boolean = {
      val maybeErrorMessage = errors.get(field)
      maybeErrorMessage.isEmpty || maybeErrorMessage.contains("")
    }
  }

  object RegisterState {
    val empty = RegisterState(Map(), Map())
  }

  case class RegisterProps(note: String,
                           register: (RegisterState) => Future[UserModel],
                           trackingContext: TrackingContext,
                           trackingParameters: Map[String, String])

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
      case ValidationError(_, _) =>
        "global" -> I18n.t("authenticate.error-message")
    }
  }
}
