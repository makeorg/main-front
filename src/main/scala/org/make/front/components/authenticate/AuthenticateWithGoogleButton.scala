package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.client.{BadRequestHttpException, UnauthorizedHttpException}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.ReactGoogleLogin.{
  ReactGoogleLoginVirtualDOMAttributes,
  ReactGoogleLoginVirtualDOMElements
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSConverters._
import scala.util.{Failure, Success}

object AuthenticateWithGoogleButton {

  case class AuthenticateWithGoogleButtonProps(trackingContext: TrackingContext,
                                               trackingParameters: Map[String, String],
                                               isConnected: Boolean,
                                               signIn: (Response) => Future[_],
                                               googleAppId: String,
                                               errorMessages: Seq[String],
                                               isLookingLikeALink: Boolean)

  case class AuthenticateWithGoogleButtonState(errorMessages: Seq[String] = Seq.empty)

  val reactClass: ReactClass =
    React.createClass[AuthenticateWithGoogleButtonProps, AuthenticateWithGoogleButtonState](
      displayName = "AuthenticateWithGoogle",
      getInitialState = { _ =>
        AuthenticateWithGoogleButtonState(Seq.empty)
      },
      render = { self =>
        def trackFailure(provider: String): Unit = {
          TrackingService
            .track(
              "authen-social-failure",
              self.props.wrapped.trackingContext,
              self.props.wrapped.trackingParameters + ("social-network" -> provider)
            )
        }
        // @toDo: manage specific errors
        def handleCallback(result: Future[_], provider: String): Unit = {
          result.onComplete {
            case Success(_) =>
              TrackingService
                .track(
                  "authen-social-success",
                  self.props.wrapped.trackingContext,
                  self.props.wrapped.trackingParameters + ("social-network" -> provider)
                )
              self.setState(AuthenticateWithGoogleButtonState())
            case Failure(UnauthorizedHttpException) =>
              trackFailure(provider)
              self.setState(state => state.copy(errorMessages = Seq("authenticate.no-account-found")))
            case Failure(BadRequestHttpException(_)) =>
              trackFailure(provider)
              self.setState(state => state.copy(errorMessages = Seq("authenticate.no-email-found")))
            case Failure(_) =>
              trackFailure(provider)
              self.setState(state => state.copy(errorMessages = Seq("authenticate.error-message")))
          }
        }

        val googleCallbackResponse: (Response) => Unit = { response =>
          self.setState(self.state.copy(errorMessages = Seq.empty))
          handleCallback(self.props.wrapped.signIn(response), "Google")
        }

        val googleCallbackFailure: (Response) => Unit = { _ =>
          self.setState(self.state.copy(errorMessages = Seq(I18n.t("authenticate.no-account-found"))))
        }

        val buttonClasses: String =
          Seq(if (self.props.wrapped.isLookingLikeALink) {
            TextStyles.smallText.htmlClass + " " +
              AuthenticateWithGoogleButtonStyles.buttonLookingLikeALink.htmlClass
          } else {
            CTAStyles.basic.htmlClass + " " + CTAStyles.basicOnButton.htmlClass + " " +
              AuthenticateWithGoogleButtonStyles.button.htmlClass
          }).mkString(" ")

        <.ReactGoogleLogin(
          ^.clientID := self.props.wrapped.googleAppId,
          ^.scope := "profile email",
          ^.onSuccess := googleCallbackResponse,
          ^.onFailure := googleCallbackFailure,
          ^.isSignIn := self.props.wrapped.isConnected,
          ^.buttonStyle := Map("transition" -> "none", "opacity" -> 1).toJSDictionary,
          ^.buttonDisabledStyle := Map("transition" -> "none", "opacity" -> 1).toJSDictionary,
          ^.className := buttonClasses
        )(
          <.span(^.className := FontAwesomeStyles.googlePlus)(),
          " Google+",
          <.style()(AuthenticateWithGoogleButtonStyles.render[String])
        )
      }
    )
}

object AuthenticateWithGoogleButtonStyles extends StyleSheet.Inline {

  import dsl._

  val buttonLookingLikeALink: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val button: StyleA =
    style(
      display.block,
      width(100.%%),
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.SocialNetworksColor.googlePlus)
    )
}
