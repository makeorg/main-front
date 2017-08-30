package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.front.facades.Localize.LocalizeVirtualDOMAttributes
import org.make.front.components.AppComponentStyles
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
                              forgotPassword: ()                                                => Unit,
                              isOpen: Boolean,
                              googleAppId: String,
                              facebookAppId: String,
                              isRegistering: Boolean,
                              isProposalFlow: Boolean = false)

  case class State(isOpen: Boolean,
                   isRegistering: Boolean,
                   username: String = "",
                   password: String = "",
                   firstName: String = "",
                   age: String = "",
                   postalCode: String = "",
                   profession: String = "",
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

        var overlayClass: Seq[StyleA] =
          Seq(MakeStyles.Modal.overlay, ConnectUserComponentStyles.overlay)
        var modalClass: Seq[StyleA] = Seq(MakeStyles.Modal.modal)
        var modalContentClass: Seq[StyleA] = Seq(MakeStyles.Modal.content)
        var modalCloseClass: Seq[StyleA] = Seq(MakeStyles.Modal.close)
        var socialLoginButtonLeftClass: Seq[StyleA] =
          Seq(MakeStyles.Button.facebook, ConnectUserComponentStyles.button, BulmaStyles.Helpers.isPulledRight)
        var socialLoginButtonRightClass: Seq[StyleA] =
          Seq(MakeStyles.Button.google, ConnectUserComponentStyles.button, BulmaStyles.Helpers.isPulledLeft)
        var buttonWrapperClass: Seq[StyleA] =
          Seq(BulmaStyles.Helpers.isClearfix, ConnectUserComponentStyles.buttonsWrapper)
        var formContainerClass: Seq[StyleA] = Seq()

        if (self.props.wrapped.isProposalFlow) {
          overlayClass = Seq(ProposalSubmitComponentStyles.overlayModalStyle)
          modalClass = Seq(MakeStyles.Modal.modal, ProposalSubmitComponentStyles.modalStyle)
          modalContentClass =
            Seq(ProposalSubmitComponentStyles.modalContent, ProposalSubmitComponentStyles.modalContentSignin)
          modalCloseClass = Seq(MakeStyles.Modal.close, ProposalSubmitComponentStyles.close)
          socialLoginButtonLeftClass = Seq(
            MakeStyles.Button.facebook,
            ConnectUserComponentStyles.button,
            BulmaStyles.Helpers.isPulledRight,
            ConnectUserComponentStyles.proposalSocialLeftButton
          )
          socialLoginButtonRightClass = Seq(
            MakeStyles.Button.google,
            ConnectUserComponentStyles.button,
            BulmaStyles.Helpers.isPulledLeft,
            ConnectUserComponentStyles.proposalSocialRightButton
          )
          buttonWrapperClass = Seq(BulmaStyles.Helpers.isClearfix, ConnectUserComponentStyles.proposalButtonsWrapper)
          formContainerClass = Seq(ConnectUserComponentStyles.proposalFormContainer)
        }

        <.div()(
          <.ReactModal(
            ^.contentLabel := "User Account Journey",
            ^.isOpen := self.state.isOpen,
            ^.overlayClassName := overlayClass,
            ^.className := modalClass,
            ^.onRequestClose := closeModal(self)
          )(
            <.a(^.onClick := closeModal(self), ^.className := modalCloseClass)(I18n.t("form.login.close")),
            <.div(^.className := modalContentClass)(
              <.div(^.className := ConnectUserComponentStyles.buttons)(if (self.props.wrapped.isProposalFlow) {
                <.div()(
                  <.Translate(
                    ^.className := ConnectUserComponentStyles.introFirstLine,
                    ^.value := "form.login.proposalIntroFirst"
                  )(),
                  <.Translate(
                    ^.className := ConnectUserComponentStyles.introSecondLine,
                    ^.value := "form.login.proposalIntroSecond"
                  )()
                )
              }, <.Translate(^.className := MakeStyles.Modal.title, ^.value := {
                if (self.state.isRegistering) {
                  "form.login.socialRegister"
                } else {
                  "form.login.socialConnect"
                }
              })(), <.div(^.className := buttonWrapperClass)(
                <.ReactFacebookLogin(
                  ^.appId := self.props.wrapped.facebookAppId,
                  ^.scope := "public_profile, email",
                  ^.fields := "first_name, last_name, email, name, picture",
                  ^.callback := facebookCallbackResponse(self),
                  ^.cssClass := socialLoginButtonLeftClass,
                  ^.iconClass := Seq(ConnectUserComponentStyles.buttonIcon.htmlClass, FontAwesomeStyles.facebook.htmlClass).mkString(" "),
                  ^.textButton := "facebook"
                )(),
                <.ReactGoogleLogin(
                  ^.clientID := self.props.wrapped.googleAppId,
                  ^.scope := "profile email",
                  ^.onSuccess := googleCallbackResponse(self),
                  ^.onFailure := googleCallbackFailure(self),
                  ^.isSignIn := self.props.wrapped.isConnected,
                  ^.className := socialLoginButtonRightClass
                )(<.i(^.className := Seq(ConnectUserComponentStyles.buttonIcon, FontAwesomeStyles.googlePlus))(), "google+")
              )),
              <.div(^.className := ConnectUserComponentStyles.lineWrapper)(
                <.span(^.className := ConnectUserComponentStyles.line)(),
                <.Translate(^.className := ConnectUserComponentStyles.underlineText, ^.value := "form.or")(),
                <.span(^.className := ConnectUserComponentStyles.line)()
              ),
              <.div(^.className := formContainerClass)(<.Translate(^.className := MakeStyles.Modal.title, ^.value := {
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

  def signInElement(self: Self[ConnectUserProps, State]): ReactElement = {

    var submitButtonContainer: Seq[StyleA] = Seq()
    var submitButton: Seq[StyleA] = Seq(MakeStyles.Button.default, ConnectUserComponentStyles.submitButton)
    var forgetPasswordClass: Seq[StyleA] = Seq(ConnectUserComponentStyles.text)
    var toggleSignInRegisterClass: Seq[StyleA] = Seq()

    if (self.props.wrapped.isProposalFlow) {
      submitButtonContainer = Seq(ConnectUserComponentStyles.proposalSubmitButtonContainer)
      submitButton = Seq(
        MakeStyles.Button.default,
        ConnectUserComponentStyles.submitButton,
        ConnectUserComponentStyles.proposalSubmitButton
      )
      forgetPasswordClass =
        Seq(ConnectUserComponentStyles.text, ConnectUserComponentStyles.proposalToggleSignInRegister)
      toggleSignInRegisterClass = Seq(ConnectUserComponentStyles.proposalToggleSignInRegister)
    }

    <.form(^.onSubmit := handleSignInSubmit(self))(
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.envelopeTransparent))(),
        <.input(
          ^.`type`.email,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := I18n.t("form.fieldLabelEmail"),
          ^.onChange := handleUsernameChange(self),
          ^.value := self.state.username
        )()
      ),
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.lock))(),
        <.input(
          ^.`type`.password,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := I18n.t("form.fieldLabelPassword"),
          ^.onChange := handlePasswordChange(self),
          ^.value := self.state.password
        )()
      ),
      <.div()(<.span(^.className := ConnectUserComponentStyles.errorMessage)(self.state.errorMessage)),
      <.div(^.className := submitButtonContainer)(
        <.button(^.className := submitButton)(
          <.i(^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, ConnectUserComponentStyles.buttonIcon))(),
          <.Translate(^.value := "form.login.submitButton")()
        ),
        <.div(^.className := forgetPasswordClass)(
          <.Translate(^.value := "form.login.oupsI")(),
          <.a(^.className := ConnectUserComponentStyles.link, ^.onClick := forgotPassword(self))(I18n.t("form.login.forgotPassword"))
        ),
        <.div(^.className := toggleSignInRegisterClass)(
          <.Translate(^.value := "form.login.noAccount")(),
          <.a(^.className := ConnectUserComponentStyles.link, ^.onClick := toggleRegister(self))(
            I18n.t("form.login.createAccount")
          )
        )
      )
    )
  }

  def registerElement(self: Self[ConnectUserProps, State]): ReactElement = {

    var termsClass: Seq[StyleA] = Seq(ConnectUserComponentStyles.terms)
    var submitButtonContainer: Seq[StyleA] = Seq()
    var submitButton: Seq[StyleA] = Seq(MakeStyles.Button.default, ConnectUserComponentStyles.submitButton)
    var toggleSignInRegisterClass: Seq[StyleA] = Seq()

    if (self.props.wrapped.isProposalFlow) {
      termsClass = Seq(ConnectUserComponentStyles.terms, ConnectUserComponentStyles.proposalTerms)
      submitButtonContainer = Seq(ConnectUserComponentStyles.proposalSubmitButtonContainer)
      submitButton = Seq(
        MakeStyles.Button.default,
        ConnectUserComponentStyles.submitButton,
        ConnectUserComponentStyles.proposalSubmitButton
      )
      toggleSignInRegisterClass = Seq(ConnectUserComponentStyles.proposalToggleSignInRegister)
    }

    <.form(^.onSubmit := handleRegisterSubmit(self))(
      // email field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.envelopeTransparent))(),
        <.input(
          ^.`type`.email,
          ^.required := true,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := s"${I18n.t("form.fieldLabelEmail")} ${I18n.t("form.required")}",
          ^.onChange := handleUsernameChange(self),
          ^.value := self.state.username
        )()
      ),
      // password field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.lock))(),
        <.i(
          ^.className := Seq(
            ConnectUserComponentStyles.eye(self.state.typePassword == "password"),
            MakeStyles.Form.inputIconLeft
          ),
          ^.onClick := toggleHidePassword(self)
        )(),
        <.input(
          ^.`type` := self.state.typePassword,
          ^.required := true,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
          ^.onChange := handlePasswordChange(self),
          ^.value := self.state.password
        )()
      ),
      // firstname field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.user))(),
        <.input(
          ^.`type`.text,
          ^.required := true,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := s"${I18n.t("form.fieldLabelFirstName")} ${I18n.t("form.required")}",
          ^.onChange := handleFirstNameChange(self),
          ^.value := self.state.firstName
        )()
      ),
      // extra proposal fields
      if (self.props.wrapped.isProposalFlow) { extraFields(self) },
      <.div()(<.span(^.className := ConnectUserComponentStyles.errorMessage)(self.state.errorMessage)),
      <.div(^.className := termsClass)(I18n.t("form.register.termsAgreed")),
      <.div(^.className := submitButtonContainer)(
        <.button(^.className := submitButton)(
          <.i(^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, ConnectUserComponentStyles.buttonIcon))(),
          <.Translate(^.value := "form.register.subscribe")()
        ),
        <.div(^.className := toggleSignInRegisterClass)(
          <.Translate(^.value := "form.register.alreadySubscribed")(),
          <.a(^.className := ConnectUserComponentStyles.link, ^.onClick := toggleRegister(self))(
            I18n.t("form.connection")
          )
        )
      ),
      if (!self.props.wrapped.isProposalFlow) {
        <.div()(
          <.div(^.className := ConnectUserComponentStyles.lineWrapper)(
            <.span(^.className := ConnectUserComponentStyles.line)()
          ),
          <.button(
            ^.className := Seq(ConnectUserComponentStyles.noRegisterButton, MakeStyles.Button.default),
            ^.onClick := closeModal(self)
          )(
            <.Translate(
              ^.dangerousHtml := true,
              ^.value := "form.register.noRegister",
              ^("break") := (if (isMobile) "<br>" else "")
            )()
          )
        )
      }
    )
  }

  def extraFields(self: Self[ConnectUserProps, State]): ReactElement = {

    val ages: Seq[Int] = Range(13, 100)

    <.div()(
      // age field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.child))(),
        <.select(
          ^.`type`.text,
          ^.required := true,
          ^.className := Seq(MakeStyles.Form.inputSelect, ConnectUserComponentStyles.input),
          ^.placeholder := s"${I18n.t("form.fieldLabelAge")}",
          ^.onChange := handleAgeChange(self),
          ^.value := self.state.age
        )(<.option(^.value := "")(s"${I18n.t("form.fieldLabelAge")}"), ages.map(age => <.option(^.value := age)(age)))
      ),
      // postal code field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.mapMarker))(),
        <.input(
          ^.`type`.text,
          ^.required := true,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := s"${I18n.t("form.fieldPostalCode")}",
          ^.onChange := handlePostalCodeChange(self),
          ^.value := self.state.postalCode
        )()
      ),
      // profession field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.suitCase))(),
        <.input(
          ^.`type`.text,
          ^.required := true,
          ^.className := Seq(MakeStyles.Form.inputText, ConnectUserComponentStyles.input),
          ^.placeholder := s"${I18n.t("form.fieldProfession")}",
          ^.onChange := handleProfessionChange(self),
          ^.value := self.state.profession
        )()
      )
    )
  }

  def isMobile: Boolean = {
    import org.scalajs.dom
    dom.window.hasOwnProperty("matchMedia") && dom.window.matchMedia("(max-width: 800px)").matches
  }

  // @toDo: add validation
  private def handleUsernameChange(self: Self[ConnectUserProps, State]) = (e: FormSyntheticEvent[HTMLInputElement]) => {
    val newUsername = e.target.value
    self.setState(_.copy(username = newUsername))
  }

  // @toDo: add validation
  private def handlePasswordChange(self: Self[ConnectUserProps, State]) = (e: FormSyntheticEvent[HTMLInputElement]) => {
    val newPassword = e.target.value
    self.setState(_.copy(password = newPassword))
  }

  // @toDo: add validation
  private def handleFirstNameChange(self: Self[ConnectUserProps, State]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newFirstName = e.target.value
      self.setState(_.copy(firstName = newFirstName))
    }

  // @toDo: add validation
  private def handleAgeChange(self: Self[ConnectUserProps, State]) = (e: FormSyntheticEvent[HTMLInputElement]) => {
    val newAge = e.target.value
    self.setState(_.copy(age = newAge))
  }

  // @toDo: add validation
  private def handlePostalCodeChange(self: Self[ConnectUserProps, State]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newPostalCode = e.target.value
      self.setState(_.copy(postalCode = newPostalCode))
    }

  // @toDo: add validation
  private def handleProfessionChange(self: Self[ConnectUserProps, State]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newProfession = e.target.value
      self.setState(_.copy(profession = newProfession))
    }

  private def handleSignInSubmit(self: Self[ConnectUserProps, State]) = (e: SyntheticEvent) => {
    self.setState(self.state.copy(errorMessage = ""))
    e.preventDefault()
    self.props.wrapped.signIn(self.state.username, self.state.password, self)
  }

  private def handleRegisterSubmit(self: Self[ConnectUserProps, State]) = (e: SyntheticEvent) => {
    self.setState(self.state.copy(errorMessage = ""))
    e.preventDefault()
    self.props.wrapped.register(self.state.username, self.state.password, self.state.firstName, self)
  }

  private def toggleHidePassword(self: Self[ConnectUserProps, State]) = () => {
    val typePassword = if (self.state.typePassword == "input") "password" else "input"
    self.setState(self.state.copy(typePassword = typePassword))
  }

  private def closeModal(self: Self[ConnectUserProps, State]) = () => {
    self.setState(self.state.copy(errorMessage = ""))
    self.props.wrapped.closeModal()
  }

  private def forgotPassword(self: Self[ConnectUserProps, State]) = () => {
    self.props.wrapped.forgotPassword()
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

  val overlay: StyleA = style(zIndex(1000))
  val buttons: StyleA = style()
  val buttonsWrapper: StyleA = style(margin.auto)
  val button: StyleA = style(width(48.8F.%%))
  val buttonsInfo: StyleA = style(marginTop(1.4F.rem), display.block)

  val link: StyleA = style(color(MakeStyles.Color.pink), fontWeight.bold)

  val lineWrapper: StyleA =
    style(display.flex, alignItems.center, margin(3.4F.rem, auto, 2.9F.rem, auto))
  val line: StyleA =
    style(height(0.1F.rem), backgroundColor(rgba(0, 0, 0, 0.3)), flexGrow(1), marginTop(0.5F.rem), opacity(0.3))
  val underlineText: StyleA = style(MakeStyles.Font.playfairDisplayItalic, margin(0.rem, 1.6F.rem), fontSize(1.8F.rem))
  val input: StyleA =
    style(height(4.rem), width(100.%%), (media.all.maxWidth(800.px))(height(3.rem)))
  val buttonIcon: StyleA = style(paddingBottom(0.5F.rem), paddingRight(0.9.rem))

  val submitButton: StyleA = style(marginBottom(1.7F.rem))
  val noRegisterButton: StyleA =
    style(
      marginBottom(1.7F.rem),
      backgroundColor(MakeStyles.Color.grey).important,
      border.none,
      (media.all.maxWidth(800.px))(fontSize(1.3F.rem), lineHeight(1.3F.rem), width(100.%%)),
      unsafeRoot("button.make-button-default.ConnectUserComponentStyles-noRegisterButton")(
        (media.all.maxWidth(800.px))(height.auto)
      )
    )
  val text: StyleA = style(marginBottom(0.8F.rem))
  val terms: StyleA = style(marginBottom(0.8F.rem), fontSize(1.4.rem), textAlign.left)

  val errorMessage: StyleA = style(
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

  val introFirstLine: StyleA = style(
    MakeStyles.Font.playfairDisplayItalic,
    fontSize(1.8F.rem),
    lineHeight(1.8F.rem),
    display.block,
    textAlign.center,
    marginBottom(1.7F.rem),
    color(MakeStyles.Color.black),
    (media.all.maxWidth(800.px))(fontSize(1.5F.rem), lineHeight(1.5F.rem), marginBottom(1.6F.rem))
  )
  val introSecondLine: StyleA =
    style(
      MakeStyles.Font.tradeGothicLTStd,
      fontSize(4.6F.rem),
      lineHeight(4.6F.rem),
      display.block,
      textAlign.center,
      textTransform.uppercase,
      marginBottom(6.1F.rem),
      color(MakeStyles.Color.black),
      (media.all.maxWidth(800.px))(fontSize(3.rem), lineHeight(3.rem), marginBottom(3.2F.rem))
    )
  val proposalSocialLeftButton: StyleA = style(width(22.8F.rem), (media.all.maxWidth(800.px))(width(14.7F.rem)))
  val proposalSocialRightButton: StyleA = style(width(22.8F.rem), (media.all.maxWidth(800.px))(width(14.7F.rem)))
  val proposalButtonsWrapper: StyleA =
    style(margin.auto, width(46.7F.rem), (media.all.maxWidth(800.px))(width(31.rem)))
  val proposalFormContainer: StyleA =
    style(width(46.7F.rem), margin.auto, (media.all.maxWidth(800.px))(width(31.rem)))
  val proposalTerms: StyleA = style(color(MakeStyles.Color.greyLight), lineHeight(1.4F.rem), marginBottom(4.1F.rem))
  val proposalSubmitButtonContainer: StyleA = style(display.flex, flexDirection.column)
  val proposalSubmitButton: StyleA = style(width(15.4F.rem), margin(0.rem, auto, 1.9F.rem, auto))
  val proposalToggleSignInRegister: StyleA =
    style(MakeStyles.Font.circularStdBook, fontSize(1.6F.rem), textAlign.center)

}
