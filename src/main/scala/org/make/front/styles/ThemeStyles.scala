package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{AV, Length, Media, ValueT}

object ThemeStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
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
    val primary: ValueT[ValueT.Color] = rgba(237, 24, 68, 1)
  }

  object TextColor {
    val base: ValueT[ValueT.Color] = rgb(0, 0, 0)
    val light: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.5)
    val lighter: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.3)
    val white: ValueT[ValueT.Color] = rgb(255, 255, 255)
    val grey: ValueT[ValueT.Color] = rgb(155, 155, 155)
  }

  object BackgroundColor {
    val white: ValueT[ValueT.Color] = rgb(255, 255, 255)
    val lightGrey: ValueT[ValueT.Color] = rgb(247, 247, 247)
    val grey: ValueT[ValueT.Color] = rgb(231, 231, 231)
    val black: ValueT[ValueT.Color] = rgb(0, 0, 0)
    val blackTransparent: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
  }

  object BorderColor {
    val base: ValueT[ValueT.Color] = rgb(155, 155, 155)
    val light: ValueT[ValueT.Color] = rgb(204, 204, 204)
    val blackTransparent: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.01)
  }

  /**TODO: functions with baseText value for em convertion**/
  object Spacing {
    val smaller: ValueT[ValueT.LenPct] = 10.pxToEm()
    val small: ValueT[ValueT.LenPct] = 15.pxToEm()
    val medium: ValueT[ValueT.LenPct] = 30.pxToEm()
    val largerMedium: ValueT[ValueT.LenPct] = 40.pxToEm()
    val large: ValueT[ValueT.LenPct] = 50.pxToEm()
    val larger: ValueT[ValueT.LenPct] = 60.pxToEm()
  }

  object MediaQueries {
    val belowSmall: Media.Query = media.maxWidth(499.pxToEm(16))
    val beyondSmall: Media.Query = media.minWidth(500.pxToEm(16))
    val small: Media.Query = beyondSmall.maxWidth(839.pxToEm(16))

    val belowMedium: Media.Query = media.maxWidth(839.pxToEm(16))
    val beyondMedium: Media.Query = media.minWidth(840.pxToEm(16))
    val medium: Media.Query = beyondMedium.maxWidth(1199.pxToEm(16))

    val belowLarge: Media.Query = media.maxWidth(1199.pxToEm(16))
    val beyondLarge: Media.Query = media.minWidth(1200.pxToEm(16))
  }

  val containerMaxWidth: ValueT[ValueT.LenPct] = 1200.pxToEm()
  val modalMaxWidth: ValueT[ValueT.LenPct] = 730.pxToEm()

  val mainNavDefaultHeight: ValueT[ValueT.LenPct] = 80.pxToEm()

}
