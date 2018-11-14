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
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.home.Explanations.ExplenationsProps
import org.make.front.components.home.FeaturedOperation.FeaturedOperationProps
import org.make.front.components.showcase.TrendingShowcaseContainer.TrendingShowcaseContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.Location
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, RWDHideRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object Home {

  final case class HomeProps(countryCode: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[HomeProps, Unit](
        displayName = "Home",
        componentDidMount = { _ =>
          TrackingService
            .track(eventName = "display-page-home", trackingContext = TrackingContext(TrackingLocation.homepage))
        },
        render = (self) => {
          <.div(^.className := HomeStyles.wrapper)(
            <.div(^.className := HomeStyles.mainHeaderWrapper)(
              <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
              <.div(^.className := HomeStyles.fixedMainHeaderWrapper)(
                <.CookieAlertContainerComponent.empty,
                <.MainHeaderContainer.empty
              )
            ),
            if (self.props.wrapped.countryCode == "FR") {
              <.section(^.className := LayoutRulesStyles.centeredRow)(
                <.header(^.className := HomeStyles.contentHeader)(
                  <.span(^.className := TextStyles.mediumTitle)(I18n.t("home.main-title")),
                  <.h1(^.className := js.Array(TextStyles.mediumTitle, HomeStyles.mainTitle))("Make.org")
                ),
                <.FeaturedOperationComponent(
                  ^.wrapped := FeaturedOperationProps(trackingLocation = TrackingLocation.homepage)
                )()
              )
            } else {
              <.h1(^.style := Map("display" -> "none"))("Make.org")
              <.WelcomeComponent.empty
            },
            <.FeaturedArticlesShowcaseContainerComponent.empty,
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "popular",
                intro = unescape(I18n.t("home.showcase-2.intro")),
                title = unescape(I18n.t("home.showcase-2.title")),
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.ExplanationsComponent(^.wrapped := ExplenationsProps(countryCode = self.props.wrapped.countryCode))(),
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "controversial",
                intro = unescape(I18n.t("home.showcase-3.intro")),
                title = unescape(I18n.t("home.showcase-3.title")),
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.ExtraOperationsContainerComponent.empty,
            <.MainFooterContainerComponent.empty,
            <.style()(HomeStyles.render[String])
          )
        }
      )
}

object HomeStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(unsafeChild("> section")(borderBottom(1.px, solid, ThemeStyles.BorderColor.white)))

  val mainHeaderWrapper: StyleA = style(
    paddingBottom(50.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
  )

  val mainTitle: StyleA =
    style(display.inline)

  val contentHeader: StyleA =
    style(padding(ThemeStyles.SpacingValue.medium.pxToEm(20), `0`, ThemeStyles.SpacingValue.small.pxToEm(20)))

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

}
