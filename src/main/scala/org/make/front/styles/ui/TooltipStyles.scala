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

package org.make.front.styles.ui

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._

object TooltipStyles extends StyleSheet.Inline {

  import dsl._

  val base: StyleA = style(
    position.absolute,
    zIndex(1),
    width(100.%%),
    padding(10.pxToEm()),
    textAlign.center,
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.BackgroundColor.darkGrey),
    &.after(
      content := "''",
      position.absolute
    )
  )

  val bottomPositioned: StyleA =
    style(
      base,
      top(100.%%),
      right(50.%%),
      bottom(auto),
      left(auto),
      transform := "translate(50%,0)",
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      &.after(
        right(50.%%),
        bottom(100.%%),
        borderBottom(5.pxToEm(), solid, ThemeStyles.BackgroundColor.darkGrey),
        borderRight(5.pxToEm(), solid, transparent),
        borderLeft(5.pxToEm(), solid, transparent),
        transform := "translate(50%,0)"
      )
    )

  val topPositioned: StyleA =
    style(
      base,
      top(auto),
      right(50.%%),
      bottom(100.%%),
      left(auto),
      transform := "translate(50%,0)",
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      &.after(
        right(50.%%),
        top(100.%%),
        borderTop(5.pxToEm(), solid, ThemeStyles.BackgroundColor.darkGrey),
        borderRight(5.pxToEm(), solid, transparent),
        borderLeft(5.pxToEm(), solid, transparent),
        transform := "translate(50%,0)"
      )
    )

  val rightPositioned: StyleA =
    style(
      base,
      top(auto),
      right(auto),
      bottom(50.%%),
      left(100.%%),
      transform := "translate(0,50%)",
      marginLeft(ThemeStyles.SpacingValue.smaller.pxToEm()),
      &.after(
        bottom(50.%%),
        right(100.%%),
        borderRight(5.pxToEm(), solid, ThemeStyles.BackgroundColor.darkGrey),
        borderTop(5.pxToEm(), solid, transparent),
        borderBottom(5.pxToEm(), solid, transparent),
        transform := "translate(0,50%)"
      )
    )

}
