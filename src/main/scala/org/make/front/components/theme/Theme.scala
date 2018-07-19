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

package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.politicalActions.PoliticalActionsContainer.PoliticalActionsContainerProps
import org.make.front.components.theme.ResultsInThemeContainer.ResultsInThemeContainerProps
import org.make.front.components.theme.ThemeHeader.ThemeHeaderProps
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.RWDHideRulesStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object Theme {

  final case class ThemeProps(theme: TranslatedThemeModel,
                              maybeOperation: Option[OperationModel],
                              maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[ThemeProps, Unit](
        displayName = "Theme",
        componentDidMount = { self =>
          TrackingService
            .track(
              eventName = "display-page-theme",
              trackingContext = TrackingContext(TrackingLocation.themePage),
              parameters = Map.empty,
              internalOnlyParameters = Map("themeId" -> self.props.wrapped.theme.id.value)
            )
        },
        render = (self) => {
          <("theme")()(
            <.div(^.className := ThemePageStyles.mainHeaderWrapper)(
              <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
              <.div(^.className := ThemePageStyles.fixedMainHeaderWrapper)(
                <.CookieAlertContainerComponent.empty,
                <.MainHeaderContainer.empty
              )
            ),
            <.ThemeHeaderComponent(^.wrapped := ThemeHeaderProps(self.props.wrapped.theme))(),
            <.div(^.className := ThemePageStyles.contentWrapper)(
              <.PoliticalActionsContainerComponent(
                ^.wrapped := PoliticalActionsContainerProps(Some(self.props.wrapped.theme))
              )(),
              <.ResultsInThemeContainerComponent(
                ^.wrapped := ResultsInThemeContainerProps(
                  currentTheme = self.props.wrapped.theme,
                  maybeOperation = self.props.wrapped.maybeOperation,
                  maybeLocation = self.props.wrapped.maybeLocation
                )
              )(),
              <.NavInThemesContainerComponent.empty
            ),
            <.MainFooterComponent.empty,
            <.style()(ThemePageStyles.render[String])
          )
        }
      )
}

object ThemePageStyles extends StyleSheet.Inline {

  import dsl._

  val mainHeaderWrapper: StyleA = style(
    paddingBottom(50.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
  )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val contentWrapper: StyleA =
    style(
      display.block,
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(ThemeStyles.SpacingValue.medium.pxToEm())),
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )
}
