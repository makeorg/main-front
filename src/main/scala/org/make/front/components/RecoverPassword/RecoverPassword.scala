package org.make.front.components.RecoverPassword

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.core.validation.{ConstraintError, EmailConstraint}
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.facades.ReactModal._
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.styles.{FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object PasswordRecoveryComponent {

  case class PasswordRecoveryProps(modalIsOpen: Boolean,
                                   closeModal: ()                                                     => Unit,
                                   handleReturnLinkClick: ()                                          => Unit,
                                   handleSubmit: (Self[PasswordRecoveryProps, PasswordRecoveryState]) => Unit)

  case class PasswordRecoveryState(email: String, modalIsOpen: Boolean, errorMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[PasswordRecoveryProps, PasswordRecoveryState](
      getInitialState = { self =>
        PasswordRecoveryState(email = "", modalIsOpen = self.props.wrapped.modalIsOpen, errorMessage = "")
      },
      render = self => {
        def inputStyles: StyleA = {
          if (self.state.errorMessage == "") {
            PasswordRecoveryStyles.inputText
          } else {
            PasswordRecoveryStyles.errorInput
          }
        }

        <.div()(
          <.ReactModal(
            ^.contentLabel := "Password Recovery",
            ^.isOpen := self.props.wrapped.modalIsOpen,
            ^.overlayClassName := MakeStyles.Modal.overlay,
            ^.className := MakeStyles.Modal.modal
          )(
            <.a(^.onClick := closeModal(self), ^.className := MakeStyles.Modal.close)(I18n.t("modal.close")),
            <.div(^.className := MakeStyles.Modal.content)(
              <.div()(
                <.Translate(^.className := MakeStyles.Modal.title, ^.value := { "form.passwordRecovery.title" })(),
                <.Translate(^.className := PasswordRecoveryStyles.terms, ^.value := {
                  "form.passwordRecovery.description"
                })(),
                <.form(^.onSubmit := handleSubmit(self), ^.novalidate := true)(
                  <.div(^.className := MakeStyles.Form.field)(
                    <.i(^.className := Seq(FontAwesomeStyles.envelopeTransparent, MakeStyles.Form.inputIcon))(),
                    <.input(
                      ^.`type`.email,
                      ^.className := inputStyles,
                      ^.placeholder := I18n.t("form.passwordRecovery.fieldLabelEmail"),
                      ^.onChange := handleEmailChange(self),
                      ^.value := self.state.email
                    )()
                  ),
                  <.div()(<.span(^.className := PasswordRecoveryStyles.errorMessage)(self.state.errorMessage)),
                  <.button(
                    ^.className := Seq(MakeStyles.Button.default, PasswordRecoveryStyles.submitButton),
                    ^.onClick := handleSubmit(self)
                  )(
                    <.i(
                      ^.className := Seq(FontAwesomeStyles.paperPlaneTransparent, PasswordRecoveryStyles.buttonIcon)
                    )(),
                    <.Translate(^.value := "form.passwordRecovery.sendEmail")()
                  ),
                  <.div()(
                    <.Translate(^.value := "form.passwordRecovery.return")(),
                    <.a(^.className := PasswordRecoveryStyles.link, ^.onClick := handleReturnLinkClick(self))(
                      I18n.t("form.passwordRecovery.connectScreen")
                    )
                  )
                )
              )
            )
          ),
          <.style()(PasswordRecoveryStyles.render[String])
        )
      }
    )
  private def handleEmailChange(self: Self[PasswordRecoveryProps, PasswordRecoveryState]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newEmail = e.target.value
      self.setState(_.copy(email = newEmail))
    }

  private def handleSubmit(self: Self[PasswordRecoveryProps, PasswordRecoveryState]) = (e: SyntheticEvent) => {
    e.preventDefault()
    val errors: Seq[ConstraintError] =
      EmailConstraint.validate(Some(self.state.email), Map("invalid" -> "form.passwordRecovery.invalidEmail"))

    if (errors.isEmpty) {
      self.setState(self.state.copy(errorMessage = ""))
      self.props.wrapped.handleSubmit(self)
    } else {
      self.setState(self.state.copy(errorMessage = I18n.t(errors.head.message)))
    }
  }

  private def closeModal(self: Self[PasswordRecoveryProps, PasswordRecoveryState]) = () => {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.closeModal()
  }

  private def handleReturnLinkClick(self: Self[PasswordRecoveryProps, PasswordRecoveryState]) = () => {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.handleReturnLinkClick()
  }
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
