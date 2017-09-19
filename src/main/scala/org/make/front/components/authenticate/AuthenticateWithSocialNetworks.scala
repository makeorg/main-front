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
import org.scalajs.dom.experimental.Response

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
import scalacss.internal.{Length, StyleA}
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.ExecutionContext.Implicits.global

object AuthenticateWithSocialNetworks {

  case class AuthenticateWithSocialNetworksProps(intro: String,
                                                 note: String,
                                                 isConnected: Boolean,
                                                 signInGoogle: (Response)   => Future[_],
                                                 signInFacebook: (Response) => Future[_],
                                                 googleAppId: String,
                                                 facebookAppId: String,
                                                 errorMessages: Seq[String])

  case class AuthenticateWithSocialNetworksState(errorMessages: Seq[String] = Seq.empty)

  val reactClass: ReactClass =
    React.createClass[AuthenticateWithSocialNetworksProps, AuthenticateWithSocialNetworksState](
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

        <.div(^.className := LayoutRulesStyles.row)(
          <.div(^.className := Seq(AuthenticateWithSocialNetworksStyles.introWrapper, LayoutRulesStyles.col))(
            <.p(^.className := TextStyles.smallTitle)(self.props.wrapped.intro)
          ),
          <.div(^.className := Seq(LayoutRulesStyles.col, LayoutRulesStyles.colHalf))(
            <.ReactFacebookLogin(
              ^.appId := self.props.wrapped.facebookAppId,
              ^.scope := "public_profile, email",
              ^.fields := "first_name, last_name, email, name, picture",
              ^.callback := facebookCallbackResponse,
              ^.cssClass := Seq(
                CTAStyles.basic,
                CTAStyles.basicOnButton,
                AuthenticateWithSocialNetworksStyles.facebookButton
              ),
              ^.iconClass := FontAwesomeStyles.facebook.htmlClass,
              ^.textButton := " facebook"
            )()
          ),
          <.div(^.className := Seq(LayoutRulesStyles.col, LayoutRulesStyles.colHalf))(
            <.ReactGoogleLogin(
              ^.clientID := self.props.wrapped.googleAppId,
              ^.scope := "profile email",
              ^.onSuccess := googleCallbackResponse,
              ^.onFailure := googleCallbackFailure,
              ^.isSignIn := self.props.wrapped.isConnected,
              ^.className := Seq(
                CTAStyles.basic,
                CTAStyles.basicOnButton,
                AuthenticateWithSocialNetworksStyles.googlePlusButton
              ),
              ^.style := Map()
            )(<.i(^.className := FontAwesomeStyles.googlePlus)(), " google+")
          ),
          <.div(^.className := Seq(AuthenticateWithSocialNetworksStyles.noteWrapper, LayoutRulesStyles.col))(
            <.p(^.className := Seq(AuthenticateWithSocialNetworksStyles.note, TextStyles.smallText))(
              self.props.wrapped.note
            )
          ),
          <.style()(AuthenticateWithSocialNetworksStyles.render[String])
        )
      }
    )

  object AuthenticateWithSocialNetworksStyles extends StyleSheet.Inline {
    import dsl._

    //TODO: globalize function
    implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
      def pxToEm(browserContextSize: Int = 16): Length[Double] = {
        (baseSize.toFloat / browserContextSize.toFloat).em
      }
    }
    val introWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

    val facebookButton: StyleA = style(width(100.%%), backgroundColor(rgb(58, 89, 152)))

    val googlePlusButton: StyleA = style(width(100.%%), backgroundColor(rgb(219, 68, 55)))

    val noteWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

    val note: StyleA = style(color(ThemeStyles.TextColor.lighter))

  }

}
