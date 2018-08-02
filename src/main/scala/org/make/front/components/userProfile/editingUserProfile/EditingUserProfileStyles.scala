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

package org.make.front.components.userProfile.editingUserProfile

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._

object EditingUserProfileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      position.relative,
      padding(20.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(padding(40.pxToEm(), `0`))
    )

  val sep: StyleA =
    style(
      position.absolute,
      top(`0`),
      left(`0`),
      width(90.pxToEm()),
      height(1.pxToEm()),
      backgroundColor(ThemeStyles.BorderColor.lighter)
    )

  val title: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()))
    )

  val customCheckboxLabel: StyleA =
    style(
      display.flex,
      &.hover(cursor.pointer),
      ThemeStyles.MediaQueries.beyondLargeMedium(marginLeft(180.pxToPercent(750)))
    )

  val customCheckboxIconWrapper: StyleA =
    style(
      position.relative,
      minWidth(14.pxToEm()),
      height(14.pxToEm()),
      marginRight(6.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      border(1.pxToEm(), solid, ThemeStyles.BorderColor.lighter)
    )

  val customCheckedIcon: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      display.block,
      fontSize(16.pxToEm()),
      marginTop(-10.pxToEm()),
      marginLeft(-6.pxToEm()),
      color(ThemeStyles.ThemeColor.primary)
    )

  val label: StyleA =
    style(TextStyles.smallerText, color(ThemeStyles.TextColor.lighter))

  val inputGroup: StyleA =
    style(
      display.flex,
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      alignItems.center,
      flexFlow := s"column",
      ThemeStyles.MediaQueries.beyondLargeMedium(flexFlow := s"row")
    )

  val inputLabel: StyleA =
    style(
      width(100.%%),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(textAlign.right, width(190.pxToPercent(750)), marginRight(10.pxToPercent(750))),
      &.hover(cursor.pointer)
    )

  val inputField: StyleA =
    style(
      InputStyles.wrapper,
      backgroundColor(ThemeStyles.inputsSpecialColors.fieldsBackground),
      borderColor(ThemeStyles.inputsSpecialColors.fieldsBorder),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(width(550.pxToPercent(750)))
    )

  val buttonGroup: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(marginLeft(200.pxToPercent(750)))
    )

  val submitGreyButton: StyleA =
    style(backgroundColor(ThemeStyles.TextColor.lighter))

  val submitButtonIcon: StyleA =
    style(marginRight(5.pxToEm()))

  val success: StyleA =
    style(TextStyles.smallerText, color(ThemeStyles.ThemeColor.positive))
}