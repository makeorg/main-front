package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.Components.RichVirtualDOMElements
import org.make.front.components.authenticate.login.LoginWithEmailOrSocialNetworks.LoginWithEmailOrSocialNetworksProps
import org.make.front.components.authenticate.register.RegisterWithSocialNetworksOrEmail.RegisterWithSocialNetworksOrEmailProps
import org.make.front.components.containers.RecoverPasswordContainer.RecoverPasswordContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape

object LoginOrRegister {

  case class LoginOrRegisterProps(displayView: String, onSuccessfulLogin: () => Unit = () => {})
  case class LoginOrRegisterState(currentView: String = "login")

  object LoginOrRegisterState {
    val empty = LoginOrRegisterState()
  }

  val reactClass: ReactClass =
    React.createClass[LoginOrRegisterProps, LoginOrRegisterState](
      getInitialState = { _ =>
        LoginOrRegisterState.empty
      },
      componentWillReceiveProps = { (self, props) =>
        self.setState(_.copy(currentView = props.wrapped.displayView))
      },
      render = { self =>
        val state = self.state
        val props = self.props.wrapped

        def goTo(view: String): (SyntheticEvent) => Unit = { event =>
          event.preventDefault()
          self.setState(_.copy(currentView = view))
        }

        <.div()(
          if (state.currentView == "login")
            Seq(
              <.LoginWithEmailOrSocialNetworksComponent(
                ^.wrapped := LoginWithEmailOrSocialNetworksProps(props.onSuccessfulLogin)
              )(),
              <.p()(
                unescape(I18n.t("form.login.oupsI")),
                <.a(^.onClick := goTo("reset-password"))(unescape(I18n.t("form.login.forgotPassword")))
              ),
              <.p()(
                unescape(I18n.t("form.login.noAccount")),
                <.a(^.onClick := goTo("register"))(unescape(I18n.t("form.login.createAccount")))
              )
            )
          else if (state.currentView == "reset-password")
            Seq(
              <.RecoverPasswordContainerComponent(
                ^.wrapped := RecoverPasswordContainerProps(props.onSuccessfulLogin)
              )(),
              <.p()(
                unescape(I18n.t("form.passwordRecovery.return")),
                <.a(^.onClick := goTo("login"))(unescape(I18n.t("form.passwordRecovery.connectScreen")))
              )
            )
          else
            Seq(
              <.RegisterWithSocialNetworksOrEmailComponent(
                ^.wrapped := RegisterWithSocialNetworksOrEmailProps(props.onSuccessfulLogin)
              )(),
              <.p()(
                unescape(I18n.t("form.register.alreadySubscribed")),
                <.a(^.onClick := goTo("login"))(unescape(I18n.t("form.connection")))
              )
            )
        )
      }
    )

}
