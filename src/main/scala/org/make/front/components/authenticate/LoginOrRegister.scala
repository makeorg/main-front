package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.authenticate.login.LoginWithEmailOrSocialNetworks.LoginWithEmailOrSocialNetworksProps
import org.make.front.components.authenticate.register.RegisterWithSocialNetworksOrEmail.RegisterWithSocialNetworksOrEmailProps
import org.make.front.components.containers.RecoverPasswordContainer.RecoverPasswordContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Length, StyleA}

object LoginOrRegister {

  case class LoginOrRegisterProps(registerView: String = "register",
                                  displayView: String,
                                  onSuccessfulLogin: () => Unit = () => {})
  case class LoginOrRegisterState(currentView: String = "login")

  object LoginOrRegisterState {
    val empty = LoginOrRegisterState()
  }

  val reactClass: ReactClass =
    React.createClass[LoginOrRegisterProps, LoginOrRegisterState](getInitialState = { _ =>
      LoginOrRegisterState.empty
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

        <.div(^.className := LoginOrRegisterStyles.wrapper)(if (state.currentView == "login") {
          Seq(
            <.LoginWithEmailOrSocialNetworksComponent(
              ^.wrapped := LoginWithEmailOrSocialNetworksProps(props.onSuccessfulLogin)
            )(),
            <.p(^.className := Seq(LoginOrRegisterStyles.text, TextStyles.smallText))(
              unescape(I18n.t("form.login.oupsI")),
              <.a(^.className := TextStyles.boldText, ^.onClick := goTo("reset-password"))(
                unescape(I18n.t("form.login.forgotPassword"))
              )
            ),
            <.p(^.className := Seq(LoginOrRegisterStyles.text, TextStyles.smallText))(
              unescape(I18n.t("form.login.noAccount")) + " ",
              <.a(^.className := TextStyles.boldText, ^.onClick := goTo(props.registerView))(
                unescape(I18n.t("form.login.createAccount"))
              )
            )
          )
        } else if (state.currentView == "reset-password") {
          Seq(
            <.RecoverPasswordContainerComponent(^.wrapped := RecoverPasswordContainerProps(props.onSuccessfulLogin))(),
            <.p(^.className := Seq(LoginOrRegisterStyles.text, TextStyles.smallText))(
              unescape(I18n.t("form.passwordRecovery.return")) + " ",
              <.a(^.className := TextStyles.boldText, ^.onClick := goTo("login"))(
                unescape(I18n.t("form.passwordRecovery.connectScreen"))
              )
            )
          )
        } else if (state.currentView == "register-expanded") {
          Seq(
            <.RegisterWithSocialNetworksOrEmailExpandedComponent(
              ^.wrapped := RegisterWithSocialNetworksOrEmailProps(props.onSuccessfulLogin)
            )(),
            <.p(^.className := Seq(LoginOrRegisterStyles.text, TextStyles.smallText))(
              unescape(I18n.t("form.register.alreadySubscribed")) + " ",
              <.a(^.className := TextStyles.boldText, ^.onClick := goTo("login"))(unescape(I18n.t("form.connection")))
            )
          )
        } else {
          Seq(
            <.RegisterWithSocialNetworksOrEmailComponent(
              ^.wrapped := RegisterWithSocialNetworksOrEmailProps(props.onSuccessfulLogin)
            )(),
            <.p(^.className := Seq(LoginOrRegisterStyles.text, TextStyles.smallText))(
              unescape(I18n.t("form.register.alreadySubscribed")) + " ",
              <.a(^.className := TextStyles.boldText, ^.onClick := goTo("login"))(unescape(I18n.t("form.connection")))
            )
          )
        }, <.style()(LoginOrRegisterStyles.render[String]))
    })
}

object LoginOrRegisterStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style()

  val text: StyleA =
    style(
      textAlign.center,
      margin := s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      color(ThemeStyles.TextColor.lighter),
      unsafeChild("a")(color(ThemeStyles.ThemeColor.primary))
    )

}
