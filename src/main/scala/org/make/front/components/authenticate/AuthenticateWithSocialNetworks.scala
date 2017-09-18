package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.ConnectUser.ConnectUserStyles
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.facades.ReactFacebookLogin.{
  ReactFacebookLoginVirtualDOMAttributes,
  ReactFacebookLoginVirtualDOMElements
}
import org.make.front.facades.ReactGoogleLogin.{
  ReactGoogleLoginVirtualDOMAttributes,
  ReactGoogleLoginVirtualDOMElements
}
import org.make.front.styles.FontAwesomeStyles
import org.scalajs.dom.experimental.Response

object AuthenticateWithSocialNetworks {

  case class AuthenticateWithSocialNetworksProps(
    title: String,
    comment: String,
    isConnected: Boolean,
    signInGoogle: (Response, Self[AuthenticateWithSocialNetworksProps, AuthenticateWithSocialNetworksState])   => Unit,
    signInFacebook: (Response, Self[AuthenticateWithSocialNetworksProps, AuthenticateWithSocialNetworksState]) => Unit,
    googleAppId: String,
    facebookAppId: String,
    errorMessages: Seq[String]
  )

  case class AuthenticateWithSocialNetworksState(errorMessages: Seq[String] = Seq.empty)

  val reactClass: ReactClass =
    React.createClass[AuthenticateWithSocialNetworksProps, AuthenticateWithSocialNetworksState](
      getInitialState = { _ =>
        AuthenticateWithSocialNetworksState(Seq.empty)
      },
      render = { self =>
        def facebookCallbackResponse()(response: Response): Unit = {
          self.setState(self.state.copy(errorMessages = Seq.empty))
          self.props.wrapped.signInFacebook(response, self)
        }
        def googleCallbackResponse()(response: Response): Unit = {
          self.setState(self.state.copy(errorMessages = Seq.empty))
          self.props.wrapped.signInGoogle(response, self)
        }

        def googleCallbackFailure()(response: Response): Unit = {
          self.setState(self.state.copy(errorMessages = Seq(I18n.t("form.login.errorAuthenticationFailed"))))
        }

        <.div()(
          <.p()(self.props.wrapped.title),
          <.div()(
            <.ReactFacebookLogin(
              ^.appId := self.props.wrapped.facebookAppId,
              ^.scope := "public_profile, email",
              ^.fields := "first_name, last_name, email, name, picture",
              ^.callback := facebookCallbackResponse(),
              ^.cssClass := "",
              ^.iconClass := Seq(FontAwesomeStyles.facebook)
                .map(_.htmlClass)
                .mkString(" "),
              ^.textButton := "facebook"
            )(),
            <.ReactGoogleLogin(
              ^.clientID := self.props.wrapped.googleAppId,
              ^.scope := "profile email",
              ^.onSuccess := googleCallbackResponse(),
              ^.onFailure := googleCallbackFailure(),
              ^.isSignIn := self.props.wrapped.isConnected,
              ^.className := ""
            )(<.i(^.className := Seq(ConnectUserStyles.buttonIcon, FontAwesomeStyles.googlePlus))(), "google+")
          ),
          <.p()(self.props.wrapped.comment)
        )
      }
    )

  /*
  object AuthenticateWithSocialNetworksStyle extends StyleSheet.Inline {
    import dsl._

  }
 */
}
