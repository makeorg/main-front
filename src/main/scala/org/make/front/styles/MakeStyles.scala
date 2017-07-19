package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.DevDefaults.StyleA
import scalacss.internal.{AV, Cond, Media, ValueT}

object MakeStyles extends StyleSheet.Inline {
  import dsl._

  // Responsiveness using Bulma standards: http://bulma.io/documentation/overview/responsiveness/
  val modeMobile: Cond = media.maxWidth(768.px) & media.handheld
  val modeTablet: Media.Query = media.minWidth(769.px)
  val modeDesktop: Media.Query = media.minWidth(1008.px)
  val modeWidescreen: Media.Query = media.minWidth(1200.px)
  val modeFullhd: Media.Query = media.minWidth(1392.px)

  val modeTabletOnly: Media.Query = modeTablet.maxWidth(1008.px)
  val modeDesktopOnly: Media.Query = modeDesktop.maxWidth(1200.px)
  val modeWidescreenOnly: Media.Query = modeWidescreen.maxWidth(1392.px)

  //todo: implement h1
  val title1: StyleA = style()
  val title2: StyleA = style(fontSize(3.4.rem), MakeStyles.Font.tradeGothicLTStd, textTransform.uppercase)
  //todo: implement h3
  val title3: StyleA = style()

  val inputTextFocused: StyleA = style(borderColor(c"#3898EC"), outline(none))

  val inputText: StyleA = style(
    height(4.rem),
    borderRadius(4.rem),
    border(1.px, solid, c"#CCC"),
    backgroundColor(c"#F7F7F7"),
    paddingLeft(3.5F.rem),
    paddingRight(2.rem),
    &.focus(inputTextFocused)
  )

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
    val grey: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.3)
    val pink: ValueT[ValueT.Color] = c"#ed1844"
    val white: ValueT[ValueT.Color] = rgb(255, 255, 255)
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
