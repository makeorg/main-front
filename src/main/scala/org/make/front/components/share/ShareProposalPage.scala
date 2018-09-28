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
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.ReactShare._
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationExpanded, Proposal => ProposalModel}
import org.make.front.styles._
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.utils._
import org.scalajs.dom

import scala.scalajs.js

object ShareProposalPage {

  final case class ShareProposalProps(proposal: ProposalModel,
                                      operation: OperationExpanded,
                                      language: String,
                                      country: String)

  val reactClass: ReactClass =
    React
      .createClass[ShareProposalProps, Unit](
        displayName = "ShareProposal",
        render = self => {

          def url(network: String): String =
            s"${dom.window.location.origin}/${self.props.wrapped.country}/proposal/${self.props.wrapped.proposal.id.value}/${self.props.wrapped.proposal.slug}?utm_source=$network&utm_medium=user-share&utm_campaign=${self.props.wrapped.operation.slug}&utm_content=${self.props.wrapped.proposal.id.value}&shared_proposal=voted-proposal#/${self.props.wrapped.country}/proposal/${self.props.wrapped.proposal.slug}"

          <.div(^.className := js.Array(TableLayoutStyles.wrapper, ShareProposalStyles.wrapper))(
            <.h2(^.className := js.Array(TextStyles.verySmallTitle, ShareProposalStyles.title))(
              unescape(I18n.t("proposal.share-page"))
            ),
            <.ul(^.className := ShareProposalStyles.shareButtonsList)(
              <.li(^.className := ShareProposalStyles.shareButtonItem)(
                <.FacebookShareButton(^.url := url("Facebook"))(
                  <.button(
                    ^.className := js
                      .Array(ShareProposalStyles.shareButton, ShareProposalStyles.shareWithFacebookButton)
                  )()
                )
              ),
              <.li(^.className := ShareProposalStyles.shareButtonItem)(
                <.TwitterShareButton(^.url := url("Twitter"))(
                  <.button(
                    ^.className := js.Array(ShareProposalStyles.shareButton, ShareProposalStyles.shareWithTwitterButton)
                  )()
                )
              ),
              <.li(^.className := ShareProposalStyles.shareButtonItem)(
                <.GooglePlusShareButton(^.url := url("Google"))(
                  <.button(
                    ^.className := js
                      .Array(ShareProposalStyles.shareButton, ShareProposalStyles.shareWithGooglePlusButton)
                  )()
                )
              ),
              <.li(^.className := ShareProposalStyles.shareButtonItem)(
                <.LinkedinShareButton(^.url := url("Linkedin"))(
                  <.button(
                    ^.className := js
                      .Array(ShareProposalStyles.shareButton, ShareProposalStyles.shareWithLinkedInButton)
                  )()
                )
              )
            ),
            <.style()(ShareProposalStyles.render[String])
          )
        }
      )
}

object ShareProposalStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      marginTop(20.pxToEm()),
      height :=! s"calc(10% - ${20.pxToEm().value})",
      ThemeStyles.MediaQueries.beyondMedium(textAlign.center)
    )

  val title: StyleA =
    style(
      textAlign.center,
      color(ThemeStyles.TextColor.lighter),
      ThemeStyles.MediaQueries.beyondMedium(display.inlineBlock)
    )

  val shareButtonsList: StyleA =
    style(textAlign.center, ThemeStyles.MediaQueries.beyondMedium(display.inlineBlock, verticalAlign.middle))

  val shareButtonItem: StyleA =
    style(display.inlineBlock, verticalAlign.middle, marginLeft(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val shareButton: StyleA = style(
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
      fontSize(12.pxToEm()),
      lineHeight(24.pxToEm(12)),
      ThemeStyles.Font.fontAwesome,
      textAlign.center,
      color(ThemeStyles.TextColor.white),
      ThemeStyles.MediaQueries.beyondMedium(fontSize(18.pxToEm()), lineHeight(24.pxToEm(18)))
    ),
    &.hover(boxShadow := s"0 ${1.pxToEm().value} ${1.pxToEm().value} 0 rgba(0, 0, 0, .5)"),
    ThemeStyles.MediaQueries.beyondMedium(width(40.pxToEm()), height(40.pxToEm()))
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
