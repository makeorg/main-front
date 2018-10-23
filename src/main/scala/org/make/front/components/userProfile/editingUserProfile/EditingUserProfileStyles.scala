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
      InputStyles.withIcon,
      backgroundColor(ThemeStyles.inputsSpecialColors.fieldsBackground),
      borderColor(ThemeStyles.inputsSpecialColors.fieldsBorder),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(width(550.pxToPercent(750)))
    )

  val inputTextArea: StyleA =
    style(
      InputStyles.wrapper,
      InputStyles.withIcon,
      backgroundColor(ThemeStyles.inputsSpecialColors.fieldsBackground),
      borderColor(ThemeStyles.inputsSpecialColors.fieldsBorder),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(width(550.pxToPercent(750))),
      unsafeChild("textarea")(
        height.auto,
        resize.vertical,
        ThemeStyles.MediaQueries
          .beyondSmall(height.auto)
      )
    )

  val buttonGroup: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(marginLeft(200.pxToPercent(750)))
    )

  val inlineMessage: StyleA =
    style(ThemeStyles.MediaQueries.beyondLargeMedium(marginLeft(200.pxToPercent(750))))

  val forgottenPasswordLink: StyleA =
    style(textDecoration := "underline")

  val submitButton: Boolean => StyleA = styleF.bool(
    active =>
      if (active) {
        styleS()
      } else
        styleS(backgroundColor(ThemeStyles.TextColor.lighter))
  )

  val submitGreyButton: StyleA =
    style(backgroundColor(ThemeStyles.TextColor.lighter), &.hover(backgroundColor(ThemeStyles.ThemeColor.primary)))

  val submitButtonIcon: StyleA =
    style(marginRight(5.pxToEm()))

  val contactLink: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

  val success: StyleA =
    style(TextStyles.smallerText, color(ThemeStyles.ThemeColor.positive))

  val firstNameInputWithIconWrapper: StyleA =
    style(&.before(content := "'\\f007'"))

  val ageInputWithIconWrapper: StyleA =
    style(&.before(content := "'\\f1ae'"))

  val genderInputWithIconWrapper: StyleA =
    style(&.before(content := "'\\f228'"))

  val cspInputWithIconWrapper: StyleA =
    style(&.before(content := "'\\f0f2'"))

  val postalCodeInputWithIconWrapper: StyleA =
    style(&.before(content := "'\\f041'"))

  val descriptionInputWithIconWrapper: StyleA =
    style(&.before(content := "'\\f040'"))

  val professionInputWithIconWrapper: StyleA =
    style(&.before(content := "'\\f0f2'"))

  val selectInput: StyleA =
    style(
      backgroundColor.transparent,
      width(100.%%),
      height(ThemeStyles.SpacingValue.largerMedium.pxToEm()),
      ThemeStyles.Font.circularStdBook,
      border.none,
      fontSize(1.em)
    )

  val firstOption: Boolean => StyleA = styleF.bool(
    first =>
      if (first) {
        styleS(color(ThemeStyles.TextColor.grey))
      } else {
        styleS()
    }
  )

}
