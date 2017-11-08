package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.authenticate.login.LoginWithEmailOrSocialNetworks.LoginWithEmailOrSocialNetworksProps
import org.make.front.components.authenticate.recoverPassword.RecoverPasswordContainer.RecoverPasswordContainerProps
import org.make.front.components.authenticate.register.RegisterWithSocialNetworksOrEmail.RegisterWithSocialNetworksOrEmailProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.OperationId
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

object LoginOrRegister {

  case class LoginOrRegisterProps(operation: Option[OperationId] = None,
                                  registerView: String = "register",
                                  displayView: String,
                                  onSuccessfulLogin: () => Unit = () => {})
  case class LoginOrRegisterState(currentView: String = "login")

  object LoginOrRegisterState {
    val empty = LoginOrRegisterState()
  }

  val reactClass: ReactClass =
    React.createClass[LoginOrRegisterProps, LoginOrRegisterState](displayName = "LoginOrRegister", getInitialState = {
      self =>
        LoginOrRegisterState(currentView = self.props.wrapped.displayView)
    }, componentWillReceiveProps = { (self, props) =>
      self.setState(_.copy(currentView = props.wrapped.displayView))
    }, render = {
      self =>
        val state = self.state
        val props = self.props.wrapped

        def goTo(view: String): (SyntheticEvent) => Unit = { event =>
          event.preventDefault()
          self.setState(_.copy(currentView = view))
        }

        <.div(^.className := RowRulesStyles.evenNarrowerCenteredRow)(
          <.div(^.className := ColRulesStyles.col)(if (state.currentView == "login") {
            Seq(
              <.LoginWithEmailOrSocialNetworksComponent(
                ^.wrapped := LoginWithEmailOrSocialNetworksProps(props.onSuccessfulLogin)
              )(),
              <.p(^.className := Seq(LoginOrRegisterStyles.text, TextStyles.smallText))(
                unescape(I18n.t("authenticate.forgot-password.intro") + " "),
                <.a(^.className := TextStyles.boldText, ^.onClick := goTo("reset-password"))(
                  unescape(I18n.t("authenticate.forgot-password.link-support"))
                )
              ),
              <.p(^.className := Seq(LoginOrRegisterStyles.text, TextStyles.smallText))(
                unescape(I18n.t("authenticate.switch-to-register-screen.intro") + " "),
                <.a(^.className := TextStyles.boldText, ^.onClick := goTo(props.registerView))(
                  unescape(I18n.t("authenticate.switch-to-register-screen.link-support"))
                )
              )
            )
          } else if (state.currentView == "reset-password") {
            Seq(
              <.RecoverPasswordContainerComponent(
                ^.wrapped := RecoverPasswordContainerProps(props.onSuccessfulLogin)
              )(),
              <.p(^.className := Seq(LoginOrRegisterStyles.text, TextStyles.smallText))(
                unescape(I18n.t("authenticate.back-to-login-screen.intro") + " "),
                <.a(^.className := TextStyles.boldText, ^.onClick := goTo("login"))(
                  unescape(I18n.t("authenticate.back-to-login-screen.link-support"))
                )
              )
            )
          } else {
            Seq(
              <.RegisterWithSocialNetworksOrEmailComponent(
                ^.wrapped := RegisterWithSocialNetworksOrEmailProps(
                  operation = self.props.wrapped.operation,
                  onSuccessfulLogin = props.onSuccessfulLogin
                )
              )(),
              <.p(^.className := Seq(LoginOrRegisterStyles.text, TextStyles.smallText))(
                unescape(I18n.t("authenticate.switch-to-login-screen.intro") + " "),
                <.a(^.className := TextStyles.boldText, ^.onClick := goTo("login"))(
                  unescape(I18n.t("authenticate.switch-to-login-screen.link-support"))
                )
              )
            )
          }, <.style()(LoginOrRegisterStyles.render[String]))
        )
    })
}

object LoginOrRegisterStyles extends StyleSheet.Inline {

  import dsl._

  val text: StyleA =
    style(
      textAlign.center,
      margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      color(ThemeStyles.TextColor.lighter),
      unsafeChild("a")(color(ThemeStyles.ThemeColor.primary))
    )
}
