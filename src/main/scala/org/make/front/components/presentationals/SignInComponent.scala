package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.front.facades.I18n
import org.make.front.facades.ReactFacebookLogin._
import org.make.front.facades.ReactGoogleLogin._
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.libs.ReactModal._
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.experimental.Response
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object SignInComponent {
  case class SignInProps(isConnected: Boolean,
                         signInGoogle: (Response, Self[SignInProps, SignInState])   => Unit,
                         signInFacebook: (Response, Self[SignInProps, SignInState]) => Unit,
                         signIn: (String, String, Self[SignInProps, SignInState])   => Unit,
                         closeModal: ()                                             => Unit,
                         isOpen: Boolean,
                         googleAppId: String,
                         facebookAppId: String)
  case class SignInState(isOpen: Boolean, username: String = "", password: String = "", errorMessage: String = "")

  lazy val reactClass: ReactClass =
    React.createClass[SignInProps, SignInState](
      getInitialState = { self =>
        SignInState(isOpen = self.props.wrapped.isOpen)
      },
      componentWillReceiveProps = (self, props) => {
        self.setState(self.state.copy(isOpen = props.wrapped.isOpen))
      },
      render = self =>
        <.div()(
          <.ReactModal(
            ^.contentLabel := "User Account Journey",
            ^.isOpen := self.state.isOpen,
            ^.overlayClassName := MakeStyles.Modal.overlay,
            ^.className := MakeStyles.Modal.modal,
            ^.onRequestClose := closeModal(self)
          )(
            <.a(^.onClick := closeModal(self), ^.className := MakeStyles.Modal.close)(I18n.t("form.login.close")),
            <.div(^.className := MakeStyles.Modal.content)(
              <.div(^.className := SignInComponentStyles.buttons)(
                <.Translate(^.className := MakeStyles.Modal.title, ^.value := "form.login.socialConnect")(),
                <.div(^.className := Seq(BulmaStyles.Helpers.isClearfix, SignInComponentStyles.buttonsWrapper))(
                  <.ReactFacebookLogin(
                    ^.appId := self.props.wrapped.facebookAppId,
                    ^.scope := "public_profile, email",
                    ^.fields := "first_name, last_name, email, name, picture",
                    ^.callback := facebookCallbackResponse(self),
                    ^.cssClass := Seq(
                      MakeStyles.Button.facebook,
                      SignInComponentStyles.button,
                      BulmaStyles.Helpers.isPulledLeft
                    ),
                    ^.iconClass := SignInComponentStyles.buttonIcon.htmlClass,
                    ^.textButton := "facebook"
                  )(),
                  <.ReactGoogleLogin(
                    ^.clientID := self.props.wrapped.googleAppId,
                    ^.scope := "profile email",
                    ^.onSuccess := googleCallbackResponse(self),
                    ^.onFailure := googleCallbackFailure(self),
                    ^.isSignIn := self.props.wrapped.isConnected,
                    ^.className := Seq(
                      MakeStyles.Button.google,
                      SignInComponentStyles.button,
                      BulmaStyles.Helpers.isPulledRight
                    )
                  )(<.i(^.className := SignInComponentStyles.buttonIcon.htmlClass)(), "google")
                )
              ),
              <.div(^.className := SignInComponentStyles.lineWrapper)(
                <.span(^.className := SignInComponentStyles.line)(),
                <.Translate(^.className := SignInComponentStyles.underlineText, ^.value := "form.login.or")(),
                <.span(^.className := SignInComponentStyles.line)()
              ),
              <.div()(
                <.Translate(^.className := MakeStyles.Modal.title, ^.value := "form.login.stdConnect")(),
                <.form(^.onSubmit := handleSubmit(self) _)(
                  <.div(^.className := MakeStyles.Form.field)(
                    <.i(^.className := Seq(FontAwesomeStyles.envelopeTransparent, MakeStyles.Form.inputIcon))(),
                    <.input(
                      ^.`type`.email,
                      ^.className := Seq(MakeStyles.Form.inputText, SignInComponentStyles.input),
                      ^.placeholder := I18n.t("form.login.fieldLabelEmail"),
                      ^.onChange := handleUsernameChange(self) _,
                      ^.value := self.state.username
                    )()
                  ),
                  <.div(^.className := MakeStyles.Form.field)(
                    <.i(^.className := Seq(FontAwesomeStyles.lock, MakeStyles.Form.inputIcon))(),
                    <.input(
                      ^.`type`.password,
                      ^.className := Seq(MakeStyles.Form.inputText, SignInComponentStyles.input),
                      ^.placeholder := I18n.t("form.login.fieldLabelPassword"),
                      ^.onChange := handlePasswordChange(self) _,
                      ^.value := self.state.password
                    )()
                  ),
                  <.div()(<.span(^.className := SignInComponentStyles.error)(self.state.errorMessage)),
                  <.div()(
                    <.button(^.className := Seq(MakeStyles.Button.default, SignInComponentStyles.submitButton))(
                      <.i(
                        ^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, SignInComponentStyles.buttonIcon)
                      )(),
                      <.Translate(^.value := "form.login.submitButton")()
                    ),
                    <.div(^.className := SignInComponentStyles.text)(
                      <.Translate(^.value := "form.login.oupsI")(),
                      <.a(^.className := SignInComponentStyles.link)(I18n.t("form.login.forgotPassword"))
                    ),
                    <.div()(
                      <.Translate(^.value := "form.login.anyAccount")(),
                      <.a(^.className := SignInComponentStyles.link)(I18n.t("form.login.createAccount"))
                    )
                  )
                )
              )
            ),
            <.style()(SignInComponentStyles.render[String])
          )
      )
    )

  private def handleUsernameChange(self: Self[SignInProps, SignInState])(e: FormSyntheticEvent[HTMLInputElement]) = {
    val newUsername = e.target.value
    self.setState(_.copy(username = newUsername))
  }

  private def handlePasswordChange(self: Self[SignInProps, SignInState])(e: FormSyntheticEvent[HTMLInputElement]) = {
    val newPassword = e.target.value
    self.setState(_.copy(password = newPassword))
  }

  private def handleSubmit(self: Self[SignInProps, SignInState])(e: SyntheticEvent): Unit = {
    self.setState(self.state.copy(errorMessage = ""))
    e.preventDefault()
    self.props.wrapped.signIn(self.state.username, self.state.password, self)

  }

  def open(self: Self[SignInProps, SignInState])(): Unit =
    self.setState(self.state.copy(isOpen = true))

  def close(self: Self[SignInProps, SignInState])(): Unit =
    self.setState(self.state.copy(isOpen = false))

  private def closeModal(self: Self[SignInProps, SignInState]) = () => {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.closeModal()
  }

  def facebookCallbackResponse(self: Self[SignInProps, SignInState])(response: Response): Unit = {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.signInFacebook(response, self)
  }

  def googleCallbackResponse(self: Self[SignInProps, SignInState])(response: Response): Unit = {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.signInGoogle(response, self)
  }

  def googleCallbackFailure(self: Self[SignInProps, SignInState])(response: Response): Unit = {
    self.setState(self.state.copy(errorMessage = I18n.t("form.login.errorAuthenticationFailed")))
  }
}

object SignInComponentStyles extends StyleSheet.Inline {
  import dsl._

  val buttons: StyleA = style()
  val buttonsWrapper: StyleA = style(margin.auto)
  val button: StyleA = style(width(48.8F.%%))
  val buttonIcon: StyleA =
    style(paddingBottom(0.5F.rem), paddingRight(0.9.rem), addClassName(FontAwesomeStyles.facebook.htmlClass))
  val buttonsInfo: StyleA = style(marginTop(1.4F.rem), display.block)

  val link: StyleA = style(color(MakeStyles.Color.pink))

  val lineWrapper: StyleA =
    style(display.flex, alignItems.center, margin(3.4F.rem, auto, 2.9F.rem, auto))
  val line: StyleA = style(height(0.1F.rem), backgroundColor(rgba(0, 0, 0, 0.3)), flexGrow(1), marginTop(0.5F.rem))
  val underlineText: StyleA = style(MakeStyles.Font.playfairDisplayItalic, margin(0.rem, 1.6F.rem))
  val input: StyleA =
    style(height(4.rem), width(100.%%), (media.all.maxWidth(800.px))(height(3.rem)))
  val submitButton: StyleA = style(marginBottom(1.7F.rem))
  val text: StyleA = style(marginBottom(0.8F.rem))
  val error: StyleA = style(
    display.block,
    margin.auto,
    MakeStyles.Font.circularStdBook,
    fontSize(1.4F.rem),
    color(MakeStyles.Color.error),
    lineHeight(1.8F.rem),
    paddingBottom(1.rem)
  )
}
