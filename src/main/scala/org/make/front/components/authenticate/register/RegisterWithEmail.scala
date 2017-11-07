package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.client.BadRequestHttpException
import org.make.core.validation.{Constraint, EmailConstraint, NotBlankConstraint, PasswordConstraint}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.NewPasswordInput.NewPasswordInputProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.make.front.styles.utils._
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object RegisterWithEmail {

  val reactClass: ReactClass =
    React.createClass[RegisterProps, RegisterState](displayName = "RegisterWithEmail", getInitialState = { _ =>
      RegisterState(Map(), Map())
    }, render = { self =>
      def updateField(name: String): (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
        val inputValue = event.target.value
        self.setState(
          state => state.copy(fields = state.fields + (name -> inputValue), errors = state.errors + (name -> ""))
        )
      }

      val fieldsValidation: Seq[(String, Constraint, Map[String, String])] = {
        Seq(
          (
            "email",
            NotBlankConstraint.&(EmailConstraint),
            Map(
              "invalid" -> I18n.t("authenticate.inputs.email.format-error"),
              "notBlank" -> I18n.t("authenticate.inputs.email.empty-field-error")
            )
          ),
          (
            "password",
            NotBlankConstraint.&(PasswordConstraint),
            Map(
              "notBlank" -> I18n.t("authenticate.inputs.password.empty-field-error"),
              "minMessage" -> I18n
                .t("authenticate.inputs.password.format-error", Replacements("min" -> PasswordConstraint.min.toString))
            )
          ),
          (
            "firstName",
            NotBlankConstraint,
            Map("notBlank" -> I18n.t("authenticate.inputs.first-name.empty-field-error"))
          )
        )
      }

      def onSubmit: (FormSyntheticEvent[HTMLInputElement]) => Unit = {
        event =>
          event.preventDefault()

          var errors: Map[String, String] = Map.empty

          fieldsValidation.foreach {
            case (fieldName, constraint, translation) => {
              val fieldErrors = constraint
                .validate(self.state.fields.get(fieldName), translation)
                .map(_.message)
              if (fieldErrors.nonEmpty) {
                errors += (fieldName -> fieldErrors.head)
              }
            }
          }

          if (errors.nonEmpty) {
            self.setState(_.copy(errors = errors))
          } else {
            self.props.wrapped.register(self.state).onComplete {
              case Success(_) => self.setState(RegisterState.empty)
              case Failure(e) =>
                e match {
                  case exception: BadRequestHttpException =>
                    val errors = getErrorsMessagesFromApiErrors(exception.errors).toMap
                    self.setState(_.copy(errors = errors))
                  case _ =>
                    self.setState(
                      state =>
                        state
                          .copy(errors = state.errors + ("global" -> I18n.t("authenticate.failure")))
                    )
                }
            }
          }
      }

      <.form(^.onSubmit := onSubmit, ^.novalidate := true)(
        <.label(
          ^.className := Seq(
            InputStyles.wrapper,
            InputStyles.withIcon,
            RegisterWithEmailStyles.emailInputWithIconWrapper
          )
        )(
          <.input(
            ^.`type`.email,
            ^.required := true,
            ^.placeholder := s"${I18n.t("authenticate.inputs.email.placeholder")} ${I18n.t("authenticate.inputs.required")}",
            ^.onChange := updateField("email"),
            ^.value := self.state.fields.getOrElse("email", "")
          )()
        ),
        if (self.state.errors.getOrElse("email", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("email", "")))
        },
        <.div(^.className := RegisterWithEmailStyles.newPasswordInputComponentWrapper)(
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
        if (self.state.errors.getOrElse("password", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("password", "")))
        },
        <.label(
          ^.className := Seq(
            InputStyles.wrapper,
            InputStyles.withIcon,
            RegisterWithEmailStyles.firstNameInputWithIconWrapper
          )
        )(
          <.input(
            ^.`type`.text,
            ^.required := true,
            ^.className := Seq(InputStyles.withIcon),
            ^.placeholder := s"${I18n.t("authenticate.inputs.first-name.placeholder")} ${I18n.t("authenticate.inputs.required")}",
            ^.onChange := updateField("firstName"),
            ^.value := self.state.fields.getOrElse("firstName", "")
          )()
        ),
        if (self.state.errors.getOrElse("firstName", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("firstName", "")))
        },
        if (self.state.errors.getOrElse("global", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(unescape(self.state.errors.getOrElse("firstName", "")))
        },
        if (self.props.wrapped.note != "") {
          <.p(
            ^.className := Seq(RegisterWithEmailStyles.note, TextStyles.smallerText),
            ^.dangerouslySetInnerHTML := self.props.wrapped.note
          )()
        },
        <.div(^.className := RegisterWithEmailStyles.submitButtonWrapper)(
          <.button(^.className := Seq(CTAStyles.basicOnButton, CTAStyles.basic), ^.`type` := "submit")(
            I18n.t("authenticate.register.send-cta")
          )
        ),
        <.style()(RegisterWithEmailStyles.render[String])
      )
    })
}

object RegisterWithEmailStyles extends StyleSheet.Inline {
  import dsl._

  val emailInputWithIconWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), (&.before)(content := "'\\f003'"))

  val newPasswordInputComponentWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val firstNameInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      (&.before)(content := "'\\f007'")
    )

  val note: StyleA =
    style(
      margin :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      color(ThemeStyles.TextColor.lighter),
      unsafeChild("a")(textDecoration := "underline", color(ThemeStyles.TextColor.lighter))
    )

  val submitButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)
}
