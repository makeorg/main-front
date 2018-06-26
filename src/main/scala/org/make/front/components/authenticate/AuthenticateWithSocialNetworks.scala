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
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.authenticate.AuthenticateWithFacebookContainer.AuthenticateWithFacebookContainerProps
import org.make.front.components.authenticate.AuthenticateWithGoogleContainer.AuthenticateWithGoogleContainerProps
import org.make.front.models.OperationId
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object AuthenticateWithSocialNetworks {

  case class AuthenticateWithSocialNetworksProps(trackingContext: TrackingContext,
                                                 trackingParameters: Map[String, String],
                                                 note: String = "",
                                                 onSuccessfulLogin: () => Unit = () => {},
                                                 operationId: Option[OperationId])

  val reactClass: ReactClass =
    React
      .createClass[AuthenticateWithSocialNetworksProps, Unit](
        displayName = "AuthenticateWithSocialNetworks",
        render = { self =>
          <.div()(
            <.ul()(
              <.li(^.className := AuthenticateWithSocialNetworksStyles.facebookConnectButtonWrapper)(
                <.AuthenticateWithFacebookContainerComponent(
                  ^.wrapped := AuthenticateWithFacebookContainerProps(
                    trackingContext = self.props.wrapped.trackingContext,
                    trackingParameters = self.props.wrapped.trackingParameters,
                    onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin,
                    operationId = self.props.wrapped.operationId
                  )
                )()
              ),
              <.li(^.className := AuthenticateWithSocialNetworksStyles.googleConnectButtonWrapper)(
                <.AuthenticateWithGoogleContainerComponent(
                  ^.wrapped := AuthenticateWithGoogleContainerProps(
                    trackingContext = self.props.wrapped.trackingContext,
                    trackingParameters = self.props.wrapped.trackingParameters,
                    onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin,
                    operationId = self.props.wrapped.operationId
                  )
                )()
              )
            ),
            if (self.props.wrapped.note != "") {
              <.div(^.className := AuthenticateWithSocialNetworksStyles.noteWrapper)(
                <.p(^.className := js.Array(AuthenticateWithSocialNetworksStyles.note, TextStyles.smallText))(
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
        .beyondVerySmall(
          display.inlineBlock,
          verticalAlign.middle,
          width(50.%%),
          paddingRight(ThemeStyles.SpacingValue.smaller.pxToEm())
        )
    )

  val googleConnectButtonWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondVerySmall(
          display.inlineBlock,
          verticalAlign.middle,
          width(50.%%),
          marginTop.`0`,
          paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm())
        )
    )

  val noteWrapper: StyleA = style(
    marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
    ThemeStyles.MediaQueries
      .beyondSmall(marginTop(ThemeStyles.SpacingValue.small.pxToEm())),
    textAlign.center
  )

  val note: StyleA = style(color(ThemeStyles.TextColor.lighter))
}
