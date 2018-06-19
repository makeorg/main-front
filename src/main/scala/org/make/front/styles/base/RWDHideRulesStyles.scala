package org.make.front.styles.base

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles

object RWDRulesSmallStyles extends StyleSheet.Inline {

  import dsl._

  val showBlockBeyondSmall: StyleA = style(display.none, ThemeStyles.MediaQueries.beyondSmall(display.block))
  val showInlineBlockBeyondSmall: StyleA =
    style(display.none, ThemeStyles.MediaQueries.beyondSmall(display.inlineBlock))
  val hideBeyondSmall: StyleA = style(ThemeStyles.MediaQueries.beyondSmall(display.none))
}

object RWDRulesMediumStyles extends StyleSheet.Inline {

  import dsl._

  val showBlockBeyondMedium: StyleA = style(display.none, ThemeStyles.MediaQueries.beyondMedium(display.block))
  val showInlineBlockBeyondMedium: StyleA =
    style(display.none, ThemeStyles.MediaQueries.beyondMedium(display.inlineBlock))
  val hideBeyondMedium: StyleA = style(ThemeStyles.MediaQueries.beyondMedium(display.none))
}

object RWDRulesLargeMediumStyles extends StyleSheet.Inline {

  import dsl._

  val showBlockBeyondLargeMedium: StyleA =
    style(display.none, ThemeStyles.MediaQueries.beyondLargeMedium(display.block))
  val showInlineBlockBeyondLargeMedium: StyleA =
    style(display.none, ThemeStyles.MediaQueries.beyondLargeMedium(display.inlineBlock))
  val hideBeyondLargeMedium: StyleA = style(ThemeStyles.MediaQueries.beyondLargeMedium(display.none))
}

object RWDHideRulesStyles extends StyleSheet.Inline {

  import dsl._

  val hide: StyleA = style(display.none)
  val invisible: StyleA = style(visibility.hidden)

  val showBlockBeyondLarge: StyleA = style(display.none, ThemeStyles.MediaQueries.beyondLarge(display.block))
  val showInlineBlockBeyondLarge: StyleA =
    style(display.none, ThemeStyles.MediaQueries.beyondLarge(display.inlineBlock))
  val hideBeyondLarge: StyleA = style(ThemeStyles.MediaQueries.beyondLarge(display.none))
}
