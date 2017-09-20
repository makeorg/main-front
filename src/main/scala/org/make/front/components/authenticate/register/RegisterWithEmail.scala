package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.make.client.BadRequestHttpException
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.ViewablePassword.ViewablePasswordProps
import org.make.front.facades.I18n
import org.make.front.styles.{CTAStyles, InputStyles}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scalacss.DevDefaults.StyleA
import scalacss.internal.mutable.StyleSheet.Inline

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
        <.p()(self.props.wrapped.intro),
        <.label(^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, RegisterWithEmailStyles.emailPicto))(
          <.input(
            ^.`type`.email,
            ^.required := true,
            ^.placeholder := s"${I18n.t("form.fieldLabelEmail")} ${I18n.t("form.required")}",
            ^.onChange := updateField("email"),
            ^.value := self.state.fields.getOrElse("email", "")
          )()
        ),
        <.span()(self.state.errors.getOrElse("email", "")),
        <.label(^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, RegisterWithEmailStyles.passwordPicto))(
          <.ViewablePasswordComponent(
            ^.wrapped := ViewablePasswordProps(
              value = self.state.fields.getOrElse("password", ""),
              required = true,
              placeHolder = s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
              onChange = updateField("password")
            )
          )()
        ),
        <.span()(self.state.errors.getOrElse("password", "")),
        <.label(^.className := Seq(InputStyles.wrapper, InputStyles.withIcon, RegisterWithEmailStyles.firstNamePicto))(
          <.input(
            ^.`type`.text,
            ^.required := true,
            ^.className := Seq(RegisterWithEmailStyles.firstNamePicto, InputStyles.withIcon),
            ^.placeholder := s"${I18n.t("form.fieldLabelFirstName")} ${I18n.t("form.required")}",
            ^.onChange := updateField("firstName"),
            ^.value := self.state.fields.getOrElse("firstName", "")
          )()
        ),
        <.span()(self.state.errors.getOrElse("firstName", "")),
        <.p()(self.props.wrapped.note),
        <.button(^.`type` := "submit", ^.className := Seq(CTAStyles.basicOnButton, CTAStyles.basic))(
          I18n.t("form.register.subscribe")
        ),
        <.style()(RegisterWithEmailStyles.render[String])
      )

    })

  object RegisterWithEmailStyles extends Inline {
    import dsl._

    val emailPicto: StyleA = style((&.before)(content := "'\\f003'"))
    val passwordPicto: StyleA = style((&.before)(content := "'\\f023'"))
    val firstNamePicto: StyleA = style((&.before)(content := "'\\f007'"))
  }

}
