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

package org.make.front.components.currentOperations

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.styles.utils._

import scala.scalajs.js

object WaitingForCurrentOperations {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "WaitingForCurrentOperations",
        render = _ => {

          <.div(^.className := js.Array(TableLayoutStyles.row, WaitingForCurrentOperationsStyles.wrapper))(
            <.div(
              ^.className := js
                .Array(WaitingForCurrentOperationsStyles.content, TableLayoutStyles.cellVerticalAlignMiddle)
            )(<.SpinnerComponent.empty),
            <.style()(WaitingForCurrentOperationsStyles.render[String])
          )
        }
      )
}

object WaitingForCurrentOperationsStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent))

  val content: StyleA =
    style(
      height(100.%%),
      paddingBottom(ThemeStyles.SpacingValue.large.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.SpacingValue.evenLarger.pxToEm()))
    )
}
