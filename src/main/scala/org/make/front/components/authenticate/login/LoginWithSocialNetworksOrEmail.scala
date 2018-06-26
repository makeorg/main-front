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

package org.make.front.components.authenticate.login

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.authenticate.AuthenticateWithSocialNetworks.AuthenticateWithSocialNetworksProps
import org.make.front.components.authenticate.login.LoginWithEmailContainer.LoginWithEmailContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.OperationId
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object LoginWithSocialNetworksOrEmail {

  case class LoginWithSocialNetworksOrEmailProps(trackingContext: TrackingContext,
                                                 trackingParameters: Map[String, String],
                                                 onSuccessfulLogin: () => Unit = () => {},
                                                 operationId: Option[OperationId])

  val reactClass: ReactClass = React
    .createClass[LoginWithSocialNetworksOrEmailProps, Unit](
      displayName = "LoginWithEmailOrSocialNetworks",
      render = { self =>
        <.div()(
          <.div(^.className := LoginWithSocialNetworksOrEmailStyles.introWrapper)(
            <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("authenticate.login.with-social-networks-intro")))
          ),
          <.AuthenticateWithSocialNetworksComponent(
            ^.wrapped := AuthenticateWithSocialNetworksProps(
              trackingContext = self.props.wrapped.trackingContext,
              trackingParameters = self.props.wrapped.trackingParameters,
              onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin,
              operationId = self.props.wrapped.operationId
            )
          )(),
          <.div(^.className := LoginWithSocialNetworksOrEmailStyles.separatorWrapper)(
            <.p(^.className := js.Array(LoginWithSocialNetworksOrEmailStyles.separator, TextStyles.smallText))(
              I18n.t("authenticate.login.separator")
            )
          ),
          <.div(^.className := LoginWithSocialNetworksOrEmailStyles.introWrapper)(
            <.p(^.className := TextStyles.smallTitle)(unescape(I18n.t("authenticate.login.with-email-intro")))
          ),
          <.LoginWithEmailComponent(
            ^.wrapped := LoginWithEmailContainerProps(
              note = "",
              trackingContext = self.props.wrapped.trackingContext,
              trackingParameters = self.props.wrapped.trackingParameters,
              onSuccessfulLogin = self.props.wrapped.onSuccessfulLogin
            )
          )(),
          <.style()(LoginWithSocialNetworksOrEmailStyles.render[String])
        )
      }
    )
}

object LoginWithSocialNetworksOrEmailStyles extends StyleSheet.Inline {

  import dsl._

  val introWrapper: StyleA = style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()), textAlign.center)

  val separatorWrapper: StyleA =
    style(
      margin(ThemeStyles.SpacingValue.small.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(margin(ThemeStyles.SpacingValue.medium.pxToEm(), `0`)),
      overflow.hidden,
      textAlign.center
    )

  val separator: StyleA = style(
    position.relative,
    display.inlineBlock,
    padding(`0`, 20.pxToEm()),
    ThemeStyles.Font.playfairDisplayItalic,
    fontStyle.italic,
    color(ThemeStyles.TextColor.lighter),
    &.before(
      content := "''",
      position.absolute,
      top(50.%%),
      left(100.%%),
      marginTop(-0.5.px),
      height(1.px),
      width(999999.pxToEm()),
      backgroundColor(ThemeStyles.BorderColor.veryLight)
    ),
    &.after(
      content := "''",
      position.absolute,
      top(50.%%),
      right(100.%%),
      marginTop(-0.5.px),
      height(1.px),
      width(999999.pxToEm()),
      backgroundColor(ThemeStyles.BorderColor.veryLight)
    )
  )
}
