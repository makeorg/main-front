package org.make.front.styles.base

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles
import org.make.front.styles._
import scalacss.internal.StyleA

object TextStyles extends StyleSheet.Inline {

  import dsl._

  val smallerText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(13.pxToEm()),
    lineHeight(20.0 / 13.0),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(14.pxToEm()))
  )

  val smallText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(13.pxToEm()),
    lineHeight(20.0 / 13.0),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(16.pxToEm()), lineHeight(20.0 / 16.0))
  )

  val mediumText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(15.pxToEm()),
    lineHeight(20.0 / 15.0),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()), lineHeight(23.0 / 18.0))
  )

  val biggerMediumText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(15.pxToEm()),
    lineHeight(20.0 / 15.0),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(24.pxToEm()), lineHeight(32.0 / 24.0))
  )

  val bigText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(18.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(28.pxToEm()))
  )
  val veryBigText: StyleA =
    style(ThemeStyles.Font.circularStdBook, fontSize(44.pxToEm()), lineHeight(1))

  val boldText: StyleA = style(ThemeStyles.Font.circularStdBold)
  val intro: StyleA = style(ThemeStyles.Font.playfairDisplayItalic, fontStyle.italic)
  val title: StyleA =
    style(ThemeStyles.Font.tradeGothicLTStd, textTransform.uppercase)

  val smallerTitle: StyleA =
    style(title, fontSize(15.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(fontSize(20.pxToEm())))
  val smallTitle: StyleA =
    style(title, fontSize(18.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(fontSize(22.pxToEm())))
  val mediumTitle: StyleA =
    style(title, fontSize(20.pxToEm()), ThemeStyles.MediaQueries.beyondSmall(fontSize(34.pxToEm())))
  val bigTitle: StyleA =
    style(title, fontSize(20.pxToEm()), lineHeight(1), ThemeStyles.MediaQueries.beyondSmall(fontSize(46.pxToEm())))
  val veryBigTitle: StyleA =
    style(title, fontSize(30.pxToEm()), ThemeStyles.MediaQueries.beyondMedium(fontSize(60.pxToEm())))

  val label: StyleA =
    style(
      display.inlineBlock,
      padding :=! s"${4.pxToEm(13).value} ${5.pxToEm(13).value} 0",
      ThemeStyles.Font.tradeGothicLTStd,
      fontSize(13.pxToEm()),
      lineHeight(20.0 / 13.0),
      textTransform.uppercase,
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.TextColor.base),
      ThemeStyles.MediaQueries.beyondSmall(
        padding :=! s"${8.pxToEm(18).value} ${10.pxToEm(18).value} ${3.pxToEm(18).value}",
        fontSize(18.pxToEm()),
        lineHeight(22.0 / 18.0)
      )
    )
}
