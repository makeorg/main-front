/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
