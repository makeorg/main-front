package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{Length, StyleA}

object TextStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val smallerText: StyleA = style(fontSize(13.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(fontSize(14.pxToEm())))
  val smallText: StyleA = style(fontSize(13.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(fontSize(16.pxToEm())))
  val baseText: StyleA = style(fontSize(18.pxToEm()))
  val veryBigText: StyleA = style(fontSize(44.pxToEm()))
  val boldText: StyleA = style(ThemeStyles.Font.circularStdBold)

  val title: StyleA = style(ThemeStyles.Font.tradeGothicLTStd, textTransform.uppercase)
  val smallerTitle: StyleA =
    style(title, fontSize(15.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(fontSize(20.pxToEm())))
  val smallTitle: StyleA = style(title, fontSize(22.pxToEm()))
  val mediumTitle: StyleA =
    style(title, fontSize(20.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(fontSize(34.pxToEm())))
  val bigTitle: StyleA = style(title, fontSize(46.pxToEm()))
  val veryBigTitle: StyleA =
    style(title, fontSize(30.pxToEm()), ThemeStyles.MediaQueries.beyondMedium(fontSize(60.pxToEm())))

  val label: StyleA =
    style(
      display.inlineBlock,
      padding :=! s"${ThemeStyles.Spacing.smaller.value} ${ThemeStyles.Spacing.smaller.value} ${5.pxToEm().value}",
      ThemeStyles.Font.tradeGothicLTStd,
      color(ThemeStyles.TextColor.white),
      textTransform.uppercase,
      backgroundColor(ThemeStyles.TextColor.base)
    )

}
