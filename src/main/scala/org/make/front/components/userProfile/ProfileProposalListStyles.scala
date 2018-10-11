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

package org.make.front.components.userProfile

import org.make.front.Main.CssSettings._
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import scalacss.internal.ValueT

object ProfileProposalListStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(padding(20.pxToEm(), `0`), ThemeStyles.MediaQueries.beyondLargeMedium(padding(40.pxToEm(), `0`)))

  val headerWrapper: StyleA =
    style(
      borderBottom(1.pxToEm(), solid, ThemeStyles.BorderColor.lighter),
      padding(`0`, 20.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(padding(`0`))
    )

  val title: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondSmall(fontSize(18.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm(18)))
    )

  val emptyWrapper: StyleA =
    style(display.flex, flexFlow := s"column", alignItems.center)

  val emptyIcon: StyleA =
    style(
      fontSize(42.pxToEm()),
      margin(ThemeStyles.SpacingValue.small.pxToEm(42), `0`),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(
          fontSize(72.pxToEm()),
          margin(ThemeStyles.SpacingValue.medium.pxToEm(72), `0`, 20.pxToEm(72))
        )
    )

  val emptyDesc: StyleA =
    style(TextStyles.mediumText, fontWeight.bold)

  val proposalItem: StyleA =
    style(marginTop(20.pxToEm()))

}
