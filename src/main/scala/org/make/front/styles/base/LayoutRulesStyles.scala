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

package org.make.front.styles.base

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._

object LayoutRulesStyles extends StyleSheet.Inline {

  import dsl._

  val row: StyleA = style(
    display.block,
    position.relative,
    paddingRight(ThemeStyles.SpacingValue.small.pxToEm()),
    paddingLeft(ThemeStyles.SpacingValue.small.pxToEm()),
    maxWidth(100.%%),
    ThemeStyles.MediaQueries.beyondSmall(
      paddingRight(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingLeft(ThemeStyles.SpacingValue.medium.pxToEm())
    )
  )

  val centeredRow: StyleA = style(
    row,
    ThemeStyles.MediaQueries
      .beyondLarge(maxWidth(ThemeStyles.containerMaxWidth), marginRight.auto, marginLeft.auto)
  )

  val narrowerCenteredRow: StyleA = style(
    row,
    ThemeStyles.MediaQueries
      .beyondLargeMedium(maxWidth(1000.pxToEm()), marginRight.auto, marginLeft.auto)
  )

  val evenNarrowerCenteredRow: StyleA = style(
    row,
    ThemeStyles.MediaQueries
      .beyondSmall(maxWidth(500.pxToEm()), marginRight.auto, marginLeft.auto)
  )

  val rowWithCols: StyleA = style(
    display.block,
    position.relative,
    paddingRight((ThemeStyles.SpacingValue.small / 2).pxToEm()),
    paddingLeft((ThemeStyles.SpacingValue.small / 2).pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(
      paddingRight(ThemeStyles.SpacingValue.small.pxToEm()),
      paddingLeft(ThemeStyles.SpacingValue.small.pxToEm())
    )
  )

  val centeredRowWithCols: StyleA = style(
    rowWithCols,
    ThemeStyles.MediaQueries
      .beyondLarge(maxWidth(ThemeStyles.containerMaxWidth), marginRight.auto, marginLeft.auto)
  )

  val narrowerCenteredRowWithCols: StyleA = style(
    rowWithCols,
    ThemeStyles.MediaQueries
      .beyondLarge(maxWidth(ThemeStyles.containerMaxWidth), marginRight.auto, marginLeft.auto)
  )

}

object ColRulesStyles extends StyleSheet.Inline {

  import dsl._

  val col: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.top,
      width(100.%%),
      paddingRight((ThemeStyles.SpacingValue.small / 2).pxToEm()),
      paddingLeft((ThemeStyles.SpacingValue.small / 2).pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(
        paddingRight(ThemeStyles.SpacingValue.small.pxToEm()),
        paddingLeft(ThemeStyles.SpacingValue.small.pxToEm())
      )
    )

  val colHalf: StyleA = style(width(50.%%))
  val colThird: StyleA = style(width(33.3333.%%))
  val colTwoThirds: StyleA = style(width(66.6666.%%))
  val colQuarter: StyleA = style(width(25.%%))

  val colHalfBeyondSmall: StyleA = style(ThemeStyles.MediaQueries.beyondSmall(width(50.%%)))
  val colThirdBeyondSmall: StyleA = style(ThemeStyles.MediaQueries.beyondSmall(width(33.3333.%%)))
  val colTwoThirdsBeyondSmall: StyleA = style(ThemeStyles.MediaQueries.beyondSmall(width(66.6666.%%)))
  val colQuarterBeyondSmall: StyleA = style(ThemeStyles.MediaQueries.beyondSmall(width(25.%%)))

  val colHalfBeyondMedium: StyleA = style(ThemeStyles.MediaQueries.beyondMedium(width(50.%%)))
  val colThirdBeyondMedium: StyleA = style(ThemeStyles.MediaQueries.beyondMedium(width(33.3333.%%)))
  val colTwoThirdsBeyondMedium: StyleA = style(ThemeStyles.MediaQueries.beyondMedium(width(66.6666.%%)))
  val colQuarterBeyondMedium: StyleA = style(ThemeStyles.MediaQueries.beyondMedium(width(25.%%)))

  val colHalfBeyondLarge: StyleA = style(ThemeStyles.MediaQueries.beyondLarge(width(50.%%)))
  val colThirdBeyondLarge: StyleA = style(ThemeStyles.MediaQueries.beyondLarge(width(33.3333.%%)))
  val colTwoThirdsBeyondLarge: StyleA = style(ThemeStyles.MediaQueries.beyondLarge(width(66.6666.%%)))
  val colQuarterBeyondLarge: StyleA = style(ThemeStyles.MediaQueries.beyondLarge(width(25.%%)))

}

object FlexLayoutStyles extends StyleSheet.Inline {
  import dsl._

  val fullHeightContent: StyleA =
    style(
      display.flex,
      flexFlow := "column",
      justifyContent.spaceBetween,
      minHeight :=! "calc(100vh - 3.125em)",
      ThemeStyles.MediaQueries.beyondSmall(minHeight :=! "calc(100vh - 5em)")
    )
}

object TableLayoutStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(display.table, width(100.%%))

  val fullHeightWrapper: StyleA =
    style(wrapper, height(100.%%))

  val row: StyleA =
    style(display.tableRow)

  val fullHeightRow: StyleA =
    style(row, height(100.%%))

  val cell: StyleA =
    style(display.tableCell)

  val cellVerticalAlignMiddle: StyleA =
    style(cell, verticalAlign.middle)

  val cellVerticalAlignBottom: StyleA =
    style(cell, verticalAlign.bottom)
}

object TableLayoutBeyondSmallStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(display.table, width(100.%%)))

  val fullHeightWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(wrapper, height(100.%%)))

  val row: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(display.tableRow))

  val fullHeightRow: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(row, height(100.%%)))

  val cell: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(display.tableCell))

  val cellVerticalAlignMiddle: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(cell, verticalAlign.middle))

  val cellVerticalAlignBottom: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(cell, verticalAlign.bottom))
}

object TableLayoutBeyondMediumStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondMedium(display.table, width(100.%%)))

  val fullHeightWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(wrapper, height(100.%%)))

  val cell: StyleA =
    style(ThemeStyles.MediaQueries.beyondMedium(display.tableCell))

  val cellVerticalAlignMiddle: StyleA =
    style(ThemeStyles.MediaQueries.beyondMedium(cell, verticalAlign.middle))
}
