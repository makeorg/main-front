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

package org.make.front.components.userProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, MouseSyntheticEvent}
import org.make.client.BadRequestHttpException
import org.make.core.validation.{NotBlankConstraint, PasswordConstraint}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.authenticate.NewPasswordInput.NewPasswordInputProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object ResetPassword {
  case class ResetPasswordProps(hasPassword: Boolean, changePassword: (Option[String], String) => Future[Unit])
  case class ResetPasswordState(oldPassword: Option[String] = None,
                                errorOldPassword: Option[String] = None,
                                newPassword: Option[String] = None,
                                errorNewPassword: Option[String] = None)

  lazy val reactClass: ReactClass =
    React
      .createClass[ResetPasswordProps, ResetPasswordState](
        displayName = "ResetPassword",
        getInitialState = _ => ResetPasswordState(),
        render = self => {
          def updateOldPassword: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val inputValue = event.target.value
            val oldPassword = if (!inputValue.isEmpty) Some(inputValue) else None
            self.setState(state => state.copy(oldPassword = oldPassword, errorOldPassword = None))
          }

          def updateNewPassword: FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
            val inputValue = event.target.value
            val newPassword = if (!inputValue.isEmpty) Some(inputValue) else None
            self.setState(state => state.copy(newPassword = newPassword, errorNewPassword = None))
          }

          def changePassword: MouseSyntheticEvent => Unit = {
            _ =>
              val oldPasswordError: Option[String] =
                if (self.props.wrapped.hasPassword) {
                  NotBlankConstraint
                    .validate(
                      self.state.oldPassword,
                      Map("notBlank" -> I18n.t("authenticate.inputs.password.empty-field-error-message"))
                    )
                    .map(_.message)
                    .headOption
                } else {
                  None
                }
              val newPasswordError: Option[String] =
                NotBlankConstraint
                  .&(PasswordConstraint)
                  .validate(
                    self.state.newPassword,
                    Map(
                      "notBlank" -> I18n.t("authenticate.inputs.password.empty-field-error-message"),
                      "minMessage" -> I18n
                        .t(
                          "authenticate.inputs.password.format-error-message",
                          Replacements("min" -> PasswordConstraint.min.toString)
                        )
                    )
                  )
                  .map(_.message)
                  .headOption

              if (oldPasswordError.orElse(newPasswordError).isDefined) {
                self.setState(_.copy(errorOldPassword = oldPasswordError, errorNewPassword = newPasswordError))
              } else {
                self.props.wrapped.changePassword(self.state.oldPassword, self.state.newPassword.get).onComplete {
                  case Success(_) => self.setState(ResetPasswordState())
                  case Failure(e: BadRequestHttpException) =>
                    self.setState(
                      _.copy(errorOldPassword = Some(I18n.t("user-profile.reset-password.old-password.wrong-password")))
                    )
                  case Failure(_) =>
                    self.setState(_.copy(errorNewPassword = Some(I18n.t("user-profile.reset-password.error-message"))))
                }
              }
          }

          val oldPasswordElement = if (self.props.wrapped.hasPassword) {
            <.div()(
              <.label()(I18n.t("user-profile.reset-password.old-password.label")),
              <.NewPasswordInputComponent(
                ^.wrapped := NewPasswordInputProps(
                  value = self.state.oldPassword.getOrElse(""),
                  required = true,
                  placeHolder =
                    s"${I18n.t("user-profile.reset-password.old-password.placeholder")} ${I18n.t("authenticate.inputs.required")}",
                  onChange = updateOldPassword
                )
              )(),
              if (self.state.errorOldPassword.isDefined) {
                <.p()(unescape(self.state.errorOldPassword.getOrElse("")))
              }
            )
          }
          <.div()(
            <.h3()(I18n.t("user-profile.reset-password.title")),
            oldPasswordElement,
            <.div()(
              <.label()(I18n.t("user-profile.reset-password.new-password.label")),
              <.NewPasswordInputComponent(
                ^.wrapped := NewPasswordInputProps(
                  value = self.state.newPassword.getOrElse(""),
                  required = true,
                  placeHolder = s"${I18n
                    .t("user-profile.reset-password.new-password.placeholder")} ${I18n
                    .t("user-profile.reset-password.inputs.required")}",
                  onChange = updateNewPassword
                )
              )(),
              if (self.state.errorNewPassword.isDefined) {
                <.p()(unescape(self.state.errorNewPassword.getOrElse("")))
              }
            ),
            <.div()(
              <.button(^.className := js.Array(CTAStyles.basicOnButton, CTAStyles.basic), ^.onClick := changePassword)(
                <.i(^.className := js.Array(FontAwesomeStyles.thumbsUp))(),
                unescape("&nbsp;" + I18n.t("user-profile.reset-password.send-cta"))
              )
            ),
            <.style()(ResetPasswordStyles.render[String])
          )
        }
      )
}

object ResetPasswordStyles extends StyleSheet.Inline {}
