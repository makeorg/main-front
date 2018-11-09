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

package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import io.github.shogowada.statictags.Element
import org.make.client.BadRequestHttpException
import org.make.core.validation._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.NewPasswordInput.NewPasswordInputProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.Gender.{Female, Male, Other}
import org.make.front.models.{Gender, SocioProfessionalCategory}
import org.make.front.styles._
import org.make.front.styles.base.{RWDHideRulesStyles, TextStyles}
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.util.{Failure, Success}

object RegisterWithEmailExpanded {

  val reactClass: ReactClass =
    React.createClass[RegisterProps, RegisterState](
      displayName = "RegisterWithEmailExpanded",
      getInitialState = { _ =>
        RegisterState.empty
      },
      componentDidMount = { self =>
        TrackingService
          .track(
            eventName = "display-signup-form",
            trackingContext = self.props.wrapped.trackingContext,
            parameters = self.props.wrapped.trackingParameters + ("signup-type" -> "standard"),
            internalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters
          )
        if (self.props.wrapped.additionalFields.contains(SignUpField.HiddenOptOut))
          self.setState(state => state.copy(fields = state.fields + ("optIn" -> "")))
      },
      render = { self =>
        var fieldsRefs: Map[String, HTMLInputElement] = Map.empty

        def updateField(name: String): (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
          val inputValue = event.target.value
          self.setState(
            state => state.copy(fields = state.fields + (name -> inputValue), errors = state.errors + (name -> ""))
          )
        }

        def setFieldRef(name: String): HTMLInputElement => Unit = { field =>
          fieldsRefs = fieldsRefs + (name -> field)
        }

        def toggleFieldCheckBox(name: String): () => Unit = { () =>
          val value: String = if (self.state.fields.get(name).contains(name)) {
            ""
          } else {
            name
          }
          self.setState(
            state => state.copy(fields = state.fields + (name -> value), errors = state.errors + (name -> ""))
          )
        }

        val fieldsValidation: js.Array[(String, Constraint, Map[String, String])] = {
          js.Array(
            (
              "email",
              NotBlankConstraint.&(EmailConstraint),
              Map(
                "invalid" -> I18n.t("authenticate.inputs.email.format-error-message"),
                "notBlank" -> I18n.t("authenticate.inputs.email.empty-field-error-message")
              )
            ),
            (
              "password",
              NotBlankConstraint.&(PasswordConstraint),
              Map(
                "notBlank" -> I18n.t("authenticate.inputs.password.empty-field-error-message"),
                "minMessage" -> I18n
                  .t(
                    "authenticate.inputs.password.format-error-message",
                    Replacements("min" -> PasswordConstraint.min.toString)
                  )
              )
            ),
            (
              "firstName",
              NotBlankConstraint,
              Map("notBlank" -> I18n.t("authenticate.inputs.first-name.empty-field-error-message"))
            ),
            (
              "gender",
              ChoiceConstraint(Gender.genders.keys.toJSArray),
              Map("invalid" -> I18n.t("authenticate.inputs.gender.format-error-message"))
            ),
            (
              "socioProfessionalCategory",
              ChoiceConstraint(SocioProfessionalCategory.csps.keys.toJSArray),
              Map("invalid" -> I18n.t("authenticate.inputs.csp.format-error-message"))
            )
          )
        }

        def onSubmit: (SyntheticEvent) => Unit = (event: SyntheticEvent) => {
          event.preventDefault()

          var errors: Map[String, String] = Map.empty

          val fieldsValue: Map[String, String] = fieldsRefs.map {
            case (key, field) => key -> field.value
          } ++ self.state.fields

          fieldsValidation.foreach {
            case (fieldName, constraint, translation) => {
              val fieldValue = fieldsValue.get(fieldName)
              val fieldErrors = constraint.validate(fieldValue, translation).map(_.message)
              if (fieldErrors.nonEmpty) {
                errors += (fieldName -> fieldErrors.head)
              }
            }
          }

          if (errors.nonEmpty) {
            self.setState(_.copy(errors = errors, fields = fieldsValue))
          } else {
            self.setState(_.copy(disableSubmit = true))
            self.props.wrapped.register(fieldsValue).onComplete {
              case Success(_) => self.setState(RegisterState.empty)
              case Failure(exception) =>
                exception match {
                  case exception: BadRequestHttpException =>
                    val errors = getErrorsMessagesFromApiErrors(exception.errors).toMap
                    self.setState(_.copy(errors = errors, fields = fieldsValue, disableSubmit = false))
                  case _ =>
                    self.setState(
                      state =>
                        state
                          .copy(
                            errors = state.errors + ("global" -> I18n.t("authenticate.error-message")),
                            fields = fieldsValue,
                            disableSubmit = false
                        )
                    )
                }
            }
          }
        }

        def getPartnerOptInCheckbox(field: SignUpField.PartnerOptIn): Seq[Element] = {
          val optInPartnerCheckValue: Boolean = self.state.fields.get("optInPartner") match {
            case Some("isOptInPartner") => true
            case _                      => false
          }

          Seq(
            <.input(
              ^.`type`.checkbox,
              ^.checked := optInPartnerCheckValue,
              ^.id := s"optInPartner",
              ^.value := s"isOptInPartner",
              ^.className := RWDHideRulesStyles.hide
            )(),
            <.label(
              ^.className := RegisterWithEmailExpandedStyles.customCheckboxLabel,
              ^.`for` := s"optinNewsletter",
              ^.onClick := toggleFieldCheckBox("optInPartner")
            )(
              <.span(^.className := RegisterWithEmailExpandedStyles.customCheckboxIconWrapper)(
                if (optInPartnerCheckValue) {
                  <.i(
                    ^.className := js.Array(FontAwesomeStyles.check, RegisterWithEmailExpandedStyles.customCheckedIcon)
                  )()
                }
              ),
              <.span(^.className := RegisterWithEmailExpandedStyles.label)(
                field.labels.find(_.language == self.props.wrapped.language).map(_.label).getOrElse("")
              )
            )
          )
        }

        val partnerOptIn: Option[SignUpField.PartnerOptIn] = self.props.wrapped.additionalFields
          .find(_.isInstanceOf[SignUpField.PartnerOptIn])
          .map(_.asInstanceOf[SignUpField.PartnerOptIn])

        <.form(^.onSubmit := onSubmit, ^.novalidate := true)(
          <.label(
            ^.className := js
              .Array(
                InputStyles.wrapper,
                InputStyles.withIcon,
                RegisterWithEmailExpandedStyles.emailInputWithIconWrapper
              )
          )(
            <.input(
              ^.`type`.email,
              ^.required := true,
              ^.placeholder := s"${I18n.t("authenticate.inputs.email.placeholder")} ${I18n.t("authenticate.inputs.required")}",
              ^.onChange := updateField("email"),
              ^.value := self.state.fields.getOrElse("email", ""),
              ^.ref := setFieldRef("email")
            )()
          ),
          if (self.state.errors.getOrElse("email", "").nonEmpty) {
            <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("email", "")))
          },
          <.div(^.className := RegisterWithEmailExpandedStyles.newPasswordInputComponentWrapper)(
            <.NewPasswordInputComponent(
              ^.wrapped := NewPasswordInputProps(
                value = self.state.fields.getOrElse("password", ""),
                required = true,
                placeHolder =
                  s"${I18n.t("authenticate.inputs.password.placeholder")} ${I18n.t("authenticate.inputs.required")}",
                onChange = updateField("password")
              )
            )()
          ),
          if (self.state.errors.getOrElse("password", "").nonEmpty) {
            <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("password", "")))
          },
          if (self.props.wrapped.additionalFields.contains(SignUpField.FirstName)) {
            Seq(
              <.label(
                ^.className := js.Array(
                  InputStyles.wrapper,
                  InputStyles.withIcon,
                  RegisterWithEmailExpandedStyles.firstNameInputWithIconWrapper
                )
              )(
                <.input(
                  ^.`type`.text,
                  ^.required := true,
                  ^.placeholder := s"${I18n.t("authenticate.inputs.first-name.placeholder")} ${I18n.t("authenticate.inputs.required")}",
                  ^.onChange := updateField("firstName"),
                  ^.value := self.state.fields.getOrElse("firstName", ""),
                  ^.ref := setFieldRef("firstName")
                )()
              ),
              if (self.state.errors.getOrElse("firstName", "").nonEmpty) {
                <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("firstName", "")))
              }
            )
          },
          if (self.props.wrapped.additionalFields.contains(SignUpField.Age)) {
            Seq(
              <.label(
                ^.className := js
                  .Array(
                    InputStyles.wrapper,
                    InputStyles.withIcon,
                    RegisterWithEmailExpandedStyles.ageInputWithIconWrapper
                  )
              )(
                // TODO: avoid number out of limit
                <.input(
                  ^.`type`.number,
                  ^.required := false,
                  ^.min := 13,
                  ^.max := 128,
                  ^.placeholder := s"${I18n.t("authenticate.inputs.age.placeholder")}",
                  ^.onChange := updateField("age"),
                  ^.value := self.state.fields.getOrElse("age", "")
                )()
              ),
              if (self.state.errors.getOrElse("age", "").nonEmpty) {
                <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("age", "")))
              }
            )
          },
          if (self.props.wrapped.additionalFields.contains(SignUpField.Gender)) {
            Seq(
              <.label(
                ^.className := js.Array(
                  InputStyles.wrapper,
                  InputStyles.withIcon,
                  RegisterWithEmailExpandedStyles.genderInputWithIconWrapper
                )
              )(
                <.select(
                  ^.className := js.Array(
                    RegisterWithEmailExpandedStyles.selectInput,
                    RegisterWithEmailExpandedStyles.firstOption(self.state.fields.getOrElse("gender", "").isEmpty)
                  ),
                  ^.required := false,
                  ^.onChange := updateField("gender"),
                  ^.value := self.state.fields.getOrElse("gender", "")
                )(
                  <.option(^.value := "")(I18n.t("authenticate.inputs.gender.placeholder")),
                  <.option(^.value := Male.shortName)(I18n.t(s"authenticate.inputs.gender.values.${Male.shortName}")),
                  <.option(^.value := Female.shortName)(
                    I18n.t(s"authenticate.inputs.gender.values.${Female.shortName}")
                  ),
                  <.option(^.value := Other.shortName)(I18n.t(s"authenticate.inputs.gender.values.${Other.shortName}"))
                )
              ),
              if (self.state.errors.getOrElse("gender", "").nonEmpty) {
                <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("gender", "")))
              }
            )
          },
          if (self.props.wrapped.additionalFields.contains(SignUpField.PostalCode)) {
            Seq(
              <.label(
                ^.className := js.Array(
                  InputStyles.wrapper,
                  InputStyles.withIcon,
                  RegisterWithEmailExpandedStyles.postalCodeInputWithIconWrapper
                )
              )(
                <.input(
                  ^.`type`.text,
                  ^.required := false,
                  ^.placeholder := s"${I18n.t("authenticate.inputs.postal-code.placeholder")}",
                  ^.onChange := updateField("postalCode"),
                  ^.value := self.state.fields.getOrElse("postalCode", ""),
                  ^.ref := setFieldRef("postalCode")
                )()
              ),
              if (self.state.errors.getOrElse("postalCode", "").nonEmpty) {
                <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("postalCode", "")))
              }
            )
          },
          if (self.props.wrapped.additionalFields.contains(SignUpField.Job)) {
            Seq(
              <.label(
                ^.className := js.Array(
                  InputStyles.wrapper,
                  InputStyles.withIcon,
                  RegisterWithEmailExpandedStyles.professionInputWithIconWrapper
                )
              )(
                <.input(
                  ^.`type`.text,
                  ^.required := false,
                  ^.placeholder := s"${I18n.t("authenticate.inputs.job.placeholder")}",
                  ^.onChange := updateField("profession"),
                  ^.value := self.state.fields.getOrElse("profession", "")
                )()
              ),
              if (self.state.errors.getOrElse("profession", "").nonEmpty) {
                <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("profession", "")))
              }
            )
          },
          if (self.props.wrapped.additionalFields.contains(SignUpField.Csp)) {
            Seq(
              <.label(
                ^.className := js.Array(
                  InputStyles.wrapper,
                  InputStyles.withIcon,
                  RegisterWithEmailExpandedStyles.professionInputWithIconWrapper
                )
              )(
                <.select(
                  ^.className := js.Array(
                    RegisterWithEmailExpandedStyles.selectInput,
                    RegisterWithEmailExpandedStyles
                      .firstOption(self.state.fields.getOrElse("socioProfessionalCategory", "").isEmpty)
                  ),
                  ^.required := false,
                  ^.onChange := updateField("socioProfessionalCategory"),
                  ^.value := self.state.fields.getOrElse("socioProfessionalCategory", "")
                )(
                  <.option(^.value := "")(I18n.t("authenticate.inputs.csp.placeholder")),
                  <.option(^.value := SocioProfessionalCategory.Farmers.shortName)(
                    I18n.t(s"authenticate.inputs.csp.values.${SocioProfessionalCategory.Farmers.shortName}")
                  ),
                  <.option(^.value := SocioProfessionalCategory.ArtisansMerchantsCompanyDirector.shortName)(
                    I18n.t(
                      s"authenticate.inputs.csp.values.${SocioProfessionalCategory.ArtisansMerchantsCompanyDirector.shortName}"
                    )
                  ),
                  <.option(^.value := SocioProfessionalCategory.ManagersAndHigherIntellectualOccupations.shortName)(
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
                    I18n.t(s"authenticate.inputs.csp.values.${SocioProfessionalCategory.HighSchoolStudent.shortName}")
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
                ),
              ),
              if (self.state.errors.getOrElse("socioProfessionalCategory", "").nonEmpty) {
                <.p(^.className := InputStyles.errorMessage)(
                  unescape(self.state.errors.getOrElse("socioProfessionalCategory", ""))
                )
              }
            )
          },
          if (self.state.errors.getOrElse("global", "").nonEmpty) {
            <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("global", "")))
          },
          if (self.props.wrapped.note != "") {
            <.p(
              ^.className := js.Array(RegisterWithEmailExpandedStyles.note, TextStyles.smallerText),
              ^.dangerouslySetInnerHTML := self.props.wrapped.note
            )()
          },
          if (partnerOptIn.nonEmpty) {
            getPartnerOptInCheckbox(partnerOptIn.get)
          },
          <.div(^.className := RegisterWithEmailExpandedStyles.submitButtonWrapper)(
            <.button(
              ^.className := js.Array(
                CTAStyles.basicOnButton,
                CTAStyles.basic,
                RegisterWithEmailExpandedStyles.disableSubmit(self.state.disableSubmit)
              ),
              ^.`type` := "submit"
            )(
              <.i(^.className := js.Array(FontAwesomeStyles.thumbsUp))(),
              unescape("&nbsp;" + I18n.t("authenticate.register.send-cta"))
            )
          ),
          <.style()(RegisterWithEmailExpandedStyles.render[String])
        )
      }
    )
}

object RegisterWithEmailExpandedStyles extends StyleSheet.Inline {

  import dsl._

  val emailInputWithIconWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), &.before(content := "'\\f003'"))

  val newPasswordInputComponentWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val firstNameInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      &.before(content := "'\\f007'")
    )

  val ageInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      &.before(content := "'\\f1ae'")
    )

  val postalCodeInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      &.before(content := "'\\f041'")
    )

  val genderInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      &.before(content := "'\\f228'")
    )

  val selectInput: StyleA =
    style(
      backgroundColor.transparent,
      width(100.%%),
      height(ThemeStyles.SpacingValue.largerMedium.pxToEm()),
      ThemeStyles.Font.circularStdBook,
      border.none,
      fontSize(1.em)
    )

  val firstOption: Boolean => StyleA = styleF.bool(
    first =>
      if (first) {
        styleS(color(ThemeStyles.TextColor.grey))
      } else {
        styleS()
    }
  )

  val professionInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      &.before(content := "'\\f0f2'")
    )

  val note: StyleA =
    style(
      margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      color(ThemeStyles.TextColor.lighter),
      unsafeChild("a")(textDecoration := "underline", color(ThemeStyles.TextColor.lighter))
    )

  val submitButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

  val customCheckboxLabel: StyleA =
    style(display.flex, &.hover(cursor.pointer), marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val customCheckboxIconWrapper: StyleA =
    style(
      position.relative,
      minWidth(14.pxToEm()),
      height(14.pxToEm()),
      marginRight(6.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      border(1.pxToEm(), solid, ThemeStyles.BorderColor.lighter)
    )

  val customCheckedIcon: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      display.block,
      fontSize(16.pxToEm()),
      marginTop(-10.pxToEm()),
      marginLeft(-6.pxToEm()),
      color(ThemeStyles.ThemeColor.primary)
    )

  val label: StyleA =
    style(TextStyles.smallerText, color(ThemeStyles.TextColor.lighter))

  val disableSubmit: Boolean => StyleA = styleF.bool(
    disable =>
      if (disable) {
        styleS(backgroundColor(ThemeStyles.TextColor.lighter))
      } else
        styleS()
  )
}
