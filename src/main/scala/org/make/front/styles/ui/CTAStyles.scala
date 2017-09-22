package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{Length, StyleA}

object CTAStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val basic: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.top,
      minHeight(40.pxToEm(13)),
      maxWidth(100.%%),
      boxSizing.borderBox,
      padding :=! s"0 ${20.pxToEm(13).value}",
      borderRadius(20.pxToEm(13)),
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

  val basicOnA: StyleA = style(paddingTop(11.pxToEm(13)), ThemeStyles.MediaQueries.beyondSmall(paddingTop(11.pxToEm())))

  val basicOnButton: StyleA =
    style(paddingTop(3.pxToEm(13)), ThemeStyles.MediaQueries.beyondSmall(paddingTop(3.pxToEm())))
}
