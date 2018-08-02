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

  final case class UserProfileFormState(fields: Map[String, String], errors: Map[String, String], message: String = "")

  val reactClass: ReactClass =
    React
      .createClass[UserProfileFormProps, UserProfileFormState](
        displayName = "UserProfileForm",
        getInitialState = self => {
          UserProfileFormState(fields = Map(), errors = Map())
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(
            _.copy(
              fields = Map(
                "firstName" -> props.wrapped.user.firstName
                  .getOrElse(I18n.t("user-profile.form.inputs.first-name.label")),
                "age" -> props.wrapped.user.profile.flatMap { profile =>
                  profile.dateOfBirth.map { date =>
                    Math.abs((new Date(Date.now() - date.getTime())).getUTCFullYear() - 1970)
                  }
                }.getOrElse(0).toString,
                "profession" -> props.wrapped.user.profile
                  .flatMap(_.profession)
                  .getOrElse(I18n.t("user-profile.form.inputs.profession.label")),
                "postalCode" -> props.wrapped.user.profile
                  .flatMap(_.postalCode)
                  .getOrElse(I18n.t("user-profile.form.inputs.postal-code.label"))
              )
            )
          )
        },
        render = self => {
          def updateField(name: String): (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
            val inputValue = event.target.value
            self.setState(
              state => state.copy(fields = state.fields + (name -> inputValue), errors = state.errors + (name -> ""))
            )
          }
          <.div(^.className := EditingUserProfileStyles.wrapper)(
            <.h2(^.className := EditingUserProfileStyles.title)(s"${I18n.t("user-profile.form.title")}"),
            <.form(^.onSubmit := self.props.wrapped.handleOnSubmit(self))(
              <.div(^.className := EditingUserProfileStyles.inputGroup)(
                <.label(
                  ^.`for` := s"firstName",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(I18n.t("user-profile.form.inputs.first-name.label")),
                <.input(
                  ^.id := s"firstName",
                  ^.className := EditingUserProfileStyles.inputField,
                  ^.`type`.text,
                  ^.placeholder := I18n.t("user-profile.form.inputs.first-name.label"),
                  ^.value := self.state.fields
                    .getOrElse("firstName", I18n.t("user-profile.form.inputs.first-name.label")),
                  ^.onChange := updateField("firstName")
                )()
              ),
              if (self.state.errors.getOrElse("firstName", "") != "") {
                <.p(^.className := InputStyles.errorMessage.htmlClass)(
                  unescape(self.state.errors.getOrElse("firstName", ""))
                )
              },
              <.div(^.className := EditingUserProfileStyles.inputGroup)(
                <.label(
                  ^.`for` := s"age",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(I18n.t("user-profile.form.inputs.age.label")),
                <.input(
                  ^.id := s"age",
                  ^.className := EditingUserProfileStyles.inputField,
                  ^.`type`.number,
                  ^.placeholder := I18n.t("user-profile.form.inputs.age.label"),
                  ^.value := self.state.fields.getOrElse("age", I18n.t("user-profile.form.inputs.age.label")),
                  ^.onChange := updateField("age")
                )()
              ),
              if (self.state.errors.getOrElse("age", "") != "") {
                <.p(^.className := InputStyles.errorMessage.htmlClass)(unescape(self.state.errors.getOrElse("age", "")))
              },
              <.div(^.className := EditingUserProfileStyles.inputGroup)(
                <.label(
                  ^.`for` := s"profession",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(I18n.t("user-profile.form.inputs.profession.label")),
                <.input(
                  ^.id := s"profession",
                  ^.className := EditingUserProfileStyles.inputField,
                  ^.`type`.text,
                  ^.placeholder := I18n.t("user-profile.form.inputs.profession.label"),
                  ^.value := self.state.fields
                    .getOrElse("profession", I18n.t("user-profile.form.inputs.profession.label")),
                  ^.onChange := updateField("profession")
                )()
              ),
              if (self.state.errors.getOrElse("profession", "") != "") {
                <.p(^.className := InputStyles.errorMessage.htmlClass)(
                  unescape(self.state.errors.getOrElse("profession", ""))
                )
              },
              <.div(^.className := EditingUserProfileStyles.inputGroup)(
                <.label(
                  ^.`for` := s"postalCode",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(I18n.t("user-profile.form.inputs.postal-code.label")),
                <.input(
                  ^.id := s"postalCode",
                  ^.className := EditingUserProfileStyles.inputField,
                  ^.`type`.text,
                  ^.placeholder := I18n.t("user-profile.form.inputs.postal-code.label"),
                  ^.value := self.state.fields
                    .getOrElse("postalCode", I18n.t("user-profile.form.inputs.postal-code.label")),
                  ^.onChange := updateField("postalCode")
                )()
              ),
              if (self.state.errors.getOrElse("postalCode", "") != "") {
                <.p(^.className := InputStyles.errorMessage.htmlClass)(
                  unescape(self.state.errors.getOrElse("postalCode", ""))
                )
              },
              <.div(^.className := js.Array(EditingUserProfileStyles.buttonGroup, EditingUserProfileStyles.success))(
                self.state.message
              ),
              <.div(^.className := EditingUserProfileStyles.buttonGroup)(
                <.button(
                  ^.className := js
                    .Array(CTAStyles.basic, CTAStyles.basicOnButton, EditingUserProfileStyles.submitGreyButton),
                  ^.`type` := s"submit"
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
