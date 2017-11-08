package org.make.front.components.proposal

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.utils._

object ProposalTileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      height(100.%%),
      minHeight(360.pxToEm()),
      minWidth(270.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)"
    )

  val innerWrapper: StyleA =
    style(display.table, tableLayout.fixed, width(100.%%), height(100.%%))

  val row: StyleA =
    style(display.tableRow)

  val cell: StyleA =
    style(display.tableCell, verticalAlign.top)

  val cellAlignedAtTheBottom: StyleA =
    style(display.tableCell, verticalAlign.bottom)

  val proposalInfosWrapper: StyleA = style(
    margin(`0`, ThemeStyles.SpacingValue.small.pxToEm()),
    padding(ThemeStyles.SpacingValue.small.pxToEm(), `0`, ThemeStyles.SpacingValue.smaller.pxToEm()),
    borderBottom(1.px, solid, ThemeStyles.BorderColor.veryLight)
  )

  val shareOwnProposalWrapper: StyleA = style(
    padding(
      ThemeStyles.SpacingValue.small.pxToEm(),
      ThemeStyles.SpacingValue.small.pxToEm(),
      ThemeStyles.SpacingValue.smaller.pxToEm(),
      ThemeStyles.SpacingValue.small.pxToEm()
    ),
    backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent)
  )

  val proposalLinkOnTitle: StyleA = style(color(ThemeStyles.TextColor.base))

  val contentWrapper: StyleA =
    style(padding(ThemeStyles.SpacingValue.small.pxToEm()), overflow.hidden)

  val footer: StyleA = style(
    margin(`0`, ThemeStyles.SpacingValue.small.pxToEm()),
    padding(ThemeStyles.SpacingValue.smaller.pxToEm(), `0`, ThemeStyles.SpacingValue.small.pxToEm()),
    borderTop(1.px, solid, ThemeStyles.BorderColor.veryLight)
  )

}
