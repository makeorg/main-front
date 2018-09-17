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

object OrganisationProfileForm {

  final case class OrganisationProfileFormProps(
    handleOnSubmit: Self[OrganisationProfileFormProps, OrganisationProfileFormState] => FormSyntheticEvent[
      HTMLInputElement
    ] => Unit,
    user: UserModel
  )

  final case class OrganisationProfileFormState(fields: Map[String, String],
                                                errors: Map[String, String],
                                                message: String = "",
                                                isEdited: Boolean)

  val reactClass: ReactClass =
    React
      .createClass[OrganisationProfileFormProps, OrganisationProfileFormState](
        displayName = "ActorProfileForm",
        getInitialState = _ => {
          OrganisationProfileFormState(fields = Map(), errors = Map(), isEdited = false)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(
            _.copy(
              fields = Map(
                "organisationName" -> props.wrapped.user.organisationName
                  .getOrElse(""),
                "description" -> props.wrapped.user.profile.flatMap(_.description).getOrElse("")
              )
            )
          )
        },
        render = self => {
          def updateField(name: String): FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
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
                  ^.`for` := s"organisationName",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(I18n.t("user-profile.form.inputs.organisation-name.label")),
                <.div(
                  ^.className := js
                    .Array(EditingUserProfileStyles.inputField, EditingUserProfileStyles.firstNameInputWithIconWrapper)
                )(
                  <.input(
                    ^.id := s"organisationName",
                    ^.`type`.text,
                    ^.required := true,
                    ^.value := self.state.fields
                      .getOrElse("organisationName", ""),
                    ^.onChange := updateField("organisationName")
                  )()
                )
              ),
              if (self.state.errors.getOrElse("organisationName", "") != "") {
                <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                  unescape(self.state.errors.getOrElse("organisationName", ""))
                )
              },
              <.div(^.className := EditingUserProfileStyles.inputGroup)(
                <.label(
                  ^.`for` := s"description",
                  ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                )(
                  I18n.t("user-profile.form.inputs.description.label"),
                  <.p()(unescape(" ("), if (self.state.fields.get("description").isEmpty) {
                    unescape("0")
                  } else {
                    self.state.fields
                      .get("description")
                      .map { field =>
                        field.length
                      }
                      .toSeq
                  }, unescape("/450 "), I18n.t("user-profile.form.inputs.char"), unescape(")"))
                ),
                <.div(
                  ^.className := js
                    .Array(
                      EditingUserProfileStyles.inputTextArea,
                      EditingUserProfileStyles.descriptionInputWithIconWrapper
                    )
                )(
                  <.textarea(
                    ^.id := s"description",
                    ^.`type`.text,
                    ^.required := false,
                    ^.value := self.state.fields.getOrElse("description", ""),
                    ^.onChange := updateField("description"),
                    ^.rows := 5
                  )()
                )
              ),
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
