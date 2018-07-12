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

object MVEFeaturedOperation {

  final case class MVEFeaturedOperationProps(trackingLocation: TrackingLocation)

  lazy val reactClass: ReactClass =
    React
      .createClass[MVEFeaturedOperationProps, Unit](
        displayName = "FeaturedOperation",
        render = (self) => {

          def onclick: () => Unit = { () =>
            TrackingService
              .track(
                eventName = "click-homepage-header",
                trackingContext = TrackingContext(self.props.wrapped.trackingLocation, Some("mieux-vivre-ensemble"))
              )
            scalajs.js.Dynamic.global.window.open(I18n.t("home.featured-operation.mve.learn-more.link"), "_blank")
          }

          <.section(^.className := js.Array(TableLayoutStyles.wrapper, MVEFeaturedOperationStyles.wrapper))(
            <.div(
              ^.className := js
                .Array(TableLayoutStyles.cellVerticalAlignMiddle, MVEFeaturedOperationStyles.innerWrapper)
            )(
              <.img(
                ^.className := MVEFeaturedOperationStyles.illustration,
                ^.src := featuredMVEIll.toString,
                ^("srcset") := featuredMVEIllSmall.toString + " 400w, " + featuredMVEIllSmall2x.toString + " 800w, " + featuredMVEIll.toString + " 1200w, " + featuredMVEIll2x.toString + " 2400w",
                ^.alt := I18n.t("home.featured-operation.mve.title"),
                ^("data-pin-no-hover") := "true"
              )(),
              <.div(^.className := js.Array(MVEFeaturedOperationStyles.innerSubWrapper, LayoutRulesStyles.centeredRow))(
                <.div(^.className := MVEFeaturedOperationStyles.labelWrapper)(
                  <.p(^.className := TextStyles.label)(unescape(I18n.t("home.featured-operation.mve.label")))
                ),
                <.h2(
                  ^.className := js.Array(MVEFeaturedOperationStyles.title, TextStyles.veryBigText, TextStyles.boldText)
                )(unescape(I18n.t("home.featured-operation.mve.title"))),
                <.h3(^.className := js.Array(TextStyles.mediumText, MVEFeaturedOperationStyles.subTitle))(
                  unescape(I18n.t("home.featured-operation.mve.purpose"))
                ),
                <.p(^.className := MVEFeaturedOperationStyles.ctaWrapper)(
                  <.button(^.onClick := onclick, ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton))(
                    unescape(I18n.t("home.featured-operation.mve.learn-more.label"))
                  )
                ),
                <.style()(MVEFeaturedOperationStyles.render[String])
              )
            )
          )
        }
      )
}

object MVEFeaturedOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, height(440.pxToEm()), background := s"linear-gradient(130deg, #f6dee3, #d5e7ff)")

  val innerWrapper: StyleA =
    style(position.relative, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`), textAlign.center, overflow.hidden)

  val illustration: StyleA =
    style(
      position.absolute,
      bottom(`0`),
      left(50.%%),
      height(400.pxToEm()),
      maxHeight.none,
      width.auto,
      maxWidth.none,
      transform := s"translate(-50%, 0)"
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
