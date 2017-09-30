package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.client.UnauthorizedHttpException
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.ReactFacebookLogin.{
  ReactFacebookLoginVirtualDOMAttributes,
  ReactFacebookLoginVirtualDOMElements
}
import org.make.front.facades.ReactGoogleLogin.{
  ReactGoogleLoginVirtualDOMAttributes,
  ReactGoogleLoginVirtualDOMElements
}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

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
        def handleCallback(result: Future[_]): Unit = {
          result.onComplete {
            case Success(_) => self.setState(AuthenticateWithSocialNetworksState())
            case Failure(UnauthorizedHttpException) =>
              self.setState(state => state.copy(errorMessages = Seq("form.login.errorAuthenticationFailed")))
            case Failure(_) => self.setState(state => state.copy(errorMessages = Seq("form.login.errorSignInFailed")))
          }
        }

        val facebookCallbackResponse: (Response) => Unit = { response =>
          self.setState(self.state.copy(errorMessages = Seq.empty))
          handleCallback(self.props.wrapped.signInFacebook(response))
        }

        val googleCallbackResponse: (Response) => Unit = { response =>
          self.setState(self.state.copy(errorMessages = Seq.empty))
          handleCallback(self.props.wrapped.signInGoogle(response))
        }

        val googleCallbackFailure: (Response) => Unit = { _ =>
          self.setState(self.state.copy(errorMessages = Seq(I18n.t("form.login.errorAuthenticationFailed"))))
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
              ^.iconClass := s"${FontAwesomeStyles.fa.htmlClass} ${FontAwesomeStyles.facebook.htmlClass}",
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
            )(<.i(^.className := Seq(FontAwesomeStyles.fa, FontAwesomeStyles.googlePlus))(), " google+")
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
        .beyondVerySmall(display.inlineBlock, width(50.%%), paddingRight(LayoutRulesStyles.gutter))
    )

  val facebookConnectButton: StyleA =
    style(display.block, width(100.%%), backgroundColor(ThemeStyles.SocialNetworksColor.facebook))

  val googlePlusConnectButtonWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondVerySmall(display.inlineBlock, width(50.%%), marginTop.`0`, paddingLeft(LayoutRulesStyles.gutter))
    )

  val googlePlusConnectButton: StyleA =
    style(display.block, width(100.%%), backgroundColor(ThemeStyles.SocialNetworksColor.googlePlus))

  val noteWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

  val note: StyleA = style(color(ThemeStyles.TextColor.lighter))

}
