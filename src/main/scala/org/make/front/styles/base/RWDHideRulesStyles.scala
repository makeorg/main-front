package org.make.front.styles.base

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles

object RWDHideRulesStyles extends StyleSheet.Inline {

  import dsl._

  val hide: StyleA = style(display.none)
  val invisible: StyleA = style(visibility.hidden)

  val showBlockBeyondSmall: StyleA = style(display.none, ThemeStyles.MediaQueries.beyondSmall(display.block))
  val showBlockBeyondMedium: StyleA = style(display.none, ThemeStyles.MediaQueries.beyondMedium(display.block))
  val showBlockBeyondLargeMedium: StyleA = style(display.none, ThemeStyles.MediaQueries.beyondLargeMedium(display.block))
  val showBlockBeyondLarge: StyleA = style(display.none, ThemeStyles.MediaQueries.beyondLarge(display.block))

  val showInlineBlockBeyondSmall: StyleA =
    style(display.none, ThemeStyles.MediaQueries.beyondSmall(display.inlineBlock))
  val showInlineBlockBeyondMedium: StyleA =
    style(display.none, ThemeStyles.MediaQueries.beyondMedium(display.inlineBlock))
  val showInlineBlockBeyondLarge: StyleA =
    style(display.none, ThemeStyles.MediaQueries.beyondLarge(display.inlineBlock))

  val hideBeyondSmall: StyleA = style(ThemeStyles.MediaQueries.beyondSmall(display.none))
  val hideBeyondMedium: StyleA = style(ThemeStyles.MediaQueries.beyondMedium(display.none))
  val hideBeyondLargeMedium: StyleA = style(ThemeStyles.MediaQueries.beyondLargeMedium(display.none))
  val hideBeyondLarge: StyleA = style(ThemeStyles.MediaQueries.beyondLarge(display.none))
}
