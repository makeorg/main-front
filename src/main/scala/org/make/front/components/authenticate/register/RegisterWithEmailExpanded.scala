package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.client.{BadRequestHttpException, ValidationError}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.NewPasswordInput.NewPasswordInputProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.{CTAStyles, InputStyles}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scalacss.DevDefaults.StyleA

object RegisterWithEmailExpanded {

  val reactClass: ReactClass =
    React.createClass[RegisterProps, RegisterState](displayName = "RegisterWithEmailExpanded", getInitialState = { _ =>
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
                case exception: BadRequestHttpException =>
                  val errors = exception.errors.map {
                    case ValidationError("email", Some(message)) if message.contains("already exist") =>
                      "email" -> unescape(I18n.t("form.register.errorAlreadyExist"))
                    case ValidationError("email", Some(message)) if message.contains("required") =>
                      "email" -> unescape(I18n.t("form.register.errorBlankEmail"))
                    case ValidationError("email", _) =>
                      "email" -> unescape(I18n.t("form.register.errorInvalidEmail"))
                    case ValidationError("password", Some(message)) if message.contains("required") =>
                      "password" -> unescape(I18n.t("form.register.errorBlankPassword"))
                    case ValidationError("password", _) =>
                      "password" -> unescape(I18n.t("form.register.errorMinPassword", Replacements("min" -> "8")))
                    case ValidationError("firstName", Some(message)) if message.contains("required") =>
                      "firstName" -> unescape(I18n.t("form.register.errorBlankFirstName"))
                    case ValidationError("age", _) =>
                      "age" -> unescape(I18n.t("form.register.errorChoiceAge"))
                    case ValidationError("postalCode", _) =>
                      "postalCode" -> unescape(I18n.t("form.register.errorMaxPostalCode"))
                    case ValidationError("profession", _) =>
                      "profession" -> unescape(I18n.t("form.register.errorProfession"))
                    case ValidationError(_, _) =>
                      "global" -> unescape(I18n.t("form.register.errorRegistrationFailed"))
                  }.toMap
                  self.setState(_.copy(errors = errors))

                case _ =>
                  self.setState(
                    state =>
                      state.copy(errors = state.errors + ("global" -> I18n.t("form.register.errorRegistrationFailed")))
                  )
              }

          }
          false
      }

      val agesChoices: Seq[Int] = Range(13, 100)

      <.form(^.onSubmit := onSubmit)(
        <.label(
          ^.className := Seq(
            InputStyles.wrapper,
            InputStyles.withIcon,
            RegisterWithEmailExpandedStyles.emailInputWithIconWrapper
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
        if (self.state.errors.getOrElse("email", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("email", ""))
        },
        <.div(^.className := RegisterWithEmailExpandedStyles.newPasswordInputComponentWrapper)(
          <.NewPasswordInputComponent(
            ^.wrapped := NewPasswordInputProps(
              value = self.state.fields.getOrElse("password", ""),
              required = true,
              placeHolder = s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
              onChange = updateField("password")
            )
          )()
        ),
        if (self.state.errors.getOrElse("password", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("password", ""))
        },
        <.label(
          ^.className := Seq(
            InputStyles.wrapper,
            InputStyles.withIcon,
            RegisterWithEmailExpandedStyles.firstNameInputWithIconWrapper
          )
        )(
          <.input(
            ^.`type`.text,
            ^.required := true,
            ^.placeholder := s"${I18n.t("form.fieldLabelFirstName")} ${I18n.t("form.required")}",
            ^.onChange := updateField("firstName"),
            ^.value := self.state.fields.getOrElse("firstName", "")
          )()
        ),
        if (self.state.errors.getOrElse("firstName", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("firstName", ""))
        },
        <.label(
          ^.className := Seq(
            InputStyles.wrapper,
            InputStyles.withIcon,
            RegisterWithEmailExpandedStyles.ageInputWithIconWrapper
          )
        )(
          <.select(
            ^.`type`.text,
            ^.required := false,
            ^.placeholder := s"${I18n.t("form.fieldLabelAge")}",
            ^.onChange := updateField("age"),
            ^.value := self.state.fields.getOrElse("age", "")
          )(
            <.option(^.value := "")(s"${I18n.t("form.fieldLabelAge")}"),
            agesChoices.map(age => <.option(^.value := age)(age))
          )
        ),
        if (self.state.errors.getOrElse("age", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("age", ""))
        },
        <.label(
          ^.className := Seq(
            InputStyles.wrapper,
            InputStyles.withIcon,
            RegisterWithEmailExpandedStyles.postalCodeInputWithIconWrapper
          )
        )(
          <.input(
            ^.`type`.text,
            ^.required := false,
            ^.placeholder := s"${I18n.t("form.fieldPostalCode")}",
            ^.onChange := updateField("postalCode"),
            ^.value := self.state.fields.getOrElse("postalCode", "")
          )()
        ),
        if (self.state.errors.getOrElse("postalCode", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("postalCode", ""))
        },
        <.label(
          ^.className := Seq(
            InputStyles.wrapper,
            InputStyles.withIcon,
            RegisterWithEmailExpandedStyles.professionInputWithIconWrapper
          )
        )(
          <.input(
            ^.`type`.text,
            ^.required := false,
            ^.placeholder := s"${I18n.t("form.fieldProfession")}",
            ^.onChange := updateField("profession"),
            ^.value := self.state.fields.getOrElse("profession", "")
          )()
        ),
        if (self.state.errors.getOrElse("profession", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("profession", ""))
        },
        if (self.state.errors.getOrElse("global", "") != "") {
          <.p(^.className := InputStyles.errorMessage)(self.state.errors.getOrElse("global", ""))
        },
        if (self.props.wrapped.note != "") {
          <.p(^.className := Seq(RegisterWithEmailExpandedStyles.note, TextStyles.smallerText))(self.props.wrapped.note)
        },
        <.div(^.className := RegisterWithEmailExpandedStyles.submitButtonWrapper)(
          <.button(^.className := Seq(CTAStyles.basicOnButton, CTAStyles.basic), ^.`type` := "submit")(
            I18n.t("form.register.subscribe")
          )
        ),
        <.style()(RegisterWithEmailExpandedStyles.render[String])
      )

    })
}

object RegisterWithEmailExpandedStyles extends StyleSheet.Inline {
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

  val ageInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      (&.before)(content := "'\\f1ae'")
    )

  val postalCodeInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      (&.before)(content := "'\\f041'")
    )

  val professionInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      (&.before)(content := "'\\f0f2'")
    )

  val note: StyleA =
    style(
      margin :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      color(ThemeStyles.TextColor.lighter),
      unsafeChild("a")(color(ThemeStyles.ThemeColor.primary))
    )

  val submitButtonWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

}
