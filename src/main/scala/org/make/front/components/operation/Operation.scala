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

package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.operation.OperationHeader.OperationHeaderProps
import org.make.front.components.operation.ResultsInOperationContainer.ResultsInOperationContainerProps
import org.make.front.components.operation.intro.ChanceAuxJeunesOperationIntro.ChanceAuxJeunesOperationIntroProps
import org.make.front.components.operation.intro.ClimatParisOperationIntro.ClimatParisOperationIntroProps
import org.make.front.components.operation.intro.LPAEOperationIntro.LPAEOperationIntroProps
import org.make.front.components.operation.intro.MVEOperationIntro.MVEOperationIntroProps
import org.make.front.components.operation.intro.MakeEuropeOperationIntro.MakeEuropeOperationIntroProps
import org.make.front.components.operation.intro.VFFGBOperationIntro.VFFGBOperationIntroProps
import org.make.front.components.operation.intro.VFFITOperationIntro.VFFITOperationIntroProps
import org.make.front.components.operation.intro.VFFOperationIntro.VFFOperationIntroProps
import org.make.front.models.{Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.RWDHideRulesStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object Operation {

  final case class OperationProps(operation: OperationModel,
                                  countryCode: String,
                                  language: String,
                                  onWillMount: () => Unit)

  lazy val reactClass: ReactClass =
    React
      .createClass[OperationProps, Unit](
        displayName = "Operation",
        componentWillMount = { self =>
          self.props.wrapped.onWillMount()
        },
        componentDidMount = { self =>
          TrackingService
            .track(
              "display-page-operation",
              TrackingContext(TrackingLocation.operationPage, operationSlug = Some(self.props.wrapped.operation.slug)),
              Map.empty,
              Map("id" -> self.props.wrapped.operation.operationId.value)
            )
        },
        render = (self) => {
          val operation = self.props.wrapped.operation
          if (operation.isActive) {
            <("operation")()(
              <.div(^.className := OperationStyles.mainHeaderWrapper)(
                <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
                <.div(^.className := OperationStyles.fixedMainHeaderWrapper)(
                  <.CookieAlertContainerComponent.empty,
                  <.MainHeaderContainer.empty
                )
              ),
              <(operation.headerComponent)(^.wrapped := operation.headerProps(operation))(),
              <.OperationHeaderComponent(
                ^.wrapped := OperationHeaderProps(
                  operation,
                  maybeLocation = Some(LocationModel.OperationPage(operation.operationId)),
                  language = self.props.wrapped.language
                )
              )(),
              <.div(^.className := OperationStyles.contentWrapper)(
                <.ResultsInOperationContainerComponent(
                  ^.wrapped := ResultsInOperationContainerProps(
                    currentOperation = operation,
                    maybeLocation = Some(LocationModel.OperationPage(operation.operationId))
                  )
                )()
              ),
              <.style()(OperationStyles.render[String])
            )
          } else {
            <.div.empty
          }
        }
      )
}

object OperationStyles extends StyleSheet.Inline {

  import dsl._

  val mainHeaderWrapper: StyleA = style(
    paddingBottom(50.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
  )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val contentWrapper: StyleA =
    style(display.block, backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

}
