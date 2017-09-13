package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{Length, StyleA, ValueT}

object LayoutRulesStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val gutter: ValueT[ValueT.LenPct] = ThemeStyles.Spacing.small

  val col: StyleA =
    style(display.inlineBlock, verticalAlign.top, width(100.%%), paddingRight(gutter), paddingLeft(gutter))

  val colHalf: StyleA = style(width(50.%%))
  val colThird: StyleA = style(width(33.3333.%%))
  val colQuarter: StyleA = style(width(25.%%))

  val colHalfBeyondSmall: StyleA = style(ThemeStyles.MediaQueries.beyondSmall(width(50.%%)))
  val colThirdBeyondSmall: StyleA = style(ThemeStyles.MediaQueries.beyondSmall(width(33.3333.%%)))
  val colQuarterBeyondSmall: StyleA = style(ThemeStyles.MediaQueries.beyondSmall(width(25.%%)))

  val colHalfBeyondMedium: StyleA = style(ThemeStyles.MediaQueries.beyondMedium(width(50.%%)))
  val colThirdBeyondMedium: StyleA = style(ThemeStyles.MediaQueries.beyondMedium(width(33.3333.%%)))
  val colQuarterBeyondMedium: StyleA = style(ThemeStyles.MediaQueries.beyondMedium(width(25.%%)))

  val colHalfBeyondLarge: StyleA = style(ThemeStyles.MediaQueries.beyondLarge(width(50.%%)))
  val colThirdBeyondLarge: StyleA = style(ThemeStyles.MediaQueries.beyondLarge(width(33.3333.%%)))
  val colQuarterBeyondLarge: StyleA = style(ThemeStyles.MediaQueries.beyondLarge(width(25.%%)))

  val row: StyleA = style(
    display.block,
    position.relative,
    ThemeStyles.MediaQueries.beyondSmall(paddingRight(gutter), paddingLeft(gutter))
  )

  val centeredRow: StyleA = style(
    row,
    ThemeStyles.MediaQueries
      .beyondLarge(maxWidth(ThemeStyles.containerMaxWidth), marginRight.auto, marginLeft.auto)
  )

}
