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

package org.make.front.styles

import org.make.front.Main.CssSettings._
import org.make.front.styles.utils._

import scalacss.internal.{AV, Media, ValueT}

object ThemeStyles extends StyleSheet.Inline {

  import dsl._

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
    val prominent: ValueT[ValueT.Color] = rgb(255, 110, 0)
  }

  object SocialNetworksColor {
    val facebook: ValueT[ValueT.Color] = rgb(58, 89, 152)
    val twitter: ValueT[ValueT.Color] = rgb(26, 145, 218)
    val googlePlus: ValueT[ValueT.Color] = rgb(219, 68, 55)
    val linkedIn: ValueT[ValueT.Color] = rgb(0, 119, 181)
  }

  object TextColor {
    val base: ValueT[ValueT.Color] = rgb(0, 0, 0)
    val light: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.5)
    val lighter: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.3)
    val veryLight: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
    val white: ValueT[ValueT.Color] = rgb(255, 255, 255)
    val grey: ValueT[ValueT.Color] = rgb(155, 155, 155)
    val danger: ValueT[ValueT.Color] = ThemeColor.negative
  }

  object BackgroundColor {
    val white: ValueT[ValueT.Color] = rgb(255, 255, 255)
    val lightGrey: ValueT[ValueT.Color] = rgb(242, 242, 242)
    val lightBlueGrey: ValueT[ValueT.Color] = rgb(230, 230, 240)
    val grey: ValueT[ValueT.Color] = rgb(231, 231, 231)
    val altGrey: ValueT[ValueT.Color] = rgb(218, 218, 218)
    val darkGrey: ValueT[ValueT.Color] = rgb(51, 51, 51)
    val black: ValueT[ValueT.Color] = rgb(0, 0, 0)
    val blackTransparent: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.2)
    val blackMoreTransparent: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.1)
    val blackVeryTransparent: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
    val danger: ValueT[ValueT.Color] = rgba(208, 2, 27, 0.1)
  }

  object BorderColor {
    val white: ValueT[ValueT.Color] = rgb(255, 255, 255)
    val base: ValueT[ValueT.Color] = rgb(155, 155, 155)
    val light: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.5)
    val lighter: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.3)
    val veryLight: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.1)
    val transparent: ValueT[ValueT.Color] = rgba(255, 255, 255, 0)
    val danger: ValueT[ValueT.Color] = rgba(208, 2, 27, 1)
  }

  object inputsSpecialColors {
    val fieldsBackground: ValueT[ValueT.Color] = rgb(247, 247, 247)
    val fieldsBorder: ValueT[ValueT.Color] = rgb(204, 204, 204)
  }

  /**TODO: functions with baseText value for em convertion**/
  object SpacingValue {
    val smaller: Int = 10
    val small: Int = 15
    val medium: Int = 30
    val largerMedium: Int = 40
    val large: Int = 50
    val larger: Int = 60
    val evenLarger: Int = 80
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

    val beyondLargeMedium: Media.Query = media.minWidth(1000.pxToEm())

    val belowLarge: Media.Query = media.maxWidth(1199.pxToEm())
    val beyondLarge: Media.Query = media.minWidth(1200.pxToEm())
  }

  val containerMaxWidth: ValueT[ValueT.LenPct] = 1200.pxToEm()
  val modalMaxWidth: ValueT[ValueT.LenPct] = 730.pxToEm()

  val mainNavDefaultHeight: ValueT[ValueT.LenPct] = SpacingValue.evenLarger.pxToEm()

}
