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

package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.authenticate.AuthenticateWithFacebookContainer.AuthenticateWithFacebookContainerProps
import org.make.front.components.authenticate.AuthenticateWithGoogleContainer.AuthenticateWithGoogleContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.OperationId
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object RegisterWithSocialNetworks {

  case class RegisterWithSocialNetworksProps(trackingContext: TrackingContext,
                                             trackingParameters: Map[String, String],
                                             trackingInternalOnlyParameters: Map[String, String],
                                             onSuccessfulLogin: () => Unit = () => {},
                                             operationId: Option[OperationId])

  val reactClass: ReactClass =
    React
      .createClass[RegisterWithSocialNetworksProps, Unit](
        displayName = "RegisterWithSocialNetworks",
        render = { self =>
          <.div(^.className := RegisterWithSocialNetworksStyles.wrapper)(
            <.p(^.className := js.Array(RegisterWithSocialNetworksStyles.text, TextStyles.smallText))(
              unescape(I18n.t("authenticate.register.with-social-networks.intro") + "&nbsp;")
            ),
            <.div(^.className := RegisterWithSocialNetworksStyles.buttonWrapper)(
              <.AuthenticateWithFacebookContainerComponent(
                ^.wrapped := AuthenticateWithFacebookContainerProps(
                  trackingContext = self.props.wrapped.trackingContext,
                  trackingParameters = self.props.wrapped.trackingParameters,
                  trackingInternalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters,
                  onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin,
                  isLookingLikeALink = true,
                  operationId = self.props.wrapped.operationId
                )
              )()
            ),
            <.p(^.className := js.Array(RegisterWithSocialNetworksStyles.text, TextStyles.smallText))(
              unescape("&nbsp;" + I18n.t("authenticate.register.with-social-networks.separator") + "&nbsp;")
            ),
            <.div(^.className := RegisterWithSocialNetworksStyles.buttonWrapper)(
              <.AuthenticateWithGoogleContainerComponent(
                ^.wrapped := AuthenticateWithGoogleContainerProps(
                  trackingContext = self.props.wrapped.trackingContext,
                  trackingParameters = self.props.wrapped.trackingParameters,
                  trackingInternalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters,
                  onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin,
                  isLookingLikeALink = true,
                  operationId = self.props.wrapped.operationId
                )
              )()
            ),
            <.style()(RegisterWithSocialNetworksStyles.render[String])
          )
        }
      )
}

object RegisterWithSocialNetworksStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.lighter))

  val text: StyleA =
    style(display.inlineBlock, verticalAlign.baseline)

  val buttonWrapper: StyleA =
    style(display.inlineBlock, verticalAlign.baseline, color(ThemeStyles.ThemeColor.primary))

}
