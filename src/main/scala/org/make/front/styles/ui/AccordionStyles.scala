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

object AccordionStyles extends StyleSheet.Inline {

  import dsl._

  val collapseTrigger: StyleA =
    style(width(100.%%))

  val collapseIcon: StyleA =
    style(float.right, transition := "all .25s ease-in-out")

  val collapseIconToggle: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(transform := s"rotate(0deg)")
      } else styleS(transform := s"rotate(90deg)")
  )

  val collapseWrapper: StyleA =
    style(overflow.hidden, transition := "opacity .25s ease-in-out")

  val collapseWrapperToggle: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(height(`0`), opacity(0))
      } else styleS(height.auto, opacity(1))
  )
}
