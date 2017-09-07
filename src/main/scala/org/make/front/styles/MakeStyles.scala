package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{AV, Cond, Length, Media, StyleA, ValueT}

object MakeStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

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
  val title2: StyleA = style(fontSize(3.4.rem), Font.tradeGothicLTStd, textTransform.uppercase)

  //todo: implement h3
  val title3: StyleA = style()

  ///////////////////////////////////////////////////////////////////////////

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

  object TextStyles {
    val smallerText: StyleA = style(fontSize(14.pxToEm()))
    val smallText: StyleA = style(fontSize(13.pxToEm()), MediaQueries.beyondSmall(fontSize(16.pxToEm())))
    val baseText: StyleA = style(fontSize(18.pxToEm()))
    val boldText: StyleA = style(Font.circularStdBold)
    val title: StyleA = style(Font.tradeGothicLTStd, textTransform.uppercase)
    val smallerTitle: StyleA =
      style(TextStyles.title, fontSize(15.pxToEm()), MediaQueries.beyondSmall(fontSize(20.pxToEm())))
    val smallTitle: StyleA = style(TextStyles.title, fontSize(22.pxToEm()))
    val mediumTitle: StyleA =
      style(TextStyles.title, fontSize(20.pxToEm()), MediaQueries.beyondSmall(fontSize(34.pxToEm())))
    val bigTitle: StyleA = style(TextStyles.title, fontSize(46.pxToEm()))
    val veryBigTitle: StyleA =
      style(TextStyles.title, fontSize(30.pxToEm()), MediaQueries.beyondMedium(fontSize(60.pxToEm())))
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
    val grey: ValueT[ValueT.Color] = rgb(231, 231, 231)
    val blackTransparent: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.05)
  }

  object BorderColor {
    val base: ValueT[ValueT.Color] = rgb(155, 155, 155)
    val light: ValueT[ValueT.Color] = rgb(204, 204, 204)
    val blackTransparent: ValueT[ValueT.Color] = rgba(0, 0, 0, 0.01)
  }

  object ContainerMaxWidth {
    val main: ValueT[ValueT.LenPct] = 1200.pxToEm()
  }

  object Spacing {
    val smaller: ValueT[ValueT.LenPct] = 10.pxToEm()
    val small: ValueT[ValueT.LenPct] = 15.pxToEm()
    val medium: ValueT[ValueT.LenPct] = 30.pxToEm()
    val large: ValueT[ValueT.LenPct] = 50.pxToEm()
    val larger: ValueT[ValueT.LenPct] = 60.pxToEm()
  }

  val mainNavDefaultHeight: ValueT[ValueT.LenPct] = 80.pxToEm()

}
