package org.make.front.styles

import org.make.front.Main.CssSettings._
import scalacss.internal.{AV, Media, ValueT}

object OperationStyles extends StyleSheet.Inline {

  object Font {
    val tradeGothicLTStd: AV = ThemeStyles.Font.tradeGothicLTStd
    val playfairDisplayItalic: AV = ThemeStyles.Font.playfairDisplayItalic
    val circularStdBook: AV = ThemeStyles.Font.circularStdBook
    val circularStdBold: AV = ThemeStyles.Font.circularStdBold
    val fontAwesome: AV = ThemeStyles.Font.fontAwesome
  }

  object OperationColor {
    val primary: ValueT[ValueT.Color] = ThemeStyles.ThemeColor.primary
    val secondary: ValueT[ValueT.Color] = ThemeStyles.ThemeColor.secondary
    val negative: ValueT[ValueT.Color] = ThemeStyles.ThemeColor.negative
    val positive: ValueT[ValueT.Color] = ThemeStyles.ThemeColor.positive
    val assertive: ValueT[ValueT.Color] = ThemeStyles.ThemeColor.assertive
    val prominent: ValueT[ValueT.Color] = ThemeStyles.ThemeColor.prominent
  }

  object SocialNetworksColor {
    val facebook: ValueT[ValueT.Color] = ThemeStyles.SocialNetworksColor.facebook
    val twitter: ValueT[ValueT.Color] = ThemeStyles.SocialNetworksColor.twitter
    val googlePlus: ValueT[ValueT.Color] = ThemeStyles.SocialNetworksColor.googlePlus
    val linkedIn: ValueT[ValueT.Color] = ThemeStyles.SocialNetworksColor.linkedIn
  }

  object TextColor {
    val base: ValueT[ValueT.Color] = ThemeStyles.TextColor.base
    val light: ValueT[ValueT.Color] = ThemeStyles.TextColor.light
    val lighter: ValueT[ValueT.Color] = ThemeStyles.TextColor.lighter
    val veryLight: ValueT[ValueT.Color] = ThemeStyles.TextColor.veryLight
    val white: ValueT[ValueT.Color] = ThemeStyles.TextColor.white
    val grey: ValueT[ValueT.Color] = ThemeStyles.TextColor.grey
    val danger: ValueT[ValueT.Color] = ThemeStyles.TextColor.danger
  }

  object BackgroundColor {
    val white: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.white
    val lightGrey: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.lightGrey
    val lightBlueGrey: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.lightBlueGrey
    val grey: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.grey
    val darkGrey: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.grey
    val black: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.black
    val blackTransparent: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.blackTransparent
    val blackMoreTransparent: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.blackMoreTransparent
    val blackVeryTransparent: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.blackVeryTransparent
    val danger: ValueT[ValueT.Color] = ThemeStyles.BackgroundColor.danger
  }

  object BorderColor {
    val white: ValueT[ValueT.Color] = ThemeStyles.BorderColor.white
    val base: ValueT[ValueT.Color] = ThemeStyles.BorderColor.base
    val light: ValueT[ValueT.Color] = ThemeStyles.BorderColor.light
    val lighter: ValueT[ValueT.Color] = ThemeStyles.BorderColor.lighter
    val veryLight: ValueT[ValueT.Color] = ThemeStyles.BorderColor.veryLight
    val danger: ValueT[ValueT.Color] = ThemeStyles.BorderColor.danger
  }

  /**TODO: functions with baseText value for em convertion**/
  object SpacingValue {
    val smaller: Int = ThemeStyles.SpacingValue.smaller
    val small: Int = ThemeStyles.SpacingValue.small
    val medium: Int = ThemeStyles.SpacingValue.medium
    val largerMedium: Int = ThemeStyles.SpacingValue.largerMedium
    val large: Int = ThemeStyles.SpacingValue.large
    val larger: Int = ThemeStyles.SpacingValue.larger
    val evenLarger: Int = ThemeStyles.SpacingValue.evenLarger
  }

  object MediaQueries {
    val belowVerySmall: Media.Query = ThemeStyles.MediaQueries.belowVerySmall
    val beyondVerySmall: Media.Query = ThemeStyles.MediaQueries.beyondVerySmall
    val verySmall: Media.Query = ThemeStyles.MediaQueries.verySmall

    val belowSmall: Media.Query = ThemeStyles.MediaQueries.belowSmall
    val beyondSmall: Media.Query = ThemeStyles.MediaQueries.belowSmall
    val small: Media.Query = ThemeStyles.MediaQueries.small

    val belowMedium: Media.Query = ThemeStyles.MediaQueries.belowMedium
    val beyondMedium: Media.Query = ThemeStyles.MediaQueries.beyondMedium
    val medium: Media.Query = ThemeStyles.MediaQueries.medium

    val beyondLargeMedium: Media.Query = ThemeStyles.MediaQueries.beyondLargeMedium

    val belowLarge: Media.Query = ThemeStyles.MediaQueries.belowLarge
    val beyondLarge: Media.Query = ThemeStyles.MediaQueries.beyondLarge
  }

  val containerMaxWidth: ValueT[ValueT.LenPct] = ThemeStyles.containerMaxWidth
  val modalMaxWidth: ValueT[ValueT.LenPct] = ThemeStyles.modalMaxWidth

  val mainNavDefaultHeight: ValueT[ValueT.LenPct] = ThemeStyles.mainNavDefaultHeight

}
