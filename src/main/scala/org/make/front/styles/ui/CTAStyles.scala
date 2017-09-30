package org.make.front.styles.ui

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.utils._

import scalacss.internal.StyleA

object CTAStyles extends StyleSheet.Inline {

  import dsl._

  val basic: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.top,
      minHeight(30.pxToEm(13)),
      maxWidth(100.%%),
      boxSizing.borderBox,
      padding :=! s"0 ${15.pxToEm(13).value}",
      borderRadius(15.pxToEm(13)),
      lineHeight.normal,
      ThemeStyles.Font.tradeGothicLTStd,
      textTransform.uppercase,
      textAlign.center,
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.ThemeColor.primary),
      boxShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)",
      fontSize(13.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(
        minHeight(40.pxToEm()),
        padding :=! s"0 ${20.pxToEm().value}",
        fontSize(16.pxToEm()),
        borderRadius(20.pxToEm())
      )
    )

  val basicOnA: StyleA =
    style(paddingTop(9.pxToEm(13)), ThemeStyles.MediaQueries.beyondSmall(paddingTop(12.pxToEm(16))))

  val basicOnButton: StyleA =
    style(paddingTop(3.pxToEm(13)), ThemeStyles.MediaQueries.beyondSmall(paddingTop(3.pxToEm(16))))
}
