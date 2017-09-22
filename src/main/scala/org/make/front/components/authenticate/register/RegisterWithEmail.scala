package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.client.BadRequestHttpException
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.NewPasswordInput.NewPasswordInputProps
import org.make.front.facades.I18n
import org.make.front.styles.{CTAStyles, InputStyles, TextStyles, ThemeStyles}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scalacss.DevDefaults.StyleA
import scalacss.internal.Length

object RegisterWithEmail {

  val reactClass: ReactClass =
    React.createClass[RegisterProps, RegisterState](getInitialState = { _ =>
      RegisterState(Map(), Map())
    }, render = { self =>
      def updateField(name: String): (FormSyntheticEvent[HTMLInputElement]) => Unit = { event =>
        val inputValue = event.target.value
        self.setState(
          state => state.copy(fields = state.fields + (name -> inputValue), errors = state.errors + (name -> ""))
        )
      }

      def onSubmit: () => Boolean = {
        () =>
          self.props.wrapped.register(self.state).onComplete {
            case Success(_) => self.setState(RegisterState.empty)
            case Failure(e) =>
              e match {
                case exception: BadRequestHttpException if exception.getMessage.contains("already exist") =>
                  self.setState(
                    state => state.copy(errors = state.errors + ("email" -> I18n.t("form.register.errorAlreadyExist")))
                  )
                case _ =>
                  self.setState(
                    state =>
                      state.copy(errors = state.errors + ("email" -> I18n.t("form.register.errorRegistrationFailed")))
                  )
              }
          }
          false
      }

      <.form(^.onSubmit := onSubmit)(
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
            ^.placeholder := s"${I18n.t("form.fieldLabelEmail")} ${I18n.t("form.required")}",
            ^.onChange := updateField("email"),
            ^.value := self.state.fields.getOrElse("email", "")
          )()
        ),
        if (self.state.errors.getOrElse("email", "") == "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("email", ""))
        },
        <.div(^.className := RegisterWithEmailStyles.newPasswordInputComponentWrapper)(
          <.NewPasswordInputComponent(
            ^.wrapped := NewPasswordInputProps(
              value = self.state.fields.getOrElse("password", ""),
              required = true,
              placeHolder = s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
              onChange = updateField("password")
            )
          )()
        ),
        if (self.state.errors.getOrElse("password", "") == "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("password", ""))
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
            ^.placeholder := s"${I18n.t("form.fieldLabelFirstName")} ${I18n.t("form.required")}",
            ^.onChange := updateField("firstName"),
            ^.value := self.state.fields.getOrElse("firstName", "")
          )()
        ),
        if (self.state.errors.getOrElse("firstName", "") == "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("firstName", ""))
        },
        if (self.props.wrapped.note != "") {
          <.p(^.className := Seq(RegisterWithEmailStyles.note, TextStyles.smallerText))(self.props.wrapped.note)
        },
        <.div(^.className := RegisterWithEmailStyles.submitButtonWrapper)(
          <.button(^.className := Seq(CTAStyles.basicOnButton, CTAStyles.basic), ^.`type` := "submit")(
            I18n.t("form.register.subscribe")
          )
        ),
        <.style()(RegisterWithEmailStyles.render[String])
      )

    })

}

object RegisterWithEmailStyles extends StyleSheet.Inline {
  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

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
      margin := s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      color(ThemeStyles.TextColor.lighter),
      unsafeChild("a")(color(ThemeStyles.ThemeColor.primary))
    )

  val submitButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)
}
