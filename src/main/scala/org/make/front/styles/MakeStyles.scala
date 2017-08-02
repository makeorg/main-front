package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.DevDefaults.StyleA
import scalacss.internal.{AV, ValueT}

object MakeStyles extends StyleSheet.Inline {
  import dsl._

  //todo: implement h1
  val title1: StyleA = style()
  val title2: StyleA = style(fontSize(3.4.rem), Font.tradeGothicLTStd, fontWeight.bold)
  //todo: implement h3
  val title3: StyleA = style()

  val heroTitle: StyleA = style(
    fontSize(6.rem),
    Font.tradeGothicLTStd,
    color(c"#FFF"),
    BulmaStyles.ResponsiveHelpers.block,
    textAlign.center,
    textShadow := "0.1rem 0.1rem 0.1rem #000",
    textTransform.uppercase
  )

  // colors
  object Color {
    val black: ValueT[ValueT.Color] = rgb(0, 0, 0)
    val pink: ValueT[ValueT.Color] = c"#ed1844"
  }

  // font
  object Font {
    val tradeGothicLTStd: AV = fontFamily :=! "TradeGothicLTStdBdCn20"
    val playfairDisplayRegular: AV = fontFamily :=! "PlayfairDisplayRegular"
    val playfairDisplayItalic: AV = fontFamily :=! "PlayfairDisplayItalic"
    val playfairDisplayBoldItalic: AV = fontFamily :=! "PlayfairDisplayBoldItalic"
    val playfairDisplayBold: AV = fontFamily :=! "PlayfairDisplayBold"
    val playfairDisplayBlackItalic: AV = fontFamily :=! "PlayfairDisplayBlackItalic"
    val playfairDisplayBlack: AV = fontFamily :=! "PlayfairDisplayBlack"
    val circularStdMedium: AV = fontFamily :=! "CircularStdMedium"
    val circularStdBook: AV = fontFamily :=! "CircularStdBook"
    val circularStdBold: AV = fontFamily :=! "CircularStdBold"
    val circularStdBlack: AV = fontFamily :=! "CircularStdBlack"
  }

  // background
  object Background {
    val footer: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
  }

  def gradientBackground(from: String, to: String): StyleA =
    style(background := s"linear-gradient(131deg, $from, $to)")
}
