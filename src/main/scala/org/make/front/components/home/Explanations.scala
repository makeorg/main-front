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

package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object Explanations {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "Explanations",
        render = { _ =>
          val openTarget: () => Unit = () => {
            TrackingService.track(
              eventName = "click-button-whoweare",
              trackingContext = TrackingContext(TrackingLocation.showcaseHomepage)
            )
            scalajs.js.Dynamic.global.window.open(I18n.t("home.explanations.article-2.see-more-link"), "_blank")
          }
          <.section(^.className := ExplanationsStyles.wrapper)(
            <.div(^.className := js.Array(LayoutRulesStyles.centeredRowWithCols))(
              <.article(
                ^.className := js
                  .Array(ExplanationsStyles.article, ColRulesStyles.col, ColRulesStyles.colHalfBeyondMedium)
              )(
                <.h3(^.className := js.Array(ExplanationsStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                  unescape(I18n.t("home.explanations.article-1.intro"))
                ),
                <.h2(^.className := TextStyles.mediumTitle)(unescape(I18n.t("home.explanations.article-1.title"))),
                <.ul()(
                  <.li(^.className := ExplanationsStyles.item)(
                    <.span(^.className := js.Array(ExplanationsStyles.icon, FontAwesomeStyles.lightbulbTransparent))(),
                    <.p(
                      ^.className := TextStyles.mediumText,
                      ^.dangerouslySetInnerHTML := I18n.t("home.explanations.article-1.item-2")
                    )()
                  ),
                  <.li(^.className := ExplanationsStyles.item)(
                    <.span(^.className := js.Array(ExplanationsStyles.icon, FontAwesomeStyles.thumbsUp))(),
                    <.p(
                      ^.className := TextStyles.mediumText,
                      ^.dangerouslySetInnerHTML := I18n.t("home.explanations.article-1.item-1")
                    )()
                  ),
                  <.li(^.className := ExplanationsStyles.item)(
                    <.span(^.className := js.Array(ExplanationsStyles.icon, FontAwesomeStyles.group))(),
                    <.p(
                      ^.className := TextStyles.mediumText,
                      ^.dangerouslySetInnerHTML := I18n.t("home.explanations.article-1.item-3")
                    )()
                  )
                )
              ),
              <.article(
                ^.className := js.Array(
                  ExplanationsStyles.article,
                  ExplanationsStyles.secondArticle,
                  ColRulesStyles.col,
                  ColRulesStyles.colHalfBeyondMedium
                )
              )(
                <.h3(^.className := js.Array(ExplanationsStyles.intro, TextStyles.mediumText, TextStyles.intro))(
                  unescape(I18n.t("home.explanations.article-2.intro"))
                ),
                <.h2(^.className := TextStyles.mediumTitle)(unescape(I18n.t("home.explanations.article-2.title"))),
                <.p(^.className := js.Array(ExplanationsStyles.paragraph, TextStyles.mediumText))(
                  unescape(I18n.t("home.explanations.article-2.text"))
                ),
                <.p(^.className := WelcomeStyles.ctaWrapper)(
                  <.button(
                    ^.onClick := openTarget,
                    ^.className := js.Array(CTAStyles.basic, CTAStyles.negative, CTAStyles.basicOnButton)
                  )(unescape(I18n.t("home.explanations.article-2.see-more")))
                )
              )
            ),
            <.style()(ExplanationsStyles.render[String])
          )
        }
      )
}

object ExplanationsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      backgroundColor(ThemeStyles.ThemeColor.primary),
      color(ThemeStyles.TextColor.white)
    )

  val article: StyleA =
    style(
      ThemeStyles.MediaQueries.beyondMedium(
        paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.small.pxToEm())
      )
    )

  val secondArticle: StyleA =
    style(
      ThemeStyles.MediaQueries.belowMedium(
        &.before(
          content := "''",
          display.block,
          height(1.px),
          width(100.%%),
          marginTop(20.pxToEm()),
          marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
          backgroundColor(rgba(255, 255, 255, 0.6))
        )
      ),
      ThemeStyles.MediaQueries.beyondMedium(
        borderLeft(1.px, solid, rgba(255, 255, 255, 0.6)),
        paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val intro: StyleA =
    style(marginBottom(5.pxToEm(15)), ThemeStyles.MediaQueries.beyondSmall(marginBottom(5.pxToEm(18))))

  val item: StyleA = style(
    position.relative,
    display.inlineBlock,
    width(100.%%),
    paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm()),
    margin(ThemeStyles.SpacingValue.smaller.pxToEm(), `0`),
    opacity(0.7)
  )

  val icon: StyleA =
    style(
      position.absolute,
      left(`0`),
      top(3.pxToEm(15)),
      fontSize(15.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(top(3.pxToEm(18)), fontSize(18.pxToEm()))
    )

  val paragraph: StyleA =
    style(display.inlineBlock, width(100.%%), margin(ThemeStyles.SpacingValue.smaller.pxToEm(), `0`), opacity(0.7))

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()))

}
