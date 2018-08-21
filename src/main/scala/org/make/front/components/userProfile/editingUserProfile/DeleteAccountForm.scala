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

package org.make.front.components.userProfile.editingUserProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.NewPasswordInput.NewPasswordInputProps
import org.make.front.components.authenticate.NewPasswordInputStyles
import org.make.front.components.authenticate.recoverPassword.RecoverPasswordContainer.RecoverPasswordContainerProps
import org.make.front.components.modals.Modal.ModalProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.base.LayoutRulesStyles
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

object DeleteAccountForm {
  final case class DeleteAccountFormProps(handleOnSubmit: (Self[DeleteAccountFormProps, DeleteAccountFormState]) => (
                                            FormSyntheticEvent[HTMLInputElement]
                                          ) => Unit,
                                          userHasPassword: Boolean)

  final case class DeleteAccountFormState(fields: Map[String, String],
                                          errors: Map[String, String],
                                          message: String = "",
                                          isEdited: Boolean,
                                          isForgotPasswordModalOpened: Boolean = false)

  val reactClass: ReactClass =
    React
      .createClass[DeleteAccountFormProps, DeleteAccountFormState](
        displayName = "DeleteAccountForm",
        getInitialState = self => {
          DeleteAccountFormState(fields = Map(), errors = Map(), isEdited = false)
        },
        render = self => {
          def updateField(name: String): (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
            val inputValue = event.target.value
            self.setState(
              state =>
                state.copy(
                  fields = state.fields + (name -> inputValue),
                  errors = state.errors + (name -> ""),
                  message = "",
                  isEdited = true
              )
            )
          }

          def disableButtonState: () => Unit = { () =>
            self.setState(_.copy(isEdited = false))
          }

          def openForgotPasswordModal() = () => {
            self.setState(state => state.copy(isForgotPasswordModalOpened = true))
          }

          def toggleResetPasswordModal() = () => {
            self.setState(state => state.copy(isForgotPasswordModalOpened = !self.state.isForgotPasswordModalOpened))
          }


          <.div(^.className := EditingUserProfileStyles.wrapper)(
            <.div(^.className := EditingUserProfileStyles.sep)(),
            <.div(^.className := EditingUserProfileStyles.sep)(),
            <.h2(^.className := EditingUserProfileStyles.title)(s"${I18n.t("user-profile.delete-account.title")}"),
            <.p(^.className := js.Array(EditingUserProfileStyles.buttonGroup, EditingUserProfileStyles.label))(
              s"${I18n.t("user-profile.delete-account.description")}"
            ),
            <.form(^.onSubmit := self.props.wrapped.handleOnSubmit(self))(
              if (self.props.wrapped.userHasPassword) {
                Seq(
                  <.div(^.className := EditingUserProfileStyles.inputGroup)(
                    <.label(
                      ^.`for` := s"deleteaccountpassword",
                      ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                    )(I18n.t("user-profile.delete-account.password.label")),
                      <.NewPasswordInputComponent(
                        ^.wrapped := NewPasswordInputProps(
                          value = self.state.fields.get("password").getOrElse(""),
                          id = s"deleteaccountpassword",
                          htmlClass = js.Array(EditingUserProfileStyles.inputField, NewPasswordInputStyles.withIconWrapper),
                          required = true,
                          placeHolder = s"${I18n
                            .t("user-profile.delete-account.password.placeholder")}",
                          onChange = updateField("password")
                        )
                      )()
                  ),
                  self.state.errors.get("password").map { error =>
                    <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                      if(!error.isEmpty){
                        <("Error")()(
                          unescape(error),
                          unescape("&nbsp;"),
                          <.button(^.onClick := openForgotPasswordModal())(
                            I18n
                              .t("user-profile.reset-password.old-password.wrong-password-alt-extra")
                          )
                        )

                      }

                    )
                  }
                )
              } else {
                Seq(

                  <.div(^.className := EditingUserProfileStyles.inputGroup)(
                    <.label(
                      ^.`for` := s"useremail",
                      ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                    )(I18n.t("user-profile.delete-account.email.label")),
                    <.div(
                      ^.className := js
                        .Array(EditingUserProfileStyles.inputField, EditingUserProfileStyles.firstNameInputWithIconWrapper)
                    )(
                      <.input(
                        ^.id := s"useremail",
                        ^.`type`.text,
                        ^.required := true,
                        ^.value := self.state.fields.get("email").getOrElse(""),
                        ^.placeholder := s"${I18n
                          .t("user-profile.delete-account.email.placeholder")}",
                        ^.onChange := updateField("email")
                      )()
                    )
                  ),
                  self.state.errors.get("email").map { error =>
                    <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                      unescape(error)
                    )
                  }
                )
              },
              self.state.errors.get("global").map { error =>
                <.p(^.className := js.Array(EditingUserProfileStyles.buttonGroup, InputStyles.errorMessage))(
                  unescape(error)
                )
              },
              <.div(^.className := EditingUserProfileStyles.buttonGroup)(
                <.button(
                  ^.className := js
                    .Array(
                      CTAStyles.basic,
                      CTAStyles.basicOnButton,
                      EditingUserProfileStyles.submitButton(self.state.isEdited)),
                  ^.`type` := s"submit",
                  ^.onClick := disableButtonState
                )(
                  <.i(^.className := js.Array(FontAwesomeStyles.trashAlt, EditingUserProfileStyles.submitButtonIcon))(),
                  <.span()(s"${I18n.t("user-profile.delete-account.cta")}")
                )
              )
            ),<.ModalComponent(
              ^.wrapped := ModalProps(
                isModalOpened = self.state.isForgotPasswordModalOpened,
                closeCallback = toggleResetPasswordModal()
              )
            )(
              <.div(^.className := LayoutRulesStyles.evenNarrowerCenteredRow)(
              <.RecoverPasswordContainerComponent(^.wrapped := RecoverPasswordContainerProps(
                onRecoverPasswordSuccess = toggleResetPasswordModal()
              ))()
            )
            ),
            <.style()(EditingUserProfileStyles.render[String])
          )
        }
      )
}
