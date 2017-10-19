package org.make.front.components.authenticate

import org.make.client.ValidationError
import org.make.core.validation.PasswordConstraint
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{User => UserModel}

import scala.concurrent.Future

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

  case class RegisterProps(note: String, register: (RegisterState) => Future[UserModel])

  def getErrorsMessagesFromApiErrors(errors: Seq[ValidationError]): Seq[(String, String)] = {
    errors.map {
      case ValidationError("email", Some(message)) if message.contains("already exist") =>
        "email" -> I18n.t("authenticate.register.errors.already-exists")
      case ValidationError("email", Some(message)) if message.contains("required") =>
        "email" -> I18n.t("authenticate.inputs.email.empty-field-error")
      case ValidationError("email", _) =>
        "email" -> I18n.t("authenticate.inputs.email.format-error")
      case ValidationError("password", Some(message)) if message.contains("required") =>
        "password" -> I18n.t("authenticate.inputs.password.empty-field-error")
      case ValidationError("password", _) =>
        "password" -> I18n.t(
          "authenticate.inputs.password.format-error",
          Replacements("min" -> PasswordConstraint.min.toString)
        )
      case ValidationError("firstName", Some(message)) if message.contains("required") =>
        "firstName" -> I18n.t("authenticate.inputs.first-name.empty-field-error")
      case ValidationError("age", _) =>
        "age" -> I18n.t("authenticate.inputs.age.format-error")
      case ValidationError("postalCode", _) =>
        "postalCode" -> I18n.t("authenticate.inputs.postal-code.format-error")
      case ValidationError("profession", _) =>
        "profession" -> I18n.t("authenticate.inputs.job.format-error")
      case ValidationError(_, _) =>
        "global" -> I18n.t("authenticate.failure")
    }
  }
}
