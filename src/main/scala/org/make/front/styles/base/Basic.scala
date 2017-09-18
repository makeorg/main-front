package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{Attr}

object Basic extends StyleSheet.Inline {

  import dsl._

  val tradeGothicLTStd = fontFace("TradeGothicLTStdBdCn20")(
    _.src(
      "url(./fonts/TradeGothicLTStd-BdCn20.ttf)",
      "url('./fonts/TradeGothicLTStd-BdCn20.woff2') format('woff2')",
      "url('./fonts/TradeGothicLTStd-BdCn20.woff') format('woff')"
    ).fontStyle.normal.fontWeight.normal
  )

  val circularStdBook =
    fontFace("CircularStdBook")(
      _.src(
        "url('./fonts/CircularStd-Book.woff2') format('woff2')",
        "url('./fonts/CircularStd-Book.woff') format('woff')"
      ).fontStyle.normal.fontWeight.normal
    )

  val circularStdBold =
    fontFace("CircularStdBold")(
      _.src(
        "url(./fonts/CircularStd-Bold.woff2) format('woff2')",
        "url('./fonts/CircularStd-Bold.woff') format('woff')"
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
      font := "inherit",
      textAlign.left,
      lineHeight.normal,
      background := "none",
      color.inherit,
      overflow.visible,
      userSelect := "none",
      cursor.pointer
    ),
    unsafeRoot("input::-moz-focus-inner,button::-moz-focus-inner")(padding(`0`), border(`0`))
  )

}
