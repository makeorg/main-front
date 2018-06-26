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
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.scalajs.dom
import org.make.core.URI._

import scala.scalajs.js

object ShareProposal {

  final case class ShareProps(operation: OperationExpanded, intro: Option[String] = None)

  val reactClass: ReactClass =
    React
      .createClass[ShareProps, Unit](
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
              Map("sequenceId" -> self.props.wrapped.operation.landingSequenceId.value, "network" -> network)
            )
          }

          def trackOnClose(network: String) = () => {
            TrackingService.track(
              "click-share-sequence-validate",
              TrackingContext(
                location = TrackingLocation.sequencePage,
                operationSlug = Some(self.props.wrapped.operation.slug)
              ),
              Map("sequenceId" -> self.props.wrapped.operation.landingSequenceId.value, "network" -> network)
            )
          }

          if (self.props.wrapped.intro.isDefined) {
            <.div(^.className := ShareStyles.intro)(
              <.p(^.className := TextStyles.smallerTitle)(self.props.wrapped.intro.getOrElse(""))
            )
          }
          <.ul(^.className := ShareStyles.list)(
            <.li(^.className := ShareStyles.item)(
              <.FacebookShareButton(^.url := shareUrl("Facebook"), ^.beforeOnClick := trackOnClick("Facebook"))(
                <.button(^.className := js.Array(ShareStyles.button, ShareStyles.shareWithFacebookButton))()
              )
            ),
            <.li(^.className := ShareStyles.item)(
              <.TwitterShareButton(^.url := shareUrl("Twitter"), ^.beforeOnClick := trackOnClick("Twitter"))(
                <.button(^.className := js.Array(ShareStyles.button, ShareStyles.shareWithTwitterButton))()
              )
            ),
            <.li(^.className := ShareStyles.item)(
              <.GooglePlusShareButton(^.url := shareUrl("Google"), ^.beforeOnClick := trackOnClick("Google"))(
                <.button(^.className := js.Array(ShareStyles.button, ShareStyles.shareWithGooglePlusButton))()
              )
            ),
            <.li(^.className := ShareStyles.item)(
              <.LinkedinShareButton(^.url := shareUrl("Linkedin"), ^.beforeOnClick := trackOnClick("Linkedin"))(
                <.button(^.className := js.Array(ShareStyles.button, ShareStyles.shareWithLinkedInButton))()
              )
            ),
            <.style()(ShareStyles.render[String])
          )
        }
      )
}

object ShareStyles extends StyleSheet.Inline {

  import dsl._

  val intro: StyleA = style(
    display.inlineBlock,
    color(ThemeStyles.TextColor.lighter),
    marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
    paddingTop(10.pxToEm()),
    paddingBottom(10.pxToEm())
  )

  val list: StyleA = style(display.inlineBlock, margin(-5.pxToEm()))

  val item: StyleA = style(display.inlineBlock, margin(5.pxToEm()), verticalAlign.middle)

  val button: StyleA = style(
    display.inlineBlock,
    verticalAlign.middle,
    width(30.pxToEm()),
    height(30.pxToEm()),
    borderRadius(50.%%),
    overflow.hidden,
    boxShadow := s"0 0 0 0 rgba(0, 0, 0, .0)",
    transition := "box-shadow .2s ease-in-out",
    &.before(
      display.inlineBlock,
      width(100.%%),
      fontSize(13.pxToEm()),
      lineHeight(30.pxToEm(13)),
      ThemeStyles.Font.fontAwesome,
      textAlign.center,
      color(ThemeStyles.TextColor.white)
    ),
    &.hover(boxShadow := s"0 1px 1px 0 rgba(0, 0, 0, .5)"),
    ThemeStyles.MediaQueries
      .beyondSmall(width(40.pxToEm()), height(40.pxToEm()), &.before(fontSize(18.pxToEm()), lineHeight(40.pxToEm(18))))
  )

  val shareWithFacebookButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.facebook), &.before(content := "'\\f09a'"))
  val shareWithTwitterButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.twitter), &.before(content := "'\\f099'"))
  val shareWithGooglePlusButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.googlePlus), &.before(content := "'\\f0d5'"))
  val shareWithLinkedInButton: StyleA =
    style(backgroundColor(ThemeStyles.SocialNetworksColor.linkedIn), &.before(content := "'\\f0e1'"))

}
