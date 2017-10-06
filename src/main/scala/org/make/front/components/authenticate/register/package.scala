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
        "email" -> I18n.t("form.register.errorAlreadyExist")
      case ValidationError("email", Some(message)) if message.contains("required") =>
        "email" -> I18n.t("form.register.errorBlankEmail")
      case ValidationError("email", _) =>
        "email" -> I18n.t("form.register.errorInvalidEmail")
      case ValidationError("password", Some(message)) if message.contains("required") =>
        "password" -> I18n.t("form.register.errorBlankPassword")
      case ValidationError("password", _) =>
        "password" -> I18n.t("form.register.errorMinPassword", Replacements("min" -> PasswordConstraint.min.toString))
      case ValidationError("firstName", Some(message)) if message.contains("required") =>
        "firstName" -> I18n.t("form.register.errorBlankFirstName")
      case ValidationError("age", _) =>
        "age" -> I18n.t("form.register.errorChoiceAge")
      case ValidationError("postalCode", _) =>
        "postalCode" -> I18n.t("form.register.errorMaxPostalCode")
      case ValidationError("profession", _) =>
        "profession" -> I18n.t("form.register.errorProfession")
      case ValidationError(_, _) =>
        "global" -> I18n.t("form.register.errorRegistrationFailed")
    }
  }
}
