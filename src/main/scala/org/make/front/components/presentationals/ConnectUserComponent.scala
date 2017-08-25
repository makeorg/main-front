package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.front.facades.I18n
import org.make.front.facades.ReactFacebookLogin._
import org.make.front.facades.ReactGoogleLogin._
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.facades.ReactModal._
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.experimental.Response
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object ConnectUserComponent {
  case class ConnectUserProps(isConnected: Boolean,
                              signInGoogle: (Response, Self[ConnectUserProps, State])           => Unit,
                              signInFacebook: (Response, Self[ConnectUserProps, State])         => Unit,
                              signIn: (String, String, Self[ConnectUserProps, State])           => Unit,
                              register: (String, String, String, Self[ConnectUserProps, State]) => Unit,
                              closeModal: ()                                                    => Unit,
                              isOpen: Boolean,
                              googleAppId: String,
                              facebookAppId: String,
                              isRegistering: Boolean)

  case class State(isOpen: Boolean,
                   isRegistering: Boolean,
                   username: String = "",
                   password: String = "",
                   firstName: String = "",
                   errorMessage: String = "",
                   typePassword: String = "password")

  lazy val reactClass: ReactClass =
    React.createClass[ConnectUserProps, State](
      getInitialState = { self =>
        State(isOpen = self.props.wrapped.isOpen, isRegistering = self.props.wrapped.isRegistering)
      },
      componentWillReceiveProps = { (self, props) =>
        self.setState(self.state.copy(isOpen = props.wrapped.isOpen, isRegistering = self.state.isRegistering))
      },
      render = self => {
        <.div()(
          <.ReactModal(
            ^.contentLabel := "User Account Journey",
            ^.isOpen := self.state.isOpen,
            ^.overlayClassName := MakeStyles.Modal.overlay,
            ^.className := MakeStyles.Modal.modal,
            ^.onRequestClose := closeModal(self)
          )(
            <.a(^.onClick := closeModal(self) _, ^.className := MakeStyles.Modal.close)(I18n.t("form.login.close")),
            <.div(^.className := MakeStyles.Modal.content)(
              <.div(^.className := ConnectUserComponentStyles.buttons)(
                <.Translate(^.className := MakeStyles.Modal.title, ^.value := {
                  if (self.state.isRegistering) {
                    "form.login.socialRegister"
                  } else {
                    "form.login.socialConnect"
                  }
                })(),
                <.div(^.className := Seq(BulmaStyles.Helpers.isClearfix, ConnectUserComponentStyles.buttonsWrapper))(
                  <.ReactFacebookLogin(
                    ^.appId := self.props.wrapped.facebookAppId,
                    ^.scope := "public_profile, email",
                    ^.fields := "first_name, last_name, email, name, picture",
                    ^.callback := facebookCallbackResponse(self),
                    ^.cssClass := Seq(
                      MakeStyles.Button.facebook,
                      ConnectUserComponentStyles.button,
                      BulmaStyles.Helpers.isPulledLeft
                    ),
                    ^.iconClass := Seq(
                      ConnectUserComponentStyles.buttonIcon.htmlClass,
                      FontAwesomeStyles.facebook.htmlClass
                    ).mkString(" "),
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
                      ConnectUserComponentStyles.button,
                      BulmaStyles.Helpers.isPulledRight
                    )
                  )(
                    <.i(^.className := Seq(ConnectUserComponentStyles.buttonIcon, FontAwesomeStyles.googlePlus))(),
                    "google+"
                  )
                )
              ),
              <.div(^.className := ConnectUserComponentStyles.lineWrapper)(
                <.span(^.className := ConnectUserComponentStyles.line)(),
                <.Translate(^.className := ConnectUserComponentStyles.underlineText, ^.value := "form.or")(),
                <.span(^.className := ConnectUserComponentStyles.line)()
              ),
              <.div()(<.Translate(^.className := MakeStyles.Modal.title, ^.value := {
                if (self.state.isRegistering) {
                  "form.login.stdRegister"
                } else {
                  "form.login.stdConnect"
                }
              })(), if (self.state.isRegistering) {
                registerElement(self)
              } else {
                signInElement(self)
              })
            ),
            <.style()(ConnectUserComponentStyles.render[String])
          )
        )
      }
    )

  def signInElement(self: Self[ConnectUserProps, State]): ReactElement =
    <.form(^.onSubmit := handleSignInSubmit(self) _)(
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(FontAwesomeStyles.envelopeTransparent, MakeStyles.Form.inputIcon))(),
        <.input(
          ^.`type`.email,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := I18n.t("form.fieldLabelEmail"),
          ^.onChange := handleUsernameChange(self) _,
          ^.value := self.state.username
        )()
      ),
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(FontAwesomeStyles.lock, MakeStyles.Form.inputIcon))(),
        <.input(
          ^.`type`.password,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := I18n.t("form.fieldLabelPassword"),
          ^.onChange := handlePasswordChange(self) _,
          ^.value := self.state.password
        )()
      ),
      <.div()(<.span(^.className := ConnectUserComponentStyles.error)(self.state.errorMessage)),
      <.div()(
        <.button(^.className := Seq(MakeStyles.Button.default, ConnectUserComponentStyles.submitButton))(
          <.i(^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, ConnectUserComponentStyles.buttonIcon))(),
          <.Translate(^.value := "form.login.submitButton")()
        ),
        <.div(^.className := ConnectUserComponentStyles.text)(
          <.Translate(^.value := "form.login.oupsI")(),
          <.a(^.className := ConnectUserComponentStyles.link)(I18n.t("form.login.forgotPassword"))
        ),
        <.div()(
          <.Translate(^.value := "form.login.noAccount")(),
          <.a(^.className := ConnectUserComponentStyles.link, ^.onClick := toggleRegister(self))(
            I18n.t("form.login.createAccount")
          )
        )
      )
    )

  def registerElement(self: Self[ConnectUserProps, State]): ReactElement =
    <.form(^.onSubmit := handleRegisterSubmit(self) _)(
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(FontAwesomeStyles.envelopeTransparent, MakeStyles.Form.inputIcon))(),
        <.input(
          ^.`type`.email,
          ^.required := true,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := s"${I18n.t("form.fieldLabelEmail")} ${I18n.t("form.required")}",
          ^.onChange := handleUsernameChange(self) _,
          ^.value := self.state.username
        )()
      ),
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(FontAwesomeStyles.lock, MakeStyles.Form.inputIcon))(),
        <.i(
          ^.className := Seq(
            ConnectUserComponentStyles.eye(self.state.typePassword == "password"),
            MakeStyles.Form.inputIconLeft
          ),
          ^.onClick := toggleHidePassword(self) _
        )(),
        <.input(
          ^.`type` := self.state.typePassword,
          ^.required := true,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
          ^.onChange := handlePasswordChange(self) _,
          ^.value := self.state.password
        )()
      ),
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(FontAwesomeStyles.user, MakeStyles.Form.inputIcon))(),
        <.input(
          ^.`type`.text,
          ^.required := true,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := s"${I18n.t("form.fieldLabelFirstName")} ${I18n.t("form.required")}",
          ^.onChange := handleFirstNameChange(self) _,
          ^.value := self.state.firstName
        )()
      ),
      <.div()(<.span(^.className := ConnectUserComponentStyles.error)(self.state.errorMessage)),
      <.div(^.className := ConnectUserComponentStyles.terms)(I18n.t("form.register.termsAgreed")),
      <.div()(
        <.button(^.className := Seq(MakeStyles.Button.default, ConnectUserComponentStyles.submitButton))(
          <.i(^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, ConnectUserComponentStyles.buttonIcon))(),
          <.Translate(^.value := "form.register.subscribe")()
        ),
        <.div()(
          <.Translate(^.value := "form.register.alreadySubscribed")(),
          <.a(^.className := ConnectUserComponentStyles.link, ^.onClick := toggleRegister(self))(
            I18n.t("form.connection")
          )
        )
      ),
      <.div(^.className := ConnectUserComponentStyles.lineWrapper)(
        <.span(^.className := ConnectUserComponentStyles.line)()
      ),
      <.button(
        ^.className := Seq(ConnectUserComponentStyles.noRegisterButton, MakeStyles.Button.default),
        ^.onClick := closeModal(self) _
      )(I18n.t("form.register.noRegister"))
    )

  private def handleUsernameChange(self: Self[ConnectUserProps, State])(e: FormSyntheticEvent[HTMLInputElement]) = {
    val newUsername = e.target.value
    self.setState(_.copy(username = newUsername))
  }

  private def handlePasswordChange(self: Self[ConnectUserProps, State])(e: FormSyntheticEvent[HTMLInputElement]) = {
    val newPassword = e.target.value
    self.setState(_.copy(password = newPassword))
  }

  private def handleFirstNameChange(self: Self[ConnectUserProps, State])(e: FormSyntheticEvent[HTMLInputElement]) = {
    val newFirstName = e.target.value
    self.setState(_.copy(firstName = newFirstName))
  }

  private def handleSignInSubmit(self: Self[ConnectUserProps, State])(e: SyntheticEvent): Unit = {
    self.setState(self.state.copy(errorMessage = ""))
    e.preventDefault()
    self.props.wrapped.signIn(self.state.username, self.state.password, self)
  }

  private def handleRegisterSubmit(self: Self[ConnectUserProps, State])(e: SyntheticEvent): Unit = {
    self.setState(self.state.copy(errorMessage = ""))
    e.preventDefault()
    self.props.wrapped.register(self.state.username, self.state.password, self.state.firstName, self)
  }

  private def toggleHidePassword(self: Self[ConnectUserProps, State])(): Unit = {
    val typePassword = if (self.state.typePassword == "input") "password" else "input"
    self.setState(self.state.copy(typePassword = typePassword))
  }

  private def closeModal(self: Self[ConnectUserProps, State])(): Unit = {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.closeModal()
  }

  private def toggleRegister(self: Self[ConnectUserProps, State]) = () => {
    self.setState(self.state.copy(errorMessage = "", isRegistering = !self.state.isRegistering))
  }

  private def facebookCallbackResponse(self: Self[ConnectUserProps, State])(response: Response): Unit = {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.signInFacebook(response, self)
  }

  private def googleCallbackResponse(self: Self[ConnectUserProps, State])(response: Response): Unit = {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.signInGoogle(response, self)
  }

  private def googleCallbackFailure(self: Self[ConnectUserProps, State])(response: Response): Unit = {
    self.setState(self.state.copy(errorMessage = I18n.t("form.login.errorAuthenticationFailed")))
  }
}

object ConnectUserComponentStyles extends StyleSheet.Inline {

  import dsl._

  val buttons: StyleA = style()
  val buttonsWrapper: StyleA = style(margin.auto)
  val button: StyleA = style(width(48.8F.%%))
  val buttonIcon: StyleA = style(paddingBottom(0.5F.rem), paddingRight(0.9.rem))
  val buttonsInfo: StyleA = style(marginTop(1.4F.rem), display.block)

  val link: StyleA = style(color(MakeStyles.Color.pink), fontWeight.bold)

  val lineWrapper: StyleA =
    style(display.flex, alignItems.center, margin(3.4F.rem, auto, 2.9F.rem, auto))
  val line: StyleA =
    style(height(0.1F.rem), backgroundColor(rgba(0, 0, 0, 0.3)), flexGrow(1), marginTop(0.5F.rem), opacity(0.3))
  val underlineText: StyleA = style(MakeStyles.Font.playfairDisplayItalic, margin(0.rem, 1.6F.rem))
  val input: StyleA =
    style(height(4.rem), width(100.%%), (media.all.maxWidth(800.px))(height(3.rem)))
  val submitButton: StyleA = style(marginBottom(1.7F.rem))
  val noRegisterButton: StyleA =
    style(marginBottom(1.7F.rem), backgroundColor(MakeStyles.Color.grey).important, border.none)
  val text: StyleA = style(marginBottom(0.8F.rem))
  val terms: StyleA = style(marginBottom(0.8F.rem), fontSize(1.4.rem), textAlign.left)
  val error: StyleA = style(
    display.block,
    margin.auto,
    MakeStyles.Font.circularStdBook,
    fontSize(1.4F.rem),
    color(MakeStyles.Color.error),
    lineHeight(1.8F.rem),
    paddingBottom(1.rem)
  )

  val eye: (Boolean) => StyleA = styleF.bool(
    typePassword =>
      if (typePassword) {
        styleS(
          (&.hover)(cursor.pointer),
          addClassName(FontAwesomeStyles.eyeSlash.htmlClass),
          color(MakeStyles.Color.grey)
        )
      } else {
        styleS((&.hover)(cursor.pointer), addClassName(FontAwesomeStyles.eye.htmlClass))
    }
  )

}
