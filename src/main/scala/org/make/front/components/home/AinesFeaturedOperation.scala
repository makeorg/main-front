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

object AinesFeaturedOperation {

  final case class AinesFeaturedOperationProps(trackingLocation: TrackingLocation)

  lazy val reactClass: ReactClass =
    React
      .createClass[AinesFeaturedOperationProps, Unit](
        displayName = "FeaturedOperation",
        render = (self) => {

          def learnMoreOnFeaturedOperation: () => Unit = { () =>
            TrackingService
              .track(
                eventName = "click-homepage-header",
                trackingContext = TrackingContext(self.props.wrapped.trackingLocation, Some("aines"))
              )
            scalajs.js.Dynamic.global.window.location = I18n.t("home.featured-operation.aines.learn-more.link")
          }

          <.section(^.className := js.Array(TableLayoutStyles.wrapper, AinesFeaturedOperationStyles.wrapper))(
            <.div(
              ^.className := js
                .Array(TableLayoutStyles.cellVerticalAlignMiddle, AinesFeaturedOperationStyles.innerWrapper)
            )(
              <.img(
                ^.className := AinesFeaturedOperationStyles.illustration,
                ^.src := ainesLarge.toString,
                ^("srcset") := ainesSmall.toString + " 400w, " + ainesSmall2x.toString + " 800w, " + ainesMedium.toString + " 840w, " + ainesMedium2x.toString + " 1680w, " + ainesLarge.toString + " 1350w, " + ainesLarge2x.toString + " 2700w",
                ^.alt := I18n.t("home.featured-operation.aines.intro.title"),
                ^("data-pin-no-hover") := "true"
              )(),
              <.div(
                ^.className := js.Array(AinesFeaturedOperationStyles.innerSubWrapper, LayoutRulesStyles.centeredRow)
              )(
                <.div(^.className := AinesFeaturedOperationStyles.labelWrapper)(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("home.featured-operation.aines.label")))
                ),
                <.h2(
                  ^.className := js
                    .Array(AinesFeaturedOperationStyles.title, TextStyles.veryBigText, TextStyles.boldText)
                )(unescape(I18n.t("home.featured-operation.aines.title"))),
                <.h3(^.className := js.Array(TextStyles.mediumText, AinesFeaturedOperationStyles.subTitle))(
                  unescape(I18n.t("home.featured-operation.aines.purpose"))
                ),
                <.p(^.className := AinesFeaturedOperationStyles.ctaWrapper)(
                  <.button(
                    ^.onClick := learnMoreOnFeaturedOperation,
                    ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton)
                  )(unescape(I18n.t("home.featured-operation.aines.learn-more.label")))
                ),
                <.style()(AinesFeaturedOperationStyles.render[String])
              )
            )
          )
        }
      )
}

object AinesFeaturedOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, height(440.pxToEm()), background := s"black")

  val innerWrapper: StyleA =
    style(position.relative, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`), textAlign.center, overflow.hidden)

  val illustration: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      height.auto,
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, -50%)",
      opacity(0.85)
    )

  val innerSubWrapper: StyleA =
    style(position.relative, zIndex(1))

  val labelWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.larger.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val title: StyleA =
    style(color(ThemeStyles.TextColor.white), textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)")

  val subTitle: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      color(ThemeStyles.TextColor.white),
      textShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)"
    )

  val ctaWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

}
