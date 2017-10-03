package org.make.front.components.proposal

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.utils._

import scalacss.DevDefaults.StyleA
import scalacss.internal.mutable.StyleSheet

object ProposalStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(height(100.%%), backgroundColor(ThemeStyles.BackgroundColor.white))

  val proposalInfosWrapper: StyleA = style(
    margin :=! s"0 ${ThemeStyles.SpacingValue.small.pxToEm().value}",
    padding :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0 ${ThemeStyles.SpacingValue.smaller.pxToEm().value}",
    borderBottom :=! s"1px solid ${ThemeStyles.BorderColor.veryLight.value}"
  )

  val shareOwnProposalWrapper: StyleA = style(
    padding :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} ${ThemeStyles.SpacingValue.small
      .pxToEm()
      .value} ${ThemeStyles.SpacingValue.smaller.pxToEm().value} ${ThemeStyles.SpacingValue.small.pxToEm().value}",
    backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent)
  )

  val contentWrapper: StyleA =
    style(padding(ThemeStyles.SpacingValue.small.pxToEm()))

  val footer: StyleA = style(
    margin :=! s"0 ${ThemeStyles.SpacingValue.small.pxToEm().value}",
    padding :=! s"${ThemeStyles.SpacingValue.smaller.pxToEm().value} 0 ${ThemeStyles.SpacingValue.small.pxToEm().value}",
    borderTop :=! s"1px solid ${ThemeStyles.BorderColor.veryLight.value}"
  )

}
