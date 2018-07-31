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
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.ReactShare._
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scala.scalajs.js

object ShareUserProfile {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "ConsultationShare",
        render = { self =>
          <.article(^.className := js.Array(ShareUserProfileStyles.wrapper, LayoutRulesStyles.centeredRow))(
            <.h3(^.className := ShareUserProfileStyles.title)(unescape(I18n.t("user-profile.share-profile"))),
            <.ul(^.className := ShareUserProfileStyles.list)(
              <.li(^.className := ShareUserProfileStyles.item)(
                <.FacebookShareButton()(
                  <.button(
                    ^.className := js
                      .Array(ShareUserProfileStyles.button, ShareUserProfileStyles.shareWithFacebookButton)
                  )()
                )
              ),
              <.li(^.className := ShareUserProfileStyles.item)(
                <.TwitterShareButton()(
                  <.button(
                    ^.className := js
                      .Array(ShareUserProfileStyles.button, ShareUserProfileStyles.shareWithTwitterButton)
                  )()
                )
              ),
              <.li(^.className := ShareUserProfileStyles.item)(
                <.GooglePlusShareButton()(
                  <.button(
                    ^.className := js
                      .Array(ShareUserProfileStyles.button, ShareUserProfileStyles.shareWithGooglePlusButton)
                  )()
                )
              ),
              <.li(^.className := ShareUserProfileStyles.item)(
                <.LinkedinShareButton()(
                  <.button(
                    ^.className := js
                      .Array(ShareUserProfileStyles.button, ShareUserProfileStyles.shareWithLinkedInButton)
                  )()
                )
              ),
              <.style()(ShareUserProfileStyles.render[String])
            ),
            <.style()(ShareUserProfileStyles.render[String])
          )
        }
      )

}

object ShareUserProfileStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(padding(20.pxToEm()), marginTop(20.pxToEm()))
    )

  val title: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      style(paddingBottom(ThemeStyles.SpacingValue.small.pxToEm(15))),
      ThemeStyles.MediaQueries.beyondSmall(
        fontSize(18.pxToEm()),
        paddingBottom(15.pxToEm(18)),
        marginBottom(20.pxToEm(18)),
        borderBottom(1.pxToEm(18), solid, ThemeStyles.BorderColor.veryLight)
      )
    )

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
