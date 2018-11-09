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
import org.make.front.models.Gender.{Female, Male, Other}
import org.make.front.models.{SocioProfessionalCategory, User => UserModel}
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js
import scala.scalajs.js.Date

object UserProfileForm {

  final case class UserProfileFormProps(
    handleOnSubmit: (Self[UserProfileFormProps, UserProfileFormState], Map[String, HTMLInputElement]) => Unit,
    user: UserModel
  )

  final case class UserProfileFormState(fields: Map[String, String],
                                        errors: Map[String, String],
                                        message: String = "",
                                        isEdited: Boolean,
                                        displayGender: Boolean = false,
                                        displayCsp: Boolean = false)

  val reactClass: ReactClass =
    React
      .createClass[UserProfileFormProps, UserProfileFormState](
        displayName = "UserProfileForm",
        getInitialState = self => {
          UserProfileFormState(fields = Map(), errors = Map(), isEdited = false)
        },
        componentWillReceiveProps = { (self, props) =>
          val genderNonEmpty: Boolean = props.wrapped.user.profile.flatMap(_.gender).nonEmpty
          val cspNonEmpty: Boolean = props.wrapped.user.profile.flatMap(_.socioProfessionalCategory).nonEmpty
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
                  .getOrElse(""),
                "description" -> props.wrapped.user.profile
                  .flatMap(_.description)
                  .getOrElse(""),
                "gender" -> props.wrapped.user.profile.flatMap(_.gender.map(_.shortName)).getOrElse(""),
                "socioProfessionalCategory" -> props.wrapped.user.profile
                  .flatMap(_.socioProfessionalCategory.map(_.shortName))
                  .getOrElse("")
              ).filter {
                case (key, value) => value.nonEmpty
              },
              displayCsp = cspNonEmpty,
              displayGender = genderNonEmpty
            )
          )
        },
        render = self => {
          var fieldsRefs: Map[String, HTMLInputElement] = Map.empty

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

          def setFieldRef(name: String): HTMLInputElement => Unit = { field =>
            fieldsRefs = fieldsRefs + (name -> field)
          }

          def submit: () => Unit = { () =>
            self.setState(_.copy(isEdited = false))
            self.props.wrapped.handleOnSubmit(self, fieldsRefs)
          }

          <.div(^.className := EditingUserProfileStyles.wrapper)(
            <.h2(^.className := EditingUserProfileStyles.title)(s"${I18n.t("user-profile.form.title")}"),
            <.form()(
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
                    ^.onChange := updateField("firstName"),
                    ^.ref := setFieldRef("firstName")
                  )()
                )
              ),
              if (self.state.errors.getOrElse("firstName", "").nonEmpty) {
                <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                  unescape(self.state.errors.getOrElse("firstName", ""))
                )
              },
              if (self.state.displayGender) {
                Seq(
                  <.div(^.className := EditingUserProfileStyles.inputGroup)(
                    <.label(
                      ^.`for` := s"gender",
                      ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                    )(I18n.t("user-profile.form.inputs.gender.label")),
                    <.div(
                      ^.className := js
                        .Array(EditingUserProfileStyles.inputField, EditingUserProfileStyles.genderInputWithIconWrapper)
                    )(
                      <.select(
                        ^.className := js.Array(
                          EditingUserProfileStyles.selectInput,
                          EditingUserProfileStyles.firstOption(self.state.fields.getOrElse("gender", "").isEmpty)
                        ),
                        ^.required := false,
                        ^.onChange := updateField("gender"),
                        ^.value := self.state.fields.getOrElse("gender", "")
                      )(
                        <.option(^.value := "")(""),
                        <.option(^.value := Male.shortName)(
                          I18n.t(s"authenticate.inputs.gender.values.${Male.shortName}")
                        ),
                        <.option(^.value := Female.shortName)(
                          I18n.t(s"authenticate.inputs.gender.values.${Female.shortName}")
                        ),
                        <.option(^.value := Other.shortName)(
                          I18n.t(s"authenticate.inputs.gender.values.${Other.shortName}")
                        )
                      )
                    )
                  ),
                  if (self.state.errors.getOrElse("gender", "").nonEmpty) {
                    <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                      unescape(self.state.errors.getOrElse("gender", ""))
                    )
                  }
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
              if (self.state.errors.getOrElse("age", "").nonEmpty) {
                <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                  unescape(self.state.errors.getOrElse("age", ""))
                )
              },
              if (self.state.displayCsp) {
                Seq(
                  <.div(^.className := EditingUserProfileStyles.inputGroup)(
                    <.label(
                      ^.`for` := s"socioProfessionalCategory",
                      ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                    )(I18n.t("user-profile.form.inputs.csp.label")),
                    <.div(
                      ^.className := js
                        .Array(EditingUserProfileStyles.inputField, EditingUserProfileStyles.cspInputWithIconWrapper)
                    )(
                      <.select(
                        ^.className := js.Array(
                          EditingUserProfileStyles.selectInput,
                          EditingUserProfileStyles
                            .firstOption(self.state.fields.getOrElse("socioProfessionalCategory", "").isEmpty)
                        ),
                        ^.required := false,
                        ^.onChange := updateField("socioProfessionalCategory"),
                        ^.value := self.state.fields.getOrElse("socioProfessionalCategory", "")
                      )(
                        <.option(^.value := "")(""),
                        <.option(^.value := SocioProfessionalCategory.Farmers.shortName)(
                          I18n.t(s"authenticate.inputs.csp.values.${SocioProfessionalCategory.Farmers.shortName}")
                        ),
                        <.option(^.value := SocioProfessionalCategory.ArtisansMerchantsCompanyDirector.shortName)(
                          I18n.t(
                            s"authenticate.inputs.csp.values.${SocioProfessionalCategory.ArtisansMerchantsCompanyDirector.shortName}"
                          )
                        ),
                        <.option(
                          ^.value := SocioProfessionalCategory.ManagersAndHigherIntellectualOccupations.shortName
                        )(
                          I18n.t(
                            s"authenticate.inputs.csp.values.${SocioProfessionalCategory.ManagersAndHigherIntellectualOccupations.shortName}"
                          )
                        ),
                        <.option(^.value := SocioProfessionalCategory.IntermediateProfessions.shortName)(
                          I18n.t(
                            s"authenticate.inputs.csp.values.${SocioProfessionalCategory.IntermediateProfessions.shortName}"
                          )
                        ),
                        <.option(^.value := SocioProfessionalCategory.Employee.shortName)(
                          I18n.t(s"authenticate.inputs.csp.values.${SocioProfessionalCategory.Employee.shortName}")
                        ),
                        <.option(^.value := SocioProfessionalCategory.Workers.shortName)(
                          I18n.t(s"authenticate.inputs.csp.values.${SocioProfessionalCategory.Workers.shortName}")
                        ),
                        <.option(^.value := SocioProfessionalCategory.HighSchoolStudent.shortName)(
                          I18n.t(
                            s"authenticate.inputs.csp.values.${SocioProfessionalCategory.HighSchoolStudent.shortName}"
                          )
                        ),
                        <.option(^.value := SocioProfessionalCategory.Student.shortName)(
                          I18n.t(s"authenticate.inputs.csp.values.${SocioProfessionalCategory.Student.shortName}")
                        ),
                        <.option(^.value := SocioProfessionalCategory.Apprentice.shortName)(
                          I18n.t(s"authenticate.inputs.csp.values.${SocioProfessionalCategory.Apprentice.shortName}")
                        ),
                        <.option(^.value := SocioProfessionalCategory.Other.shortName)(
                          I18n.t(s"authenticate.inputs.csp.values.${SocioProfessionalCategory.Other.shortName}")
                        )
                      )
                    )
                  ),
                  if (self.state.errors.getOrElse("socioProfessionalCategory", "").nonEmpty) {
                    <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                      unescape(self.state.errors.getOrElse("socioProfessionalCategory", ""))
                    )
                  }
                )
              } else {
                Seq(
                  <.div(^.className := EditingUserProfileStyles.inputGroup)(
                    <.label(
                      ^.`for` := s"profession",
                      ^.className := js.Array(EditingUserProfileStyles.inputLabel, EditingUserProfileStyles.label)
                    )(I18n.t("user-profile.form.inputs.profession.label")),
                    <.div(
                      ^.className := js
                        .Array(
                          EditingUserProfileStyles.inputField,
                          EditingUserProfileStyles.professionInputWithIconWrapper
                        )
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
                  if (self.state.errors.getOrElse("profession", "").nonEmpty) {
                    <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                      unescape(self.state.errors.getOrElse("profession", ""))
                    )
                  }
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
                    ^.onChange := updateField("postalCode"),
                    ^.ref := setFieldRef("postalCode")
                  )()
                )
              ),
              if (self.state.errors.getOrElse("postalCode", "").nonEmpty) {
                <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                  unescape(self.state.errors.getOrElse("postalCode", ""))
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
              if (self.state.errors.getOrElse("description", "").nonEmpty) {
                <.p(^.className := js.Array(InputStyles.errorMessage, EditingUserProfileStyles.inlineMessage))(
                  unescape(self.state.errors.getOrElse("description", ""))
                )
              },
              <.div(^.className := js.Array(EditingUserProfileStyles.buttonGroup, EditingUserProfileStyles.success))(
                self.state.message
              ),
              if (self.state.errors.getOrElse("global", "").nonEmpty) {
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
                  ^.onClick := submit
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
