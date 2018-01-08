package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.client.{BadRequestHttpException, UnauthorizedHttpException}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.ReactFacebookLogin.{
  ReactFacebookLoginVirtualDOMAttributes,
  ReactFacebookLoginVirtualDOMElements
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSConverters._
import scala.util.{Failure, Success}

object AuthenticateWithFacebookButton {

  case class AuthenticateWithFacebookButtonProps(isConnected: Boolean,
                                                 signIn: (Response) => Future[_],
                                                 facebookAppId: String,
                                                 errorMessages: Seq[String],
                                                 isLookingLikeALink: Boolean)

  case class AuthenticateWithFacebookButtonState(errorMessages: Seq[String] = Seq.empty)

  val reactClass: ReactClass =
    React.createClass[AuthenticateWithFacebookButtonProps, AuthenticateWithFacebookButtonState](
      displayName = "AuthenticateWithFacebook",
      getInitialState = { _ =>
        AuthenticateWithFacebookButtonState(Seq.empty)
      },
      render = { self =>
        // @toDo: manage specific errors
        def handleCallback(result: Future[_], provider: String): Unit = {
          result.onComplete {
            case Success(_) =>
              TrackingService.track("authen-social-success", Map("source" -> provider))
              self.setState(AuthenticateWithFacebookButtonState())
            case Failure(UnauthorizedHttpException) =>
              self.setState(state => state.copy(errorMessages = Seq("authenticate.no-account-found")))
            case Failure(BadRequestHttpException(_)) =>
              self.setState(state => state.copy(errorMessages = Seq("authenticate.no-email-found")))
            case Failure(_) => self.setState(state => state.copy(errorMessages = Seq("authenticate.failure")))
          }
        }

        val facebookCallbackResponse: (Response) => Unit = { response =>
          self.setState(self.state.copy(errorMessages = Seq.empty))
          handleCallback(self.props.wrapped.signIn(response), "Facebook")
        }

        val buttonClasses: String =
          Seq(if (self.props.wrapped.isLookingLikeALink) {
            TextStyles.smallText.htmlClass + " " + AuthenticateWithFacebookButtonStyles.buttonLookingLikeALink.htmlClass
          } else {
            CTAStyles.basic.htmlClass + " " + CTAStyles.basicOnButton.htmlClass + " " +
              AuthenticateWithFacebookButtonStyles.button.htmlClass
          }).mkString(" ")

        <.ReactFacebookLogin(
          ^.appId := self.props.wrapped.facebookAppId,
          ^.scope := "public_profile, email",
          ^.fields := "first_name, last_name, email, name, picture",
          ^.callback := facebookCallbackResponse,
          ^.disableMobileRedirect := true,
          ^.cssClass := buttonClasses,
          ^.containerStyle := Map("transition" -> "none", "opacity" -> 1).toJSDictionary,
          ^.iconElement :=
            <.i(
              ^.className := Seq(
                if (self.props.wrapped.isLookingLikeALink) { FontAwesomeStyles.facebookOfficial } else {
                  FontAwesomeStyles.facebook
                }
              )
            )(<.style()(AuthenticateWithFacebookButtonStyles.render[String])),
          ^.textButton := " facebook"
        )()
      }
    )
}

object AuthenticateWithFacebookButtonStyles extends StyleSheet.Inline {

  import dsl._

  val buttonLookingLikeALink: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val button: StyleA =
    style(
      display.block,
      width(100.%%),
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.SocialNetworksColor.facebook)
    )
}
