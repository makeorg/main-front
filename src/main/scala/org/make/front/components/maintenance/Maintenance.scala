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

package org.make.front.components.maintenance

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, RWDHideRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.utils._

import scala.scalajs.js

object Maintenance {

  lazy val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[Unit, Unit](
          displayName = "Maintenance",
          render = { self =>
            <("maintenance")()(
              <.div(^.className := js.Array(TableLayoutStyles.fullHeightWrapper))(
                <.div(^.className := TableLayoutStyles.row)(
                  <.div(^.className := js.Array(TableLayoutStyles.cell, MaintenanceStyles.mainHeaderWrapper))(
                    <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
                    <.div(^.className := MaintenanceStyles.fixedMainHeaderWrapper)(
                      <.CookieAlertContainerComponent.empty,
                      <.MainHeaderContainer.empty
                    )
                  )
                ),
                <.div(^.className := js.Array(TableLayoutStyles.row, MaintenanceStyles.fullHeight))(
                  <.div(^.className := js.Array(TableLayoutStyles.cell, MaintenanceStyles.articleCell))(
                    <.div(^.className := js.Array(LayoutRulesStyles.centeredRow, MaintenanceStyles.fullHeight))(
                      <.article(^.className := MaintenanceStyles.article)(
                        <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
                          <.div(
                            ^.className := js
                              .Array(TableLayoutStyles.cellVerticalAlignMiddle, MaintenanceStyles.articleInnerWrapper)
                          )(
                            <.div(^.className := LayoutRulesStyles.row)(
                              <.p(^.className := js.Array(MaintenanceStyles.intro, TextStyles.mediumIntro))(
                                unescape(I18n.t("maintenance.intro"))
                              ),
                              <.div(^.className := MaintenanceStyles.titleWrapper)(
                                <.h1(^.className := TextStyles.veryBigTitle)(unescape(I18n.t("maintenance.title")))
                              ),
                              <.div(^.className := MaintenanceStyles.textWrapper)(
                                <.p(
                                  ^.className := js.Array(MaintenanceStyles.text, TextStyles.mediumText),
                                  ^.dangerouslySetInnerHTML := I18n.t("maintenance.explanation-1")
                                )()
                              ),
                              <.div(^.className := MaintenanceStyles.textWrapper)(
                                <.p(
                                  ^.className := js.Array(MaintenanceStyles.text, TextStyles.mediumText),
                                  ^.dangerouslySetInnerHTML := I18n.t("maintenance.explanation-2")
                                )()
                              )
                            )
                          )
                        )
                      )
                    )
                  )
                )
              ),
              <.NavInThemesContainerComponent.empty,
              <.style()(MaintenanceStyles.render[String])
            )
          }
        )
    )
}

object MaintenanceStyles extends StyleSheet.Inline {

  import dsl._

  val fullHeight: StyleA =
    style(height(100.%%))

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

  val mainHeaderWrapper: StyleA =
    style(
      paddingBottom(50.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
    )

  val articleCell: StyleA =
    style(verticalAlign.middle, padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`))

  val article: StyleA =
    style(height(100.%%), backgroundColor(ThemeStyles.BackgroundColor.lightBlueGrey), textAlign.center)

  val articleInnerWrapper: StyleA =
    style(
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(
        paddingTop(ThemeStyles.SpacingValue.large.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.large.pxToEm())
      )
    )

  val intro: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val titleWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(ThemeStyles.SpacingValue.large.pxToEm()))
    )

  val textWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondMedium(
        marginTop(ThemeStyles.SpacingValue.medium.pxToEm()),
        marginBottom(ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val text: StyleA =
    style(color(ThemeStyles.TextColor.lighter))

}
