package org.make.front.components.proposals.proposal

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles

import scalacss.DevDefaults.StyleA
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object ProposalStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.white), padding(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val header: StyleA = style(
    paddingBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
    borderBottom :=! s"1px solid ${ThemeStyles.BorderColor.veryLight.value}"
  )

  val contentWrapper: StyleA =
    style(paddingTop(ThemeStyles.SpacingValue.smaller.pxToEm()), paddingBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val footer: StyleA = style(borderTop :=! s"1px solid ${ThemeStyles.BorderColor.veryLight.value}")

}
