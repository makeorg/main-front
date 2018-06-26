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

package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles._
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.utils._

import scala.scalajs.js

object ShareOwnProposal {

  final case class ShareOwnProposalProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    React
      .createClass[ShareOwnProposalProps, Unit](
        displayName = "ShareOwnProposal",
        render = (self) => {

          <.div(^.className := TableLayoutStyles.wrapper)(
            <.h4(^.className := js.Array(TableLayoutStyles.cell, TextStyles.smallText, ShareOwnProposalStyles.intro))(
              unescape(I18n.t("proposal.created-by-user"))
            ),
            /*<.div(^.className := TableLayoutStyles.cell)(
              <.ul(^.className := ShareOwnProposalStyles.shareButtonsList)(
                <.li(^.className := ShareOwnProposalStyles.shareButtonItem)(
                  <.button(
                    ^.className := js.Array(
                      ShareOwnProposalStyles.shareButton,
                      ShareOwnProposalStyles.shareWithFacebookButton
                    )
                  )()
                ),
                <.li(^.className := ShareOwnProposalStyles.shareButtonItem)(
                  <.button(
                    ^.className := js.Array(
                      ShareOwnProposalStyles.shareButton,
                      ShareOwnProposalStyles.shareWithTwitterButton
                    )
                  )()
                ),
                <.li(^.className := ShareOwnProposalStyles.shareButtonItem)(
                  <.button(
                    ^.className := js.Array(
                      ShareOwnProposalStyles.shareButton,
                      ShareOwnProposalStyles.shareWithGooglePlusButton
                    )
                  )()
                ),
                <.li(^.className := ShareOwnProposalStyles.shareButtonItem)(
                  <.button(
                    ^.className := js.Array(
                      ShareOwnProposalStyles.shareButton,
                      ShareOwnProposalStyles.shareWithLinkedInButton
                    )
                  )()
                )
              )
            ),*/
            <.style()(ShareOwnProposalStyles.render[String])
          )
        }
      )
}

object ShareOwnProposalStyles extends StyleSheet.Inline {

  import dsl._

  val intro: StyleA =
    style(color(ThemeStyles.ThemeColor.prominent))

  val shareButtonsList: StyleA = style(textAlign.right, margin(-2.px))

  val shareButtonItem: StyleA = style(display.inlineBlock, margin(2.px), verticalAlign.middle)

  val shareButton: StyleA = style(
    display.inlineBlock,
    verticalAlign.middle,
    width(24.pxToEm()),
    height(24.pxToEm()),
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
      color(ThemeStyles.TextColor.white)
    ),
    &.hover(boxShadow := s"0 ${1.pxToEm().value} ${1.pxToEm().value} 0 rgba(0, 0, 0, .5)")
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
