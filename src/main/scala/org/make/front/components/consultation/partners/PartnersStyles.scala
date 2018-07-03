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

package org.make.front.components.consultation.partners

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.ui.TooltipStyles
import org.make.front.styles.utils._
import scalacss.internal.{Attr, ValueT}

object PartnersStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingRight(ThemeStyles.SpacingValue.small.pxToEm()),
      &.after(
        Attr.real("content") := "''", clear.both, display.table
      ),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        paddingLeft(20.pxToEm()),
        paddingRight(20.pxToEm()),
      )
    )

  val BorderColor: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.5)

  val item: StyleA =
    style(
      position.relative,
      float.left,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      marginRight(14.pxToPercent(320)),
      ThemeStyles.MediaQueries.beyondLargeMedium(
        width(50.pxToPercent(320))
      )
    )

  val avatar: StyleA =
    style(
      width(50.pxToEm()),
      height(50.pxToEm()),
      borderRadius(50.pxToEm()),
      border(1.pxToEm(), solid, BorderColor),
      overflow.hidden,
      ThemeStyles.MediaQueries.beyondLargeMedium(
        width(100.%%),
        height.auto
      )
    )

  val tooltip: StyleA =
    style(
      TooltipStyles.topPositioned,
      minWidth(200.pxToEm()),
      transition := "opacity .25s ease-in-out"
    )

  val tooltipTrigger: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS(
          opacity(1),
          zIndex(1)
        )
      } else styleS(
        opacity(0),
        zIndex(-1)
      )
  )

}
