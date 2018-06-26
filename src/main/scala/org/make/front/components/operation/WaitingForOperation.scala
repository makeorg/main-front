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
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{RWDHideRulesStyles, TableLayoutStyles}
import org.make.front.styles.utils._

import scala.scalajs.js

object WaitingForOperation {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "WaitingForOperation",
        render = (self) => {

          <.div(^.className := js.Array(WaitingForOperationStyles.wrapper, TableLayoutStyles.fullHeightWrapper))(
            <.div(^.className := TableLayoutStyles.row)(
              <.div(^.className := js.Array(TableLayoutStyles.cell, WaitingForOperationStyles.mainHeaderWrapper))(
                <.div(^.className := RWDHideRulesStyles.invisible)(<.CookieAlertContainerComponent.empty),
                <.div(^.className := WaitingForOperationStyles.fixedMainHeaderWrapper)(
                  <.CookieAlertContainerComponent.empty,
                  <.MainHeaderContainer.empty
                )
              )
            ),
            <.div(^.className := TableLayoutStyles.row)(
              <.div(
                ^.className := js.Array(WaitingForOperationStyles.content, TableLayoutStyles.cellVerticalAlignMiddle)
              )(<.SpinnerComponent.empty)
            ),
            <.style()(WaitingForOperationStyles.render[String])
          )
        }
      )
}

object WaitingForOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(tableLayout.fixed, backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val content: StyleA =
    style(
      height(100.%%),
      paddingBottom(ThemeStyles.SpacingValue.large.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.SpacingValue.evenLarger.pxToEm()))
    )

  val mainHeaderWrapper: StyleA =
    style(
      paddingBottom(50.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.mainNavDefaultHeight))
    )

  val fixedMainHeaderWrapper: StyleA =
    style(position.fixed, top(`0`), left(`0`), width(100.%%), zIndex(10), boxShadow := s"0 2px 4px 0 rgba(0,0,0,0.50)")

}
