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
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{User => UserModel}
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js
import scala.scalajs.js.Date

object UserProfileForm {

  final case class UserProfileFormProps(handleOnSubmit: (Self[UserProfileFormProps, UserProfileFormState]) => (
                                          FormSyntheticEvent[HTMLInputElement]
                                        ) => Unit,
                                        user: UserModel)

  final case class UserProfileFormState(fields: Map[String, String],
                                        errors: Map[String, String],
                                        message: String = "",
                                        isEdited: Boolean)

  val reactClass: ReactClass =
    React
      .createClass[UserProfileFormProps, UserProfileFormState](
        displayName = "UserProfileForm",
        getInitialState = self => {
          UserProfileFormState(fields = Map(), errors = Map(), isEdited = false)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(
            _.copy(
              fields = Map(
                "firstName" -> props.wrapped.user.firstName
                  .getOrElse(""),
                "age" -> props.wrapped.user.profile.flatMap { profile =>
                  profile.dateOfBirth.map { date =>
                    Math.abs(new Date(Date.now() - date.getTime()).getUTCFullYear() - 1970).toString
                  }
                }.getOrElse(""),
                "profession" -> props.wrapped.user.profile
                  .flatMap(_.profession)
                  .getOrElse(""),
                "postalCode" -> props.wrapped.user.profile
                  .flatMap(_.postalCode)
                  .getOrElse("")
              )
            )
          )
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

          <.div(^.className := EditingUserProfileStyles.wrapper)(
            <.h2(^.className := EditingUserProfileStyles.title)(s"${I18n.t("user-profile.form.title")}"),
            <.form(^.onSubmit := self.props.wrapped.handleOnSubmit(self))(
              <.div(^.className := EditingUserProfileStyles.inputGroup)(
                <.label(
                  ^.`for` := s"firstName",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(I18n.t("user-profile.form.inputs.first-name.label")),
                <.div(
                  ^.className := js
                    .Array(EditingUserProfileStyles.inputField, EditingUserProfileStyles.firstNameInputWithIconWrapper)
                )(
                  <.input(
                    ^.id := s"firstName",
                    ^.`type`.text,
                    ^.required := true,
                    ^.value := self.state.fields
                      .getOrElse("firstName", ""),
                    ^.onChange := updateField("firstName")
                  )()
                )
              ),
              if (self.state.errors.getOrElse("firstName", "") != "") {
                <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                  unescape(self.state.errors.getOrElse("firstName", ""))
                )
              },
              <.div(^.className := EditingUserProfileStyles.inputGroup)(
                <.label(
                  ^.`for` := s"age",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(I18n.t("user-profile.form.inputs.age.label")),
                <.div(
                  ^.className := js
                    .Array(EditingUserProfileStyles.inputField, EditingUserProfileStyles.ageInputWithIconWrapper)
                )(
                  <.input(
                    ^.id := s"age",
                    ^.`type`.number,
                    ^.required := false,
                    ^.min := 13,
                    ^.max := 128,
                    ^.value := self.state.fields.getOrElse("age", ""),
                    ^.onChange := updateField("age")
                  )()
                )
              ),
              if (self.state.errors.getOrElse("age", "") != "") {
                <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                  unescape(self.state.errors.getOrElse("age", ""))
                )
              },
              <.div(^.className := EditingUserProfileStyles.inputGroup)(
                <.label(
                  ^.`for` := s"profession",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(I18n.t("user-profile.form.inputs.profession.label")),
                <.div(
                  ^.className := js
                    .Array(EditingUserProfileStyles.inputField, EditingUserProfileStyles.professionInputWithIconWrapper)
                )(
                  <.input(
                    ^.id := s"profession",
                    ^.`type`.text,
                    ^.required := false,
                    ^.value := self.state.fields
                      .getOrElse("profession", ""),
                    ^.onChange := updateField("profession")
                  )()
                )
              ),
              if (self.state.errors.getOrElse("profession", "") != "") {
                <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                  unescape(self.state.errors.getOrElse("profession", ""))
                )
              },
              <.div(^.className := EditingUserProfileStyles.inputGroup)(
                <.label(
                  ^.`for` := s"postalCode",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(I18n.t("user-profile.form.inputs.postal-code.label")),
                <.div(
                  ^.className := js
                    .Array(EditingUserProfileStyles.inputField, EditingUserProfileStyles.postalCodeInputWithIconWrapper)
                )(
                  <.input(
                    ^.id := s"postalCode",
                    ^.`type`.text,
                    ^.required := false,
                    ^.value := self.state.fields
                      .getOrElse("postalCode", ""),
                    ^.onChange := updateField("postalCode")
                  )()
                )
              ),
              if (self.state.errors.getOrElse("postalCode", "") != "") {
                <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                  unescape(self.state.errors.getOrElse("postalCode", ""))
                )
              },
              <.div(^.className := js.Array(EditingUserProfileStyles.buttonGroup, EditingUserProfileStyles.success))(
                self.state.message
              ),
              if (self.state.errors.getOrElse("global", "") != "") {
                <.p(^.className := js.Array(EditingUserProfileStyles.buttonGroup, InputStyles.errorMessage))(
                  unescape(self.state.errors.getOrElse("global", ""))
                )
              },
              <.div(^.className := EditingUserProfileStyles.buttonGroup)(
                <.button(
                  ^.className := js
                    .Array(
                      CTAStyles.basic,
                      CTAStyles.basicOnButton,
                      EditingUserProfileStyles.submitButton(self.state.isEdited)
                    ),
                  ^.`type` := s"submit",
                  ^.onClick := disableButtonState
                )(
                  <.i(^.className := js.Array(FontAwesomeStyles.save, EditingUserProfileStyles.submitButtonIcon))(),
                  <.span()(s"${I18n.t("user-profile.form.cta")}")
                )
              )
            ),
            <.style()(EditingUserProfileStyles.render[String])
          )
        }
      )
}
