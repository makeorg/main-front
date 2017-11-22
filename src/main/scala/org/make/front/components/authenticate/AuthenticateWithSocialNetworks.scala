package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.client.UnauthorizedHttpException
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.ReactFacebookLogin.{
  ReactFacebookLoginVirtualDOMAttributes,
  ReactFacebookLoginVirtualDOMElements
}
import org.make.front.facades.ReactGoogleLogin.{
  ReactGoogleLoginVirtualDOMAttributes,
  ReactGoogleLoginVirtualDOMElements
}
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object AuthenticateWithSocialNetworks {

  case class AuthenticateWithSocialNetworksProps(note: String,
                                                 isConnected: Boolean,
                                                 signInGoogle: (Response)   => Future[_],
                                                 signInFacebook: (Response) => Future[_],
                                                 googleAppId: String,
                                                 facebookAppId: String,
                                                 errorMessages: Seq[String])

  case class AuthenticateWithSocialNetworksState(errorMessages: Seq[String] = Seq.empty)

  val reactClass: ReactClass =
    React.createClass[AuthenticateWithSocialNetworksProps, AuthenticateWithSocialNetworksState](
      displayName = "AuthenticateWithSocialNetworks",
      getInitialState = { _ =>
        AuthenticateWithSocialNetworksState(Seq.empty)
      },
      render = { self =>
        // @toDo: manage specific errors
        def handleCallback(result: Future[_], provider: String): Unit = {
          result.onComplete {
            case Success(_) =>
              FacebookPixel.fbq("trackCustom", "authen-social-success", js.Dictionary("source" -> provider))
              self.setState(AuthenticateWithSocialNetworksState())
            case Failure(UnauthorizedHttpException) =>
              self.setState(state => state.copy(errorMessages = Seq("authenticate.no-account-found")))
            case Failure(_) => self.setState(state => state.copy(errorMessages = Seq("authenticate.failure")))
          }
        }

        val facebookCallbackResponse: (Response) => Unit = { response =>
          self.setState(self.state.copy(errorMessages = Seq.empty))
          handleCallback(self.props.wrapped.signInFacebook(response), "Facebook")
        }

        val googleCallbackResponse: (Response) => Unit = { response =>
          self.setState(self.state.copy(errorMessages = Seq.empty))
          handleCallback(self.props.wrapped.signInGoogle(response), "Google")
        }

        val googleCallbackFailure: (Response) => Unit = { _ =>
          self.setState(self.state.copy(errorMessages = Seq(I18n.t("authenticate.no-account-found"))))
        }

        <.div()(
          <.div(^.className := AuthenticateWithSocialNetworksStyles.facebookConnectButtonWrapper)(
            <.ReactFacebookLogin(
              ^.appId := self.props.wrapped.facebookAppId,
              ^.scope := "public_profile, email",
              ^.fields := "first_name, last_name, email, name, picture",
              ^.callback := facebookCallbackResponse,
              ^.cssClass := Seq(
                CTAStyles.basic,
                CTAStyles.basicOnButton,
                AuthenticateWithSocialNetworksStyles.facebookConnectButton
              ),
              ^.iconElement := <.i(^.className := FontAwesomeStyles.facebook)(),
              ^.textButton := " facebook"
            )()
          ),
          <.div(^.className := AuthenticateWithSocialNetworksStyles.googlePlusConnectButtonWrapper)(
            <.ReactGoogleLogin(
              ^.clientID := self.props.wrapped.googleAppId,
              ^.scope := "profile email",
              ^.onSuccess := googleCallbackResponse,
              ^.onFailure := googleCallbackFailure,
              ^.isSignIn := self.props.wrapped.isConnected,
              ^.className := Seq(
                CTAStyles.basic,
                CTAStyles.basicOnButton,
                AuthenticateWithSocialNetworksStyles.googlePlusConnectButton
              )
            )(<.i(^.className := FontAwesomeStyles.googlePlus)(), " google+")
          ),
          if (self.props.wrapped.note != "") {
            <.div(^.className := AuthenticateWithSocialNetworksStyles.noteWrapper)(
              <.p(^.className := Seq(AuthenticateWithSocialNetworksStyles.note, TextStyles.smallText))(
                self.props.wrapped.note
              )
            )
          },
          <.style()(AuthenticateWithSocialNetworksStyles.render[String])
        )
      }
    )
}

object AuthenticateWithSocialNetworksStyles extends StyleSheet.Inline {
  import dsl._

  val facebookConnectButtonWrapper: StyleA =
    style(
      ThemeStyles.MediaQueries
        .beyondVerySmall(display.inlineBlock, width(50.%%), paddingRight(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val facebookConnectButton: StyleA =
    style(display.block, width(100.%%), backgroundColor(ThemeStyles.SocialNetworksColor.facebook))

  val googlePlusConnectButtonWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondVerySmall(
          display.inlineBlock,
          width(50.%%),
          marginTop.`0`,
          paddingLeft(ThemeStyles.SpacingValue.small.pxToEm())
        )
    )

  val googlePlusConnectButton: StyleA =
    style(display.block, width(100.%%), backgroundColor(ThemeStyles.SocialNetworksColor.googlePlus))

  val noteWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

  val note: StyleA = style(color(ThemeStyles.TextColor.lighter))

}
