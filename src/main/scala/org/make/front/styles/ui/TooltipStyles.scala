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
    right(50.%%),
    transform := "translateX(50%)",
    padding(10.pxToEm()),
    textAlign.center,
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.BackgroundColor.darkGrey),
    &.after(
      content := "''",
      position.absolute,
      right(50.%%),
      transform := "translateX(50%)",
      borderRight(5.pxToEm(), solid, transparent),
      borderLeft(5.pxToEm(), solid, transparent)
    )
  )

  val bottomPositioned: StyleA =
    style(
      base,
      top(100.%%),
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      &.after(bottom(100.%%), borderBottom(5.pxToEm(), solid, ThemeStyles.BackgroundColor.darkGrey))
    )

  val topPositioned: StyleA =
    style(
      base,
      bottom(100.%%),
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      &.after(top(100.%%), borderTop(5.pxToEm(), solid, ThemeStyles.BackgroundColor.darkGrey))
    )

}
