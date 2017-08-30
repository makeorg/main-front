package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.front.components.AppComponentStyles
import org.make.front.facades.{Configuration, I18n}
import org.make.front.facades.ReactModal._
import org.make.front.styles.{FontAwesomeStyles, MakeStyles}
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.helpers.Validation
import org.make.services.user.UserServiceComponent
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object PasswordRecoveryComponent extends UserServiceComponent {

  case class PasswordRecoveryProps(
    modalIsOpen: Boolean,
    closeModal: () => Unit,
    handleReturOnClick: () => Unit,
    handleSubmit: (Self[PasswordRecoveryProps, PasswordRecoveryState]) => Unit
  )

  case class PasswordRecoveryState(email: String, modalIsOpen: Boolean, errorMessage: String)

  lazy val reactClass: ReactClass =
    React.createClass[PasswordRecoveryProps, PasswordRecoveryState](
      getInitialState = { self =>
        PasswordRecoveryState(email = "", modalIsOpen = self.props.wrapped.modalIsOpen, errorMessage = "")
      },
      render = self => {
        def inputStyles: Seq[StyleA] = {
          if (self.state.errorMessage == "") {
            Seq(MakeStyles.Form.inputText)
          } else {
            Seq(MakeStyles.Form.inputText, AppComponentStyles.errorInput)
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
                <.Translate(^.className := MakeStyles.Modal.title, ^.value := {"form.passwordRecovery.title"})(),
                <.Translate(^.className := PasswordRecoveryComponentStyles.terms, ^.value := {"form.passwordRecovery.description"})(),
                <.form(^.onSubmit := handleSubmit(self))(
                  <.div(^.className := MakeStyles.Form.field)(
                    <.i(^.className := Seq(FontAwesomeStyles.envelopeTransparent, MakeStyles.Form.inputIcon))(),
                    <.input(
                      ^.`type`.email,
                      ^.className := inputStyles,
                      ^.placeholder := I18n.t("form.fieldLabelEmail"),
                      ^.onChange := handleEmailChange(self),
                      ^.value := self.state.email
                    )()
                  ),
                  <.div()(<.span(^.className := AppComponentStyles.errorMessage)(self.state.errorMessage)),
                  <.button(^.className := Seq(MakeStyles.Button.default, AppComponentStyles.submitButton), ^.onClick := handleSubmit(self))(
                    <.i(^.className := Seq(FontAwesomeStyles.paperPlaneTransparent, AppComponentStyles.buttonIcon))(),
                    <.Translate(^.value := "form.passwordRecovery.receiveEmail")()
                  ),
                  <.div()(
                    <.Translate(^.value := "form.passwordRecovery.return")(),
                    <.a(^.className := AppComponentStyles.link, ^.onClick := handleReturOnClick(self))(
                      I18n.t("form.passwordRecovery.connectScreen")
                    )
                  )
                )
              )
            )
          )
        )
      }
    )
  private def handleEmailChange(self: Self[PasswordRecoveryProps, PasswordRecoveryState]) = (e: FormSyntheticEvent[HTMLInputElement]) => {
    val newEmail = e.target.value
    self.setState(_.copy(email = newEmail))
  }

  private def handleSubmit(self: Self[PasswordRecoveryProps, PasswordRecoveryState]) = (e: SyntheticEvent)  => {
    e.preventDefault()
    if (Validation.isValidEmail(self.state.email)) {
      self.setState(self.state.copy(errorMessage = ""))
      self.props.wrapped.handleSubmit(self)
    } else {
      self.setState(self.state.copy(errorMessage = I18n.t("form.passwordRecovery.invalidEmail")))
    }
  }

  private def closeModal(self: Self[PasswordRecoveryProps, PasswordRecoveryState]) = () => {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.closeModal()
  }

  private def handleReturOnClick(self: Self[PasswordRecoveryProps, PasswordRecoveryState]) = () => {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.handleReturOnClick()
  }
}

object PasswordRecoveryComponentStyles extends StyleSheet.Inline {
  import dsl._

  val terms: StyleA = style(marginBottom(0.8F.rem), fontSize(1.4.rem), textAlign.left)

}
