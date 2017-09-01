package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{AV, Cond, Media, StyleA, ValueT}

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

  val heroTitle: StyleA = style(
    fontSize(6.rem),
    lineHeight(6.rem),
    Font.tradeGothicLTStd,
    color(c"#FFF"),
    BulmaStyles.ResponsiveHelpers.block,
    textAlign.center,
    textShadow := "0.1rem 0.1rem 0.1rem #000",
    textTransform.uppercase,
    (media.all.maxWidth(800.px))(fontSize(3.rem), lineHeight(3.rem))
  )

  // colors
  object Color {
    val white: ValueT[ValueT.Color] = c"#fff"
    val black: ValueT[ValueT.Color] = c"#000"
    val grey: ValueT[ValueT.Color] = c"#b2b2b2"
    val pink: ValueT[ValueT.Color] = c"#ed1844"
    val darkGrey: ValueT[ValueT.Color] = c"#808080"
    val error: ValueT[ValueT.Color] = c"#d0011b"
    val softGrey: ValueT[ValueT.Color] = c"#cccccc"
    val lightPink: ValueT[ValueT.Color] = rgba(208, 2, 27, 0.1)
    val backgroundFooter: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
    val lightGrey: ValueT[ValueT.Color] = c"#bcbcbc"
    val green: ValueT[ValueT.Color] = c"#6eb61f"
    val red: ValueT[ValueT.Color] = c"#da001a"
    val greyVote: ValueT[ValueT.Color] = c"#9b9b9b"
    val greyLight: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.3)
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
    val footer: ValueT[ValueT.Color] = c"#ececec"
    val wrapper: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
    val action: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
    val pink: StyleA = style(addClassNames("background-pink"))
    val google: StyleA = style(addClassNames("background-google"))
    val facebook: StyleA = style(addClassNames("background-facebook"))
  }

  object Form {
    val inputText: StyleA = style(addClassName("make-input-text"))
    val inputSelect: StyleA = style(addClassName("make-input-select"))
    val field: StyleA = style(addClassName("make-field"))
    val inputIcon: StyleA = style(addClassName("make-input-icon"))
    val inputIconLeft: StyleA = style(addClassName("make-input-icon-left"))
  }

  object Button {
    val default: StyleA = style(addClassNames(BulmaStyles.Element.button.htmlClass, "make-button-default"))
    val facebook: StyleA = style(addClassNames(BulmaStyles.Element.button.htmlClass, "make-button-facebook"))
    val google: StyleA = style(addClassNames(BulmaStyles.Element.button.htmlClass, "make-button-google"))
    val baseMake: StyleA = style(boxShadow := "0, 1px, 1px, 0, rgba(0, 0, 0, 0.5)")
  }

  object Modal {
    val overlay: StyleA = style(addClassNames("make-modal-overlay"))
    val modal: StyleA = style(addClassNames("make-modal-modal"))
    val close: StyleA = style(addClassNames("make-modal-close"))
    val content: StyleA = style(addClassNames("make-modal-content"))
    val title: StyleA = style(addClassNames("make-modal-title"))
  }

  def gradientBackground(from: String, to: String): StyleA =
    style(background := s"linear-gradient(131deg, $from, $to)")

  def gradientBackgroundImage(deg: String, from: String, to: String): StyleA =
    style(backgroundImage := s"linear-gradient($deg, $from, $to)")
}
