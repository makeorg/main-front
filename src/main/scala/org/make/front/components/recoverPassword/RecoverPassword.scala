package org.make.front.components.recoverPassword

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.core.validation.{ConstraintError, EmailConstraint}
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.styles.{FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.ExecutionContext.Implicits.global

object PasswordRecovery {

  case class PasswordRecoveryProps(handleSubmit: (String) => Future[_])

  case class PasswordRecoveryState(email: String, errorMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[PasswordRecoveryProps, PasswordRecoveryState](getInitialState = { _ =>
      PasswordRecoveryState(email = "", errorMessage = "")
    }, render = {
      self =>
        val inputStyles: StyleA = {
          if (self.state.errorMessage == "") {
            PasswordRecoveryStyles.inputText
          } else {
            PasswordRecoveryStyles.errorInput
          }
        }
        val handleEmailChange =
          (e: FormSyntheticEvent[HTMLInputElement]) => {
            val newEmail = e.target.value
            self.setState(_.copy(email = newEmail))
          }

        val handleSubmit = (e: SyntheticEvent) => {
          e.preventDefault()
          val errors: Seq[ConstraintError] =
            EmailConstraint.validate(Some(self.state.email), Map("invalid" -> "form.passwordRecovery.invalidEmail"))

          if (errors.isEmpty) {
            self.setState(self.state.copy(errorMessage = ""))
            self.props.wrapped.handleSubmit(self.state.email).onComplete {
              case Success(_) =>
              case Failure(_) =>
                self.setState(self.state.copy(errorMessage = I18n.t("form.passwordRecovery.emailDoesNotExist")))
            }
          } else {
            self.setState(self.state.copy(errorMessage = I18n.t(errors.head.message)))
          }
        }

        <.div()(
          <.div(^.className := MakeStyles.Modal.content)(
            <.div()(
              <.Translate(^.className := MakeStyles.Modal.title, ^.value := { "form.passwordRecovery.title" })(),
              <.Translate(^.className := PasswordRecoveryStyles.terms, ^.value := {
                "form.passwordRecovery.description"
              })(),
              <.form(^.onSubmit := handleSubmit, ^.novalidate := true)(
                <.div(^.className := MakeStyles.Form.field)(
                  <.i(^.className := Seq(FontAwesomeStyles.envelopeTransparent, MakeStyles.Form.inputIcon))(),
                  <.input(
                    ^.`type`.email,
                    ^.className := inputStyles,
                    ^.placeholder := I18n.t("form.passwordRecovery.fieldLabelEmail"),
                    ^.onChange := handleEmailChange,
                    ^.value := self.state.email
                  )()
                ),
                <.div()(<.span(^.className := PasswordRecoveryStyles.errorMessage)(self.state.errorMessage)),
                <.button(
                  ^.className := Seq(MakeStyles.Button.default, PasswordRecoveryStyles.submitButton),
                  ^.onClick := handleSubmit
                )(
                  <.i(^.className := Seq(FontAwesomeStyles.paperPlaneTransparent, PasswordRecoveryStyles.buttonIcon))(),
                  <.Translate(^.value := "form.passwordRecovery.sendEmail")()
                )
              )
            )
          ),
          <.style()(PasswordRecoveryStyles.render[String])
        )
    })
}

object PasswordRecoveryStyles extends StyleSheet.Inline {
  import dsl._

  val terms: StyleA = style(marginBottom(0.8F.rem), fontSize(1.4.rem), textAlign.left)

  val link: StyleA = style(color(MakeStyles.Color.pink), fontWeight.bold)

  val buttonIcon: StyleA = style(paddingBottom(0.5F.rem), paddingRight(0.9.rem))

  val submitButton: StyleA = style(marginBottom(1.7F.rem))

  val inputText: StyleA = style(MakeStyles.Form.inputText)

  val errorInput: StyleA = style(MakeStyles.Form.errorInput, addClassName(MakeStyles.Form.inputText.htmlClass))

  val errorMessage: StyleA = style(MakeStyles.Form.errorMessage)
}
