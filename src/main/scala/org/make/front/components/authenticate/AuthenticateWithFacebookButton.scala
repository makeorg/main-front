/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.util.{Failure, Success}

object AuthenticateWithFacebookButton {

  case class AuthenticateWithFacebookButtonProps(trackingContext: TrackingContext,
                                                 trackingParameters: Map[String, String],
                                                 trackingInternalOnlyParameters: Map[String, String],
                                                 isConnected: Boolean,
                                                 signIn: (Response) => Future[_],
                                                 facebookAppId: String,
                                                 errorMessages: js.Array[String],
                                                 isLookingLikeALink: Boolean)

  case class AuthenticateWithFacebookButtonState(errorMessages: js.Array[String] = js.Array())

  val reactClass: ReactClass =
    React.createClass[AuthenticateWithFacebookButtonProps, AuthenticateWithFacebookButtonState](
      displayName = "AuthenticateWithFacebook",
      getInitialState = { _ =>
        AuthenticateWithFacebookButtonState(js.Array())
      },
      render = { self =>
        def trackFailure(provider: String): Unit = {
          TrackingService
            .track(
              eventName = "authen-social-failure",
              trackingContext = self.props.wrapped.trackingContext,
              parameters = self.props.wrapped.trackingParameters + ("social-network" -> provider),
              internalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters
            )
        }

        // @toDo: manage specific errors
        def handleCallback(result: Future[_], provider: String): Unit = {
          result.onComplete {
            case Success(_) =>
              TrackingService
                .track(
                  eventName = "authen-social-success",
                  trackingContext = self.props.wrapped.trackingContext,
                  parameters = self.props.wrapped.trackingParameters + ("social-network" -> provider),
                  internalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters
                )
              self.setState(AuthenticateWithFacebookButtonState())
            case Failure(UnauthorizedHttpException) =>
              trackFailure(provider)
              self.setState(_.copy(errorMessages = js.Array("authenticate.no-account-found")))
            case Failure(BadRequestHttpException(_)) =>
              self.setState(_.copy(errorMessages = js.Array("authenticate.no-email-found")))
              trackFailure(provider)
            case Failure(_) =>
              self.setState(_.copy(errorMessages = js.Array("authenticate.error-message")))
              trackFailure(provider)
          }
        }

        val facebookCallbackResponse: (Response) => Unit = { response =>
          self.setState(self.state.copy(errorMessages = js.Array()))
          handleCallback(self.props.wrapped.signIn(response), "Facebook")
        }

        val buttonClasses: String =
          js.Array(if (self.props.wrapped.isLookingLikeALink) {
              TextStyles.smallText.htmlClass + " " + AuthenticateWithFacebookButtonStyles.buttonLookingLikeALink.htmlClass
            } else {
              CTAStyles.basic.htmlClass + " " + CTAStyles.basicOnButton.htmlClass + " " +
                AuthenticateWithFacebookButtonStyles.button.htmlClass
            })
            .mkString(" ")

        <.ReactFacebookLogin(
          ^.appId := self.props.wrapped.facebookAppId,
          ^.scope := "public_profile, email",
          ^.fields := "first_name, last_name, email, name, picture",
          ^.callback := facebookCallbackResponse,
          ^.disableMobileRedirect := true,
          ^.cssClass := buttonClasses,
          ^.containerStyle := Map("transition" -> "none", "opacity" -> 1).toJSDictionary,
          ^.version := "2.3",
          ^.iconElement :=
            <.i(^.className := js.Array(if (self.props.wrapped.isLookingLikeALink) {
              FontAwesomeStyles.facebookOfficial
            } else {
              FontAwesomeStyles.facebook
            }))(<.style()(AuthenticateWithFacebookButtonStyles.render[String])),
          ^.textButton := " Facebook"
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
