package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{AV, Length, Media, ValueT}

object ThemeStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  object Font {
    val tradeGothicLTStd: AV = fontFamily :=! "TradeGothicLTStdBdCn20"
    val playfairDisplayItalic: AV = fontFamily :=! "Playfair Display"
    val circularStdBook: AV = fontFamily :=! "CircularStdBook"
    val circularStdBold: AV = fontFamily :=! "CircularStdBold"
    val fontAwesome: AV = fontFamily :=! "FontAwesome"
  }

  object ThemeColor {
    val primary: ValueT[ValueT.Color] = rgb(237, 24, 68)
    val secondary: ValueT[ValueT.Color] = rgb(37, 49, 134)
    val negative: ValueT[ValueT.Color] = rgb(218, 0, 27)
    val positive: ValueT[ValueT.Color] = rgb(110, 182, 31)
    val assertive: ValueT[ValueT.Color] = rgb(218, 0, 27)
  }

  object TextColor {
    val base: ValueT[ValueT.Color] = rgb(0, 0, 0)
    val light: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.5)
    val lighter: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.3)
    val white: ValueT[ValueT.Color] = rgb(255, 255, 255)
    val grey: ValueT[ValueT.Color] = rgb(155, 155, 155)
    val danger: ValueT[ValueT.Color] = ThemeColor.negative
  }

  object BackgroundColor {
    val white: ValueT[ValueT.Color] = rgb(255, 255, 255)
    val lightGrey: ValueT[ValueT.Color] = rgb(247, 247, 247)
    val grey: ValueT[ValueT.Color] = rgb(231, 231, 231)
    val black: ValueT[ValueT.Color] = rgb(0, 0, 0)
    val blackTransparent: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.2)
    val blackVeryTransparent: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
    val danger: ValueT[ValueT.Color] = rgba(208, 2, 27, 0.1)
  }

  object BorderColor {
    val base: ValueT[ValueT.Color] = rgb(155, 155, 155)
    val light: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.5)
    val lighter: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.3)
    val veryLight: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.1)
    val danger: ValueT[ValueT.Color] = rgba(208, 2, 27, 1)
  }

  /**TODO: functions with baseText value for em convertion**/
  object SpacingValue {
    val smaller: Int = 10
    val small: Int = 15
    val medium: Int = 30
    val largerMedium: Int = 40
    val large: Int = 50
    val larger: Int = 60
  }

  object MediaQueries {

    val belowVerySmall: Media.Query = media.maxWidth(399.pxToEm())
    val beyondVerySmall: Media.Query = media.minWidth(400.pxToEm())
    val verySmall: Media.Query = belowVerySmall.maxWidth(499.pxToEm())

    val belowSmall: Media.Query = media.maxWidth(499.pxToEm())
    val beyondSmall: Media.Query = media.minWidth(500.pxToEm())
    val small: Media.Query = beyondSmall.maxWidth(839.pxToEm())

    val belowMedium: Media.Query = media.maxWidth(839.pxToEm())
    val beyondMedium: Media.Query = media.minWidth(840.pxToEm())
    val medium: Media.Query = beyondMedium.maxWidth(1199.pxToEm())

    val belowLarge: Media.Query = media.maxWidth(1199.pxToEm())
    val beyondLarge: Media.Query = media.minWidth(1200.pxToEm())
  }

  val containerMaxWidth: ValueT[ValueT.LenPct] = 1200.pxToEm()
  val modalMaxWidth: ValueT[ValueT.LenPct] = 730.pxToEm()

  val mainNavDefaultHeight: ValueT[ValueT.LenPct] = 80.pxToEm()

}
