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
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object Welcome {

  lazy val reactClass: ReactClass = React.createClass[Unit, Unit](
    displayName = "Welcome",
    render = { _ =>
      def onclick: () => Unit = { () =>
        TrackingService.track(eventName = "click-button-whoweare", trackingContext = TrackingContext(TrackingLocation.homepage))
        scalajs.js.Dynamic.global.window.open(I18n.t("welcome.intro.see-more-link"), "_blank")
      }
      <.section(^.className := js.Array(TableLayoutStyles.wrapper, WelcomeStyles.wrapper))(
        <.div(^.className := js.Array(TableLayoutStyles.cellVerticalAlignMiddle, WelcomeStyles.innerWrapper))(
          <.img(
            ^.className := WelcomeStyles.illustration,
            ^.src := welcome.toString,
            ^("srcset") := welcomeSmall.toString + " 400w, " + welcomeSmall2x.toString + " 800w, " + welcomeMedium.toString + " 840w, " + welcomeMedium2x.toString + " 1680w, " + welcome.toString + " 1350w, " + welcome2x.toString + " 2700w",
            ^.alt := "Make.org",
            ^("data-pin-no-hover") := "true"
          )(),
          <.div(^.className := js.Array(WelcomeStyles.innerSubWrapper, LayoutRulesStyles.centeredRow))(
            <.div(^.className := WelcomeStyles.labelWrapper)(
              <.p(^.className := TextStyles.label)(unescape(I18n.t("home.welcome.baseline")))
            ),
            <.h2(^.className := js.Array(WelcomeStyles.title, TextStyles.veryBigText, TextStyles.boldText))(
              unescape(I18n.t("home.welcome.title"))
            ),
            <.h3(^.className := js.Array(TextStyles.mediumText, WelcomeStyles.subTitle))(
              unescape(I18n.t("home.welcome.subtitle"))
            ),
            <.p(^.className := WelcomeStyles.ctaWrapper)(
              <.button(^.onClick := onclick, ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton))(
                unescape(I18n.t("home.welcome.see-more"))
              )
            ),
            <.style()(WelcomeStyles.render[String])
          )
        )
      )
    }
  )
}

object WelcomeStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative, height(440.pxToEm()), backgroundColor(ThemeStyles.BackgroundColor.black))

  val innerWrapper: StyleA =
    style(position.relative, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`), textAlign.center, overflow.hidden)

  val illustration: StyleA =
    style(
      position.absolute,
      top(`0`),
      left(50.%%),
      height.auto,
      maxHeight.none,
      minHeight(100.%%),
      width.auto,
      maxWidth.none,
      minWidth(100.%%),
      transform := s"translate(-50%, 0%)",
      opacity(0.5)
    )

  val innerSubWrapper: StyleA =
    style(position.relative, zIndex(1))

  val labelWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

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
