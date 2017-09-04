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

  // font
  object Font {
    val tradeGothicLTStd: AV = fontFamily :=! "TradeGothicLTStdBdCn20"
    val playfairDisplayItalic: AV = fontFamily :=! "Playfair Display"
    val circularStdBook: AV = fontFamily :=! "CircularStdBook"
    val circularStdBold: AV = fontFamily :=! "CircularStdBold"
  }

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

  // background
  object Background {
    val footer: ValueT[ValueT.Color] = c"#ececec"
    val wrapper: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
    val action: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
    val pink: StyleA = style(addClassNames("background-pink"))
    val google: StyleA = style(addClassNames("background-google"))
    val facebook: StyleA = style(addClassNames("background-facebook"))
  }

  def gradientBackground(from: String, to: String): StyleA =
    style(background := s"linear-gradient(131deg, $from, $to)")

  def gradientBackgroundImage(deg: String, from: String, to: String): StyleA =
    style(backgroundImage := s"linear-gradient($deg, $from, $to)")

  object Spacing {
    val smaller: ValueT[ValueT.LenPctAuto] = 1.rem
    val small: ValueT[ValueT.LenPctAuto] = 1.5.rem
    val medium: ValueT[ValueT.LenPctAuto] = 3.rem
    val large: ValueT[ValueT.LenPctAuto] = 5.rem
    val larger: ValueT[ValueT.LenPctAuto] = 6.rem
  }

  object Form {
    val inputText: StyleA = style(addClassName("make-input-text"))
    val inputSelect: StyleA = style(addClassName("make-input-select"))
    val field: StyleA = style(addClassName("make-field"))
    val inputIcon: StyleA = style(addClassName("make-input-icon"))
    val inputIconLeft: StyleA = style(addClassName("make-input-icon-left"))

    val errorInput: StyleA =
      style(backgroundColor(MakeStyles.Color.lightPink), border :=! s"0.1rem solid ${MakeStyles.Color.error}")

    val errorMessage: StyleA = style(
      display.block,
      margin.auto,
      MakeStyles.Font.circularStdBook,
      fontSize(1.4F.rem),
      color(MakeStyles.Color.error),
      lineHeight(1.8F.rem),
      paddingBottom(1.rem)
    )
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

  //todo: implement h1
  val title1: StyleA = style()
  val title2: StyleA = style(fontSize(3.4.rem), MakeStyles.Font.tradeGothicLTStd, textTransform.uppercase)

  //todo: implement h3
  val title3: StyleA = style()

  ///////////////////////////////////////////////////////////////////////////

  object BorderColor {
    val discreet: ValueT[ValueT.Color] = rgb(229, 229, 229)
  }

  object StyleText {
    val smallerText: StyleA = style(fontSize(1.4.rem))
    val baseText: StyleA = style(fontSize(1.6.rem))
    val biggerText: StyleA = style(fontSize(1.8.rem))
    val boldText: StyleA = style(Font.circularStdBold)
    val title: StyleA = style(Font.tradeGothicLTStd, textTransform.uppercase)
    val smallTitle: StyleA = style(MakeStyles.StyleText.title, fontSize(2.2.rem))
    val mediumTitle: StyleA = style(MakeStyles.StyleText.title, fontSize(3.4.rem))
    val bigTitle: StyleA = style(MakeStyles.StyleText.title, fontSize(4.6.rem))
    val veryBigTitle: StyleA = style(MakeStyles.StyleText.title, fontSize(6.rem))
  }
}
