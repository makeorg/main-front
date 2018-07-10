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

package org.make.front.components.share

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.ReactShare._
import org.make.front.models.OperationExpanded
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.scalajs.dom
import org.make.core.URI._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object ShareMobile {

  final case class ShareMobileProps(operation: OperationExpanded, intro: Option[String] = None)

  val reactClass: ReactClass =
    React
      .createClass[ShareMobileProps, Unit](
        displayName = "ShareProposal",
        render = (self) => {

          def shareUrl(network: String): String = {
            // Create utm params
            val utm: String =
              "" ? ("utm_source", network) &
                ("utm_medium", "user_share") &
                ("utm_campaign", self.props.wrapped.operation.slug) &
                ("utm_content", self.props.wrapped.operation.landingSequenceId.value)

            // repalce utm params
            val url: String = self.props.wrapped.operation.shareUrl.replaceFirst("_UTM_", utm)

            s"${dom.window.location.origin}$url"
          }

          def trackOnClick(network: String) = () => {
            TrackingService.track(
              "click-share-sequence",
              TrackingContext(
                location = TrackingLocation.sequencePage,
                operationSlug = Some(self.props.wrapped.operation.slug)
              ),
              Map("network" -> network),
              Map("sequenceId" -> self.props.wrapped.operation.landingSequenceId.value)
            )
          }

          def trackOnClose(network: String) = () => {
            TrackingService.track(
              "click-share-sequence-validate",
              TrackingContext(
                location = TrackingLocation.sequencePage,
                operationSlug = Some(self.props.wrapped.operation.slug)
              ),
              Map("network" -> network),
              Map("sequenceId" -> self.props.wrapped.operation.landingSequenceId.value)
            )
          }

          <.ul(^.className := ShareMobileStyles.list)(
            <.li(^.className := ShareMobileStyles.item)(
              <.FacebookShareButton(^.url := shareUrl("Facebook"), ^.beforeOnClick := trackOnClick("Facebook"))(
                <.button(^.className := js.Array(ShareMobileStyles.button, ShareMobileStyles.FacebookButton))(
                  <.i(^.className := js.Array(FontAwesomeStyles.facebook, ShareMobileStyles.icon))(),
                  "FACEBOOK"
                )
              )
            ),
            <.li(^.className := ShareMobileStyles.item)(
              <.TwitterShareButton(^.url := shareUrl("Twitter"), ^.beforeOnClick := trackOnClick("Twitter"))(
                <.button(^.className := js.Array(ShareMobileStyles.button, ShareMobileStyles.TwitterButton))(
                  <.i(^.className := js.Array(FontAwesomeStyles.twitter, ShareMobileStyles.icon))(),
                  "TWITTER"
                )
              )
            ),
            <.li(^.className := ShareMobileStyles.item)(
              <.GooglePlusShareButton(^.url := shareUrl("Google"), ^.beforeOnClick := trackOnClick("Google"))(
                <.button(^.className := js.Array(ShareMobileStyles.button, ShareMobileStyles.GooglePlusButton))(
                  <.i(^.className := js.Array(FontAwesomeStyles.googlePlus, ShareMobileStyles.icon))(),
                  "GOOGLE +"
                )
              )
            ),
            <.li()(
              <.LinkedinShareButton(^.url := shareUrl("Linkedin"), ^.beforeOnClick := trackOnClick("Linkedin"))(
                <.button(^.className := js.Array(ShareMobileStyles.button, ShareMobileStyles.LinkedInButton))(
                  <.i(^.className := js.Array(FontAwesomeStyles.linkedin, ShareMobileStyles.icon))(),
                  "LINKEDIN"
                )
              )
            ),
            <.style()(ShareMobileStyles.render[String])
          )
        }
      )
}

object ShareMobileStyles extends StyleSheet.Inline {

  import dsl._

  val list: StyleA = style(position.fixed, bottom(100.pxToEm()), right(25.pxToEm()), zIndex(10))

  val item: StyleA = style(marginBottom(10.pxToEm()))

  val button: StyleA = style(
    position.relative,
    display.block,
    ThemeStyles.Font.tradeGothicLTStd,
    color(ThemeStyles.TextColor.white),
    fontSize(15.pxToEm()),
    width(100.pxToEm(15)),
    paddingTop(10.pxToEm(15)),
    paddingBottom(5.pxToEm(15)),
    paddingLeft(50.pxToEm(15)),
    borderRadius(15.pxToEm(15))
  )

  val icon: StyleA = style(position.absolute, left(15.pxToEm(15)), top(50.%%), marginTop(-(18 / 2).pxToEm(15)))

  val FacebookButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.facebook))
  val TwitterButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.twitter))
  val GooglePlusButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.googlePlus))
  val LinkedInButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.linkedIn))

}
