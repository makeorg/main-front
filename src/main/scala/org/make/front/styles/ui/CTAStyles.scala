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

package org.make.front.styles.ui

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.utils._

object CTAStyles extends StyleSheet.Inline {

  import dsl._

  val basic: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.top,
      minHeight(30.pxToEm(13)),
      maxWidth(100.%%),
      boxSizing.borderBox,
      padding(`0`, 15.pxToEm(13)),
      borderRadius(999.pxToEm(13)),
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
        padding(`0`, 20.pxToEm()),
        fontSize(16.pxToEm()),
        borderRadius(999.pxToEm())
      )
    )

  val negative: StyleA =
    style(color(ThemeStyles.TextColor.base), backgroundColor(ThemeStyles.BackgroundColor.white))

  val moreDiscreet: StyleA =
    style(backgroundColor(ThemeStyles.TextColor.lighter))

  val basicOnA: StyleA =
    style(
      paddingTop(9.pxToEm(13)),
      paddingBottom(4.pxToEm(13)),
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(13.pxToEm(16)), paddingBottom(6.pxToEm(16)))
    )

  val basicOnButton: StyleA =
    style(
      paddingTop(9.pxToEm(13)),
      paddingBottom(4.pxToEm(13)),
      ThemeStyles.MediaQueries.beyondSmall(paddingTop(12.pxToEm(16)), paddingBottom(7.pxToEm(16)))
    )
}
