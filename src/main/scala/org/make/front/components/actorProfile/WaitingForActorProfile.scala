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

package org.make.front.components.actorProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.styles.utils._

import scala.scalajs.js

object WaitingForActorProfile {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "WaitingForActorProfile",
        render = _ => {

          <.div(^.className := TableLayoutStyles.fullHeightWrapper)(
            <.div(
              ^.className := js
                .Array(WaitingForActorProfileStyles.content, TableLayoutStyles.cellVerticalAlignMiddle)
            )(<.SpinnerComponent.empty),
            <.style()(WaitingForActorProfileStyles.render[String])
          )
        }
      )
}

object WaitingForActorProfileStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA = style(tableLayout.fixed)
  val content: StyleA =
    style(
      height(100.%%),
      paddingBottom(ThemeStyles.SpacingValue.large.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(paddingBottom(ThemeStyles.SpacingValue.evenLarger.pxToEm()))
    )
}
