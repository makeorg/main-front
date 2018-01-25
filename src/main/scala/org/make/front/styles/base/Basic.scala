package org.make.front.styles.base

import org.make.front.Main.CssSettings._
import org.make.front.facades._
import org.make.front.styles.ThemeStyles

import scalacss.internal.{Attr, FontFace}

object Basic extends StyleSheet.Inline {

  import dsl._

  val tradeGothicLTStd: FontFace[String] = fontFace("TradeGothicLTStdBdCn20")(
    _.src(
      s"url('${tradeGothicLTTtf.toString}')",
      s"url('${tradeGothicLTWoff2.toString}') format('woff2')",
      s"url('${tradeGothicLTWoff.toString}') format('woff')"
    ).fontStyle.normal.fontWeight.normal
  )

  val circularStdBook: FontFace[String] =
    fontFace("CircularStdBook")(
      _.src(
        s"url('${circularStdBookWoff2.toString}') format('woff2')",
        s"url('${circularStdBookWoff.toString}') format('woff')"
      ).fontStyle.normal.fontWeight.normal
    )

  val circularStdBold: FontFace[String] =
    fontFace("CircularStdBold")(
      _.src(
        s"url('${circularStdBoldWoff2.toString}') format('woff2')",
        s"url('${circularStdBoldWoff.toString}') format('woff')"
      ).fontStyle.normal.fontWeight.normal
    )

  val fontAwesome: FontFace[String] =
    fontFace("")(
      _.src(
        s"url('${fontAwesomeWoff2.toString}') format('woff2')",
        s"url('${fontAwesomeWoff.toString}') format('woff')"
      ).fontStyle.normal.fontWeight.normal
    )

  style(
    unsafeRoot("html")(height(100.%%), Attr.real("-webkit-font-smoothing") := "antialiased", overflowY.scroll),
    unsafeRoot("body")(height(100.%%), color(ThemeStyles.TextColor.base)),
    unsafeRoot("#make-app")(height(100.%%)),
    unsafeRoot("*")(
      boxSizing.borderBox,
      Attr.real("-moz-osx-font-smoothing") := "grayscale",
      Attr.real("-webkit-font-smoothing") := "antialiased",
      Attr.real("-webkit-tap-highlight-color") := "transparent"
    ),
    unsafeRoot("*:focus")(outline.none),
    unsafeRoot("*::-moz-focus-inner")(border(`0`)),
    unsafeRoot("input:required:invalid")(outline.none),
    unsafeRoot("::-moz-selection")(
      backgroundColor(ThemeStyles.ThemeColor.secondary),
      color(ThemeStyles.TextColor.white)
    ),
    unsafeRoot("::selection")(backgroundColor(ThemeStyles.ThemeColor.secondary), color(ThemeStyles.TextColor.white)),
    unsafeRoot("a")(textDecoration := "none", cursor.pointer, color.initial),
    unsafeRoot("img")(height.auto, maxHeight(100.%%), maxWidth(100.%%), verticalAlign.middle),
    unsafeRoot("svg")(maxWidth(100.%%)),
    unsafeRoot("select:-moz-focusring")(color.transparent, textShadow := "0 0 0 #000"),
    unsafeRoot("button, input[type=\"submit\"]")(Attr.real("-webkit-appearance") := "button", cursor.pointer),
    unsafeRoot("input")(Attr.real("-webkit-appearance") := "none", borderRadius(`0`)),
    unsafeRoot("[role=\"button\"], input[type=\"submit\"], input[type=\"reset\"], input[type=\"button\"], button")(
      boxSizing.contentBox,
      padding(`0`),
      margin(`0`),
      border(`0`),
      borderRadius(`0`),
      font := "inherit",
      textAlign.left,
      lineHeight.normal,
      background := "none",
      color.inherit,
      overflow.visible,
      userSelect := "none",
      cursor.pointer,
      textTransform.inherit
    ),
    unsafeRoot("input::-moz-focus-inner,button::-moz-focus-inner")(padding(`0`), border(`0`)),
    unsafeRoot("hr")(border(`0`), margin(`0`), height(`0`))
  )
}
