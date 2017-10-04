package org.make.front.components.proposal

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.utils._

import scalacss.DevDefaults.StyleA
import scalacss.internal.mutable.StyleSheet

object ProposalStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      height(100.%%),
      minWidth(270.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val innerWrapper: StyleA =
    style(display.table, width(100.%%), height(100.%%))

  val row: StyleA =
    style(display.tableRow, height(0.%%))

  val stretchedRow: StyleA =
    style(height(100.%%))

  val cell: StyleA =
    style(display.tableCell, verticalAlign.top)

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
