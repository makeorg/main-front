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
import org.make.front.facades.Unescape.unescape
import org.make.front.facades._
import org.make.front.styles._
import org.make.front.styles.base.{LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object FeaturedOperation {

  final case class FeaturedOperationProps(trackingLocation: TrackingLocation)

  lazy val reactClass: ReactClass =
    React
      .createClass[FeaturedOperationProps, Unit](
        displayName = "FeaturedOperation",
        render = (self) => {

          def learnMoreOnFeaturedOperation: () => Unit = { () =>
            TrackingService
              .track(
                eventName = "click-homepage-header",
                trackingContext = TrackingContext(self.props.wrapped.trackingLocation)
              )
            scalajs.js.Dynamic.global.window.location = I18n.t("home.featured-operation.learn-more.link")
          }

          <.article(
            ^.className := js
              .Array(TableLayoutStyles.cellVerticalAlignMiddle, FeaturedOperationStyles.wrapper)
          )(
            <.img(
              ^.className := FeaturedOperationStyles.illustration,
              ^.src := featuredOperationLarge.toString,
              ^("srcset") := featuredOperationSmall.toString + " 500w, " + featuredOperationMedium.toString + " 840w, " + featuredOperationLarge.toString + " 1000w, ",
              ^.alt := I18n.t("home.featured-operation.intro.title"),
              ^("data-pin-no-hover") := "true"
            )(),
            <.div(^.className := js.Array(FeaturedOperationStyles.innerSubWrapper, LayoutRulesStyles.centeredRow))(
              <.div(^.className := FeaturedOperationStyles.labelWrapper)(
                <.p(^.className := TextStyles.label)(unescape(I18n.t("home.featured-operation.label")))
              ),
              <.h2(
                ^.className := js
                  .Array(FeaturedOperationStyles.title, TextStyles.veryBigText, TextStyles.boldText)
              )(unescape(I18n.t("home.featured-operation.title"))),
              <.h3(^.className := js.Array(TextStyles.smallText, FeaturedOperationStyles.subTitle))(
                unescape(I18n.t("home.featured-operation.purpose"))
              ),
              <.p(^.className := FeaturedOperationStyles.ctaWrapper)(
                <.button(
                  ^.onClick := learnMoreOnFeaturedOperation,
                  ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton)
                )(unescape(I18n.t("home.featured-operation.learn-more.label")))
              ),
              <.style()(FeaturedOperationStyles.render[String])
            )
          )
        }
      )
}

object FeaturedOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      position.relative,
      textAlign.center,
      overflow.hidden,
      display.flex,
      flexFlow := "column",
      justifyContent.flexEnd,
      height(375.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(height(440.pxToEm()))
    )

  val illustration: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      height(100.%%),
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, -50%)"
    )

  val innerSubWrapper: StyleA =
    style(
      position.relative,
      zIndex(1),
      marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(40.pxToEm()))
    )

  val labelWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val title: StyleA =
    style(color(ThemeStyles.TextColor.white), textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)")

  val subTitle: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      color(ThemeStyles.TextColor.white),
      textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)",
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()))
    )

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

}
