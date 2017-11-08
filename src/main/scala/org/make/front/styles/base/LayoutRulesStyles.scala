package org.make.front.styles.base

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._

import scalacss.internal.ValueT

object ColRulesStyles extends StyleSheet.Inline {

  import dsl._
  val gutter: ValueT[ValueT.LenPct] = ThemeStyles.SpacingValue.small.pxToEm()

  val col: StyleA =
    style(display.inlineBlock, verticalAlign.top, width(100.%%), paddingRight(gutter), paddingLeft(gutter))

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

object RowRulesStyles extends StyleSheet.Inline {

  import dsl._

  val gutter: ValueT[ValueT.LenPct] = ThemeStyles.SpacingValue.small.pxToEm()

  val row: StyleA = style(
    display.block,
    position.relative,
    ThemeStyles.MediaQueries.beyondSmall(paddingRight(gutter), paddingLeft(gutter))
  )

  val evenNarrowerCenteredRow: StyleA = style(
    row,
    ThemeStyles.MediaQueries
      .beyondSmall(maxWidth(500.pxToEm()), marginRight.auto, marginLeft.auto)
  )

  val narrowerCenteredRow: StyleA = style(
    row,
    ThemeStyles.MediaQueries
      .beyondLargeMedium(maxWidth(1000.pxToEm()), marginRight.auto, marginLeft.auto)
  )

  val centeredRow: StyleA = style(
    row,
    ThemeStyles.MediaQueries
      .beyondLarge(maxWidth(ThemeStyles.containerMaxWidth), marginRight.auto, marginLeft.auto)
  )
}
