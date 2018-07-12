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
import org.make.front.components.home.CultureFeaturedOperation.CultureFeaturedOperationProps
import org.make.front.components.showcase.ThemeShowcaseContainer.ThemeShowcaseContainerProps
import org.make.front.components.showcase.TrendingShowcaseContainer.TrendingShowcaseContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.Location
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.RWDHideRulesStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object Home {

  final case class HomeProps(countryCode: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[HomeProps, Unit](
        displayName = "Home",
        componentDidMount = { _ =>
          TrackingService.track("display-page-home", TrackingContext(TrackingLocation.homepage))
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
            <.h1(^.style := Map("display" -> "none"))("Make.org"),
            if (self.props.wrapped.countryCode == "FR") {
              <.CultureFeaturedOperationComponent(
                ^.wrapped := CultureFeaturedOperationProps(trackingLocation = TrackingLocation.homepage)
              )()
            } else {
              <.WelcomeComponent.empty
            },
            <.FeaturedArticlesShowcaseContainerComponent.empty,
            // Todo Product Team wanted to "hide" ThemeShowcase components, waiting for hoempage revamp
            /*<.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "democratie-vie-politique",
                maybeIntro = Some(unescape(I18n.t("home.showcase-1.intro"))),
                maybeNews = Some(I18n.t("home.showcase-1.news")),
                maybeOperation = None,
                maybeSequenceId = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),*/
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "popular",
                intro = unescape(I18n.t("home.showcase-2.intro")),
                title = unescape(I18n.t("home.showcase-2.title")),
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.ExplanationsComponent.empty,
            /*<.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "economie-emploi-travail",
                maybeOperation = None,
                maybeSequenceId = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),*/
            <.TrendingShowcaseContainerComponent(
              ^.wrapped := TrendingShowcaseContainerProps(
                trending = "controversial",
                intro = unescape(I18n.t("home.showcase-3.intro")),
                title = unescape(I18n.t("home.showcase-3.title")),
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            /*<.ThemeShowcaseContainerComponent(
              ^.wrapped := ThemeShowcaseContainerProps(
                themeSlug = "vivre-ensemble-solidarites",
                maybeOperation = None,
                maybeSequenceId = None,
                maybeLocation = Some(Location.Homepage)
              )
            )(),
            <.NavInThemesContainerComponent.empty,*/
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

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

}
