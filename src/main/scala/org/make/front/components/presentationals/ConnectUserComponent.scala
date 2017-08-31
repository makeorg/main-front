package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.core.validation.{
  ChoiceConstraint,
  EmailConstraint,
  LengthConstraint,
  NotBlankConstraint,
  PasswordConstraint
}
import org.make.front.facades.{I18n, Replacements}
import org.make.front.facades.Localize.LocalizeVirtualDOMAttributes
import org.make.front.facades.ReactFacebookLogin._
import org.make.front.facades.ReactGoogleLogin._
import org.make.front.facades.ReactModal._
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.experimental.Response
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object ConnectUserComponent {
  case class ConnectUserProps(isConnected: Boolean,
                              signInGoogle: (Response, Self[ConnectUserProps, ConnectUserState])   => Unit,
                              signInFacebook: (Response, Self[ConnectUserProps, ConnectUserState]) => Unit,
                              signIn: (String, String, Self[ConnectUserProps, ConnectUserState])   => Unit,
                              register: (Self[ConnectUserProps, ConnectUserState])                 => Unit,
                              closeModal: ()                                                       => Unit,
                              handleForgotPasswordLinkClick: ()                                    => Unit,
                              isOpen: Boolean,
                              googleAppId: String,
                              facebookAppId: String,
                              isRegistering: Boolean,
                              isProposalFlow: Boolean = false)

  case class ConnectUserState(isOpen: Boolean,
                              isRegistering: Boolean,
                              email: String = "",
                              password: String = "",
                              firstName: String = "",
                              age: Option[String] = None,
                              postalCode: Option[String] = None,
                              profession: Option[String] = None,
                              errorMessage: Seq[String] = Seq.empty,
                              typePassword: String = "password",
                              emailErrorMessage: String = "",
                              firstNameErrorMessage: String = "",
                              passwordErrorMessage: String = "",
                              ageErrorMessage: String = "",
                              postalCodeErrorMessage: String = "",
                              professionErrorMessage: String = "")

  val agesChoices: Seq[Int] = Range(13, 100)

  lazy val reactClass: ReactClass =
    React.createClass[ConnectUserProps, ConnectUserState](
      displayName = getClass.toString,
      getInitialState = { self =>
        ConnectUserState(isOpen = self.props.wrapped.isOpen, isRegistering = self.props.wrapped.isRegistering)
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
        var formContainerClass: Seq[StyleA] = Seq.empty
        var socialInfo: Seq[StyleA] = Seq(ConnectUserComponentStyles.socialInfo)

        if (self.props.wrapped.isProposalFlow) {
          overlayClass = Seq(ConnectUserComponentStyles.proposalOverlay)
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
          socialInfo = Seq(ConnectUserComponentStyles.proposalSocialInfo)

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
                  )(),
                  <.div(^.className := ConnectUserComponentStyles.lineWrapper)(
                    <.span(^.className := ConnectUserComponentStyles.line)()
                  )
                )
              }, <.Translate(^.className := MakeStyles.Modal.title, ^.value := {
                if (self.state.isRegistering) {
                  "form.login.socialRegister"
                } else {
                  "form.login.socialConnect"
                }
              })(), <.div(^.className := buttonWrapperClass)(<.ReactFacebookLogin(^.appId := self.props.wrapped.facebookAppId, ^.scope := "public_profile, email", ^.fields := "first_name, last_name, email, name, picture", ^.callback := facebookCallbackResponse(self), ^.cssClass := socialLoginButtonLeftClass, ^.iconClass := Seq(ConnectUserComponentStyles.buttonIcon.htmlClass, FontAwesomeStyles.facebook.htmlClass).mkString(" "), ^.textButton := "facebook")(), <.ReactGoogleLogin(^.clientID := self.props.wrapped.googleAppId, ^.scope := "profile email", ^.onSuccess := googleCallbackResponse(self), ^.onFailure := googleCallbackFailure(self), ^.isSignIn := self.props.wrapped.isConnected, ^.className := socialLoginButtonRightClass)(<.i(^.className := Seq(ConnectUserComponentStyles.buttonIcon, FontAwesomeStyles.googlePlus))(), "google+"))),
              if (self.state.isRegistering) {
                <.div(^.className := socialInfo)(<.Translate(^.value := "form.login.socialInfo")())
              },
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

  def signInElement(self: Self[ConnectUserProps, ConnectUserState]): ReactElement = {

    var submitButtonContainer: Seq[StyleA] = Seq.empty
    var submitButton: Seq[StyleA] = Seq(MakeStyles.Button.default, ConnectUserComponentStyles.submitButton)
    var forgetPasswordClass: Seq[StyleA] = Seq(ConnectUserComponentStyles.text)
    var toggleSignInRegisterClass: Seq[StyleA] = Seq.empty

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

    <.form(^.onSubmit := handleSignInSubmit(self), ^.novalidate := true)(
      // email
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.envelopeTransparent))(),
        <.input(
          ^.`type`.email,
          ^.className := ConnectUserComponentStyles.input(!self.state.emailErrorMessage.isEmpty),
          ^.placeholder := I18n.t("form.fieldLabelEmail"),
          ^.onChange := handleEmailChange(self),
          ^.value := self.state.email
        )()
      ),
      <.div()(
        <.span(^.className := ConnectUserComponentStyles.errorMessage(!self.state.emailErrorMessage.isEmpty).htmlClass)(
          self.state.emailErrorMessage
        )
      ),
      // password
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.lock))(),
        <.input(
          ^.`type`.password,
          ^.className := ConnectUserComponentStyles.input(!self.state.passwordErrorMessage.isEmpty),
          ^.placeholder := I18n.t("form.fieldLabelPassword"),
          ^.onChange := handlePasswordChange(self),
          ^.value := self.state.password
        )()
      ),
      <.div()(
        <.span(
          ^.className := ConnectUserComponentStyles.errorMessage(!self.state.passwordErrorMessage.isEmpty).htmlClass
        )(self.state.passwordErrorMessage)
      ),
      <.div()(
        self.state.errorMessage
          .map(
            message =>
              <.Translate(
                ^.value := message,
                ^.dangerousHtml := true,
                ^.className := ConnectUserComponentStyles.errorMessage(!message.isEmpty)
              )()
          )
      ),
      <.div(^.className := submitButtonContainer)(
        <.button(^.className := submitButton)(
          <.i(^.className := Seq(FontAwesomeStyles.thumbsUp, ConnectUserComponentStyles.buttonIcon))(),
          <.Translate(^.value := "form.login.submitButton")()
        ),
        <.div(^.className := forgetPasswordClass)(
          <.Translate(^.value := "form.login.oupsI")(),
          <.a(^.className := ConnectUserComponentStyles.link, ^.onClick := handleForgotPasswordLinkClick(self))(
            I18n.t("form.login.forgotPassword")
          )
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

  def registerElement(self: Self[ConnectUserProps, ConnectUserState]): ReactElement = {

    var termsClass: Seq[StyleA] = Seq(ConnectUserComponentStyles.terms)
    var submitButtonContainer: Seq[StyleA] = Seq.empty
    var submitButton: Seq[StyleA] = Seq(MakeStyles.Button.default, ConnectUserComponentStyles.submitButton)
    var toggleSignInRegisterClass: Seq[StyleA] = Seq.empty

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

    <.form(^.onSubmit := handleRegisterSubmit(self), ^.novalidate := true)(
      // email field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.envelopeTransparent))(),
        <.input(
          ^.`type`.email,
          ^.required := true,
          ^.className := ConnectUserComponentStyles.input(!self.state.emailErrorMessage.isEmpty),
          ^.placeholder := s"${I18n.t("form.fieldLabelEmail")} ${I18n.t("form.required")}",
          ^.onChange := handleEmailChange(self),
          ^.value := self.state.email
        )()
      ),
      <.div()(
        <.span(^.className := ConnectUserComponentStyles.errorMessage(!self.state.emailErrorMessage.isEmpty).htmlClass)(
          self.state.emailErrorMessage
        )
      ),
      // password field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.lock))(),
        if (self.state.password.nonEmpty) {
          <.i(
            ^.className := Seq(
              ConnectUserComponentStyles.eye(self.state.typePassword == "password"),
              MakeStyles.Form.inputIconLeft
            ),
            ^.onClick := toggleHidePassword(self)
          )()
        },
        <.input(
          ^.`type` := self.state.typePassword,
          ^.required := true,
          ^.className := ConnectUserComponentStyles.input(!self.state.passwordErrorMessage.isEmpty),
          ^.placeholder := s"${I18n.t("form.fieldLabelPassword")} ${I18n.t("form.required")}",
          ^.onChange := handlePasswordChange(self),
          ^.value := self.state.password
        )()
      ),
      <.div()(
        <.span(
          ^.className := ConnectUserComponentStyles.errorMessage(!self.state.passwordErrorMessage.isEmpty).htmlClass
        )(self.state.passwordErrorMessage)
      ),
      // firstname field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.user))(),
        <.input(
          ^.`type`.text,
          ^.required := true,
          ^.className := ConnectUserComponentStyles.input(!self.state.firstNameErrorMessage.isEmpty),
          ^.placeholder := s"${I18n.t("form.fieldLabelFirstName")} ${I18n.t("form.required")}",
          ^.onChange := handleFirstNameChange(self),
          ^.value := self.state.firstName
        )()
      ),
      <.div()(
        <.span(
          ^.className := ConnectUserComponentStyles.errorMessage(!self.state.firstNameErrorMessage.isEmpty).htmlClass
        )(self.state.firstNameErrorMessage)
      ),
      // extra proposal fields
      if (self.props.wrapped.isProposalFlow) { extraFields(self) },
      <.div()(
        self.state.errorMessage
          .map(
            message =>
              <.Translate(
                ^.value := message,
                ^.dangerousHtml := true,
                ^.className := ConnectUserComponentStyles.errorMessage(!message.isEmpty)
              )()
          )
      ),
      <.div(^.className := termsClass)(I18n.t("form.register.termsAgreed")),
      <.div(^.className := submitButtonContainer)(
        <.button(^.className := submitButton)(
          <.i(^.className := Seq(FontAwesomeStyles.thumbsUp, ConnectUserComponentStyles.buttonIcon))(),
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

  def extraFields(self: Self[ConnectUserProps, ConnectUserState]): ReactElement = {

    <.div()(
      // age field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.child))(),
        <.select(
          ^.`type`.text,
          ^.required := true,
          ^.className := ConnectUserComponentStyles.select(!self.state.ageErrorMessage.isEmpty),
          ^.placeholder := s"${I18n.t("form.fieldLabelAge")}",
          ^.onChange := handleAgeChange(self),
          ^.value := self.state.age.getOrElse("")
        )(
          <.option(^.value := "")(s"${I18n.t("form.fieldLabelAge")}"),
          agesChoices.map(age => <.option(^.value := age)(age))
        )
      ),
      <.div()(
        <.span(^.className := ConnectUserComponentStyles.errorMessage(!self.state.ageErrorMessage.isEmpty).htmlClass)(
          self.state.ageErrorMessage
        )
      ),
      // postal code field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.mapMarker))(),
        <.input(
          ^.`type`.text,
          ^.required := true,
          ^.className := ConnectUserComponentStyles.input(!self.state.postalCodeErrorMessage.isEmpty),
          ^.placeholder := s"${I18n.t("form.fieldPostalCode")}",
          ^.onChange := handlePostalCodeChange(self),
          ^.value := self.state.postalCode.getOrElse("")
        )()
      ),
      <.div()(
        <.span(^.className := ConnectUserComponentStyles.errorMessage(self.state.postalCode.nonEmpty).htmlClass)(
          self.state.postalCodeErrorMessage
        )
      ),
      // profession field
      <.div(^.className := MakeStyles.Form.field)(
        <.i(^.className := Seq(MakeStyles.Form.inputIcon, FontAwesomeStyles.suitCase))(),
        <.input(
          ^.`type`.text,
          ^.required := true,
          ^.className := ConnectUserComponentStyles.input(!self.state.professionErrorMessage.isEmpty),
          ^.placeholder := s"${I18n.t("form.fieldProfession")}",
          ^.onChange := handleProfessionChange(self),
          ^.value := self.state.profession.getOrElse("")
        )()
      ),
      <.div()(
        <.span(
          ^.className := ConnectUserComponentStyles.errorMessage(!self.state.professionErrorMessage.isEmpty).htmlClass
        )(self.state.professionErrorMessage)
      )
    )
  }

  def isMobile: Boolean = {
    import org.scalajs.dom
    dom.window.hasOwnProperty("matchMedia") && dom.window.matchMedia("(max-width: 800px)").matches
  }

  private def handleEmailChange(self: Self[ConnectUserProps, ConnectUserState]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newEmail = e.target.value
      self.setState(_.copy(email = newEmail, emailErrorMessage = ""))
    }

  private def handlePasswordChange(self: Self[ConnectUserProps, ConnectUserState]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newPassword = e.target.value
      self.setState(_.copy(password = newPassword, passwordErrorMessage = ""))
    }

  private def handleFirstNameChange(self: Self[ConnectUserProps, ConnectUserState]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newFirstName = e.target.value
      self.setState(_.copy(firstName = newFirstName, firstNameErrorMessage = ""))
    }

  private def handleAgeChange(self: Self[ConnectUserProps, ConnectUserState]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newAge = e.target.value
      self.setState(_.copy(age = Some(newAge), ageErrorMessage = ""))
    }

  private def handlePostalCodeChange(self: Self[ConnectUserProps, ConnectUserState]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newPostalCode = e.target.value
      self.setState(_.copy(postalCode = Some(newPostalCode), postalCodeErrorMessage = ""))
    }

  private def handleProfessionChange(self: Self[ConnectUserProps, ConnectUserState]) =
    (e: FormSyntheticEvent[HTMLInputElement]) => {
      val newProfession = e.target.value
      self.setState(_.copy(profession = Some(newProfession), professionErrorMessage = ""))
    }

  private def handleSignInSubmit(self: Self[ConnectUserProps, ConnectUserState]) = (e: SyntheticEvent) => {
    self.setState(self.state.copy(errorMessage = Seq.empty))
    e.preventDefault()

    val errorEmailMessages: Seq[String] = NotBlankConstraint
      .validate(Some(self.state.email), Map("notBlank" -> I18n.t("form.register.errorBlankEmail")))
      .map(_.message)
    val errorPasswordMessages: Seq[String] = NotBlankConstraint
      .validate(Some(self.state.password), Map("notBlank" -> I18n.t("form.register.errorBlankPassword")))
      .map(_.message)

    self.setState(
      _.copy(
        emailErrorMessage = if (errorEmailMessages.nonEmpty) errorEmailMessages.head else "",
        passwordErrorMessage = if (errorPasswordMessages.nonEmpty) errorPasswordMessages.head else ""
      )
    )

    if (errorEmailMessages.isEmpty && errorPasswordMessages.isEmpty) {
      self.props.wrapped.signIn(self.state.email, self.state.password, self)
    }
  }

  private def handleRegisterSubmit(self: Self[ConnectUserProps, ConnectUserState]) = (e: SyntheticEvent) => {
    self.setState(self.state.copy(errorMessage = Seq.empty))
    e.preventDefault()

    val maxPostalCodeLength = 10
    val ageConstraint = new ChoiceConstraint(agesChoices.map(_.toString))
    val postalCodeConstraint = new LengthConstraint(max = Some(maxPostalCodeLength))

    val errorEmailMessages: Seq[String] = (EmailConstraint & NotBlankConstraint)
      .validate(
        Some(self.state.email),
        Map(
          "invalid" -> I18n.t("form.register.errorInvalidEmail"),
          "notBlank" -> I18n.t("form.register.errorBlankEmail")
        )
      )
      .map(_.message)
    val errorPasswordMessages: Seq[String] = (PasswordConstraint & NotBlankConstraint)
      .validate(
        Some(self.state.password),
        Map(
          "minMessage" -> I18n
            .t("form.register.errorMinPassword", Replacements(("min", PasswordConstraint.min.toString))),
          "notBlank" -> I18n.t("form.register.errorBlankPassword")
        )
      )
      .map(_.message)
    val errorAgeMessages: Seq[String] =
      ageConstraint.validate(self.state.age, Map("invalid" -> I18n.t("form.register.errorChoiceAge"))).map(_.message)
    val errorPostalCodeMessages: Seq[String] = postalCodeConstraint
      .validate(
        self.state.postalCode,
        Map(
          "maxMessage" -> I18n
            .t("form.register.errorMaxPostalCode", Replacements(("max", maxPostalCodeLength.toString)))
        )
      )
      .map(_.message)
    val errorFirstNameMessages: Seq[String] = NotBlankConstraint
      .validate(Some(self.state.firstName), Map("notBlank" -> I18n.t("form.register.errorBlankFirstName")))
      .map(_.message)

    self.setState(
      self.state.copy(
        emailErrorMessage = if (errorEmailMessages.nonEmpty) errorEmailMessages.head else "",
        passwordErrorMessage = if (errorPasswordMessages.nonEmpty) errorPasswordMessages.head else "",
        ageErrorMessage = if (errorAgeMessages.nonEmpty) errorAgeMessages.head else "",
        postalCodeErrorMessage = if (errorPostalCodeMessages.nonEmpty) errorPostalCodeMessages.head else "",
        firstNameErrorMessage = if (errorFirstNameMessages.nonEmpty) errorFirstNameMessages.head else ""
      )
    )

    if (errorEmailMessages.isEmpty && errorPasswordMessages.isEmpty && errorAgeMessages.isEmpty && errorPostalCodeMessages.isEmpty && errorFirstNameMessages.isEmpty) {
      self.props.wrapped.register(self)
    }
  }

  private def toggleHidePassword(self: Self[ConnectUserProps, ConnectUserState]) = () => {
    val typePassword = if (self.state.typePassword == "input") "password" else "input"
    self.setState(self.state.copy(typePassword = typePassword))
  }

  private def closeModal(self: Self[ConnectUserProps, ConnectUserState]) = () => {

    // TODO: need synchrone call using a callback not implemented actualy in scalajs-react
    self.setState(
      self.state.copy(
        firstName = "",
        password = "",
        email = "",
        age = None,
        profession = None,
        postalCode = None,
        firstNameErrorMessage = "",
        passwordErrorMessage = "",
        emailErrorMessage = "",
        ageErrorMessage = "",
        professionErrorMessage = "",
        postalCodeErrorMessage = "",
        errorMessage = Seq.empty,
        typePassword = "password"
      )
    )
    self.forceUpdate(self.props.wrapped.closeModal)
  }

  private def handleForgotPasswordLinkClick(self: Self[ConnectUserProps, ConnectUserState]) = () => {
    self.props.wrapped.handleForgotPasswordLinkClick()
  }

  private def toggleRegister(self: Self[ConnectUserProps, ConnectUserState]) = () => {
    self.setState(
      self.state.copy(
        errorMessage = Seq.empty,
        isRegistering = !self.state.isRegistering,
        password = "",
        age = None,
        profession = None,
        postalCode = None,
        ageErrorMessage = "",
        postalCodeErrorMessage = "",
        professionErrorMessage = "",
        passwordErrorMessage = "",
        typePassword = "password"
      )
    )
  }

  private def facebookCallbackResponse(self: Self[ConnectUserProps, ConnectUserState])(response: Response): Unit = {
    self.setState(self.state.copy(errorMessage = Seq.empty))
    self.props.wrapped.signInFacebook(response, self)
  }

  private def googleCallbackResponse(self: Self[ConnectUserProps, ConnectUserState])(response: Response): Unit = {
    self.setState(self.state.copy(errorMessage = Seq.empty))
    self.props.wrapped.signInGoogle(response, self)
  }

  private def googleCallbackFailure(self: Self[ConnectUserProps, ConnectUserState])(response: Response): Unit = {
    self.setState(self.state.copy(errorMessage = Seq(I18n.t("form.login.errorAuthenticationFailed"))))
  }
}

object ConnectUserComponentStyles extends StyleSheet.Inline {

  import dsl._

  val overlay: StyleA = style(zIndex(1000))
  val proposalOverlay: StyleA =
    style(addClassName(ProposalSubmitComponentStyles.overlayModalStyle.htmlClass), zIndex(10))
  val buttons: StyleA = style()
  val buttonsWrapper: StyleA = style(margin.auto)
  val button: StyleA = style(width(48.8F.%%))
  val buttonsInfo: StyleA = style(marginTop(1.4F.rem), display.block)

  val lineWrapper: StyleA =
    style(
      display.flex,
      alignItems.center,
      margin(3.4F.rem, auto, 2.9F.rem, auto),
      width(46.7F.rem),
      (media.all.maxWidth(800.px))(width(100.%%))
    )
  val line: StyleA =
    style(height(0.1F.rem), backgroundColor(rgba(0, 0, 0, 0.3)), flexGrow(1), marginTop(0.5F.rem), opacity(0.3))
  val underlineText: StyleA = style(MakeStyles.Font.playfairDisplayItalic, margin(0.rem, 1.6F.rem), fontSize(1.8F.rem))

  val input: (Boolean) => StyleA = styleF.bool(
    hasError =>
      if (hasError) {
        styleS(
          height(4.rem),
          width(100.%%),
          MakeStyles.Form.errorInput,
          addClassName(MakeStyles.Form.inputText.htmlClass),
          (media.all.maxWidth(800.px))(height(3.rem))
        )
      } else {
        styleS(
          height(4.rem),
          width(100.%%),
          addClassName(MakeStyles.Form.inputText.htmlClass),
          (media.all.maxWidth(800.px))(height(3.rem))
        )
    }
  )

  val select: (Boolean) => StyleA = styleF.bool(
    hasError =>
      if (hasError) {
        styleS(
          height(4.rem),
          width(100.%%),
          MakeStyles.Form.errorInput,
          addClassName(MakeStyles.Form.inputSelect.htmlClass),
          (media.all.maxWidth(800.px))(height(3.rem))
        )
      } else {
        styleS(
          height(4.rem),
          width(100.%%),
          addClassName(MakeStyles.Form.inputSelect.htmlClass),
          (media.all.maxWidth(800.px))(height(3.rem))
        )
    }
  )

  val errorMessage: (Boolean) => StyleA = styleF.bool(
    hasError =>
      if (hasError) {
        styleS(MakeStyles.Form.errorMessage, unsafeChild("a") {
          color(MakeStyles.Color.error)
        })
      } else {
        styleS(height(0.rem), unsafeChild("a") {
          color(MakeStyles.Color.error)
        })
    }
  )

  val buttonIcon: StyleA = style(paddingBottom(0.5F.rem), paddingRight(0.9.rem))

  val submitButton: StyleA = style(marginBottom(1.7F.rem))

  val link: StyleA = style(color(MakeStyles.Color.pink), fontWeight.bold)
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
      color(MakeStyles.Color.black),
      (media.all.maxWidth(800.px))(fontSize(3.rem), lineHeight(3.rem))
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

  val socialInfo: StyleA =
    style(
      MakeStyles.Font.circularStdBook,
      fontSize(1.6F.rem),
      lineHeight(1.6F.rem),
      color(MakeStyles.Color.greyLight),
      textAlign.center,
      marginTop(1.6F.rem),
      (media.all.maxWidth(800.px))(fontSize(1.3F.rem), lineHeight(1.3F.rem))
    )
  val proposalSocialInfo: StyleA =
    style(
      MakeStyles.Font.circularStdBook,
      fontSize(1.6F.rem),
      lineHeight(1.6F.rem),
      color(MakeStyles.Color.greyLight),
      textAlign.center,
      marginTop(1.8F.rem),
      (media.all.maxWidth(800.px))(fontSize(1.3F.rem), lineHeight(1.3F.rem))
    )
}
