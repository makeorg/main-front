package org.make.front.styles

import org.make.front.Main.CssSettings._

import scalacss.internal.{Length, StyleA}

object CTAStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 18): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val basic: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.top,
      maxWidth(100.%%),
      boxSizing.borderBox,
      height(40.pxToEm()),
      padding :=! s"${12.pxToEm().value} ${20.pxToEm().value} 0",
      borderRadius(20.pxToEm()),
      lineHeight.normal,
      ThemeStyles.Font.tradeGothicLTStd,
      textTransform.uppercase,
      textAlign.center,
      whiteSpace.nowrap,
      overflow.hidden,
      textOverflow := s"ellipsis",
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.ThemeColor.primary),
      boxShadow := s"0 1px 1px rgba(0, 0, 0, 0.5)"
    )

}
