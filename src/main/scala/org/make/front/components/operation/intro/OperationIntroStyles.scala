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

package org.make.front.components.operation.intro

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.utils._

object OperationIntroStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.black))

  val headingWrapper: StyleA =
    style(
      position.relative,
      zIndex(1),
      paddingTop(ThemeStyles.SpacingValue.medium.pxToEm()),
      paddingBottom(ThemeStyles.SpacingValue.medium.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(
        paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()),
        paddingBottom(ThemeStyles.SpacingValue.large.pxToEm())
      )
    )

  val labelWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val infosWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val infosLabel: StyleA =
    style(width(100.%%), backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent), textAlign.center)

  val separator: StyleA =
    style(width(300.pxToEm()), margin(ThemeStyles.SpacingValue.medium.pxToEm(), auto, `0`))

  val separatorLineWrapper: StyleA =
    style(width(50.%%), paddingTop(2.pxToEm()))

  val separatorLine: StyleA =
    style(height(1.px), width(100.%%), backgroundColor(ThemeStyles.BorderColor.white), opacity(0.2))

  val separatorLineToTheLeft: StyleA = style(
    maxWidth(290.pxToEm()),
    marginLeft.auto,
    background := s"linear-gradient(to left, rgba(255,255,255,1) 0%, rgba(255,255,255,0) 100%)"
  )

  val separatorLineToTheRight: StyleA = style(
    maxWidth(290.pxToEm()),
    marginRight.auto,
    background := s"linear-gradient(to right, rgba(255,255,255,1) 0%, rgba(255,255,255,0) 100%)"
  )

  val separatorTextWrapper: StyleA = style(padding(`0`, 20.pxToEm()))

  val separatorText: StyleA = style(color(ThemeStyles.TextColor.white), opacity(0.5))

  val partnersList: StyleA = style(textAlign.center)

  val partnerItem: StyleA = style(
    display.inlineBlock,
    verticalAlign.middle,
    padding(ThemeStyles.SpacingValue.small.pxToEm(), ThemeStyles.SpacingValue.small.pxToEm(), `0`)
  )

  val partnerLogo: StyleA = style()

  val otherPartners: StyleA = style(textAlign.center, color(ThemeStyles.TextColor.white), opacity(0.5))

  val explanationWrapper: StyleA =
    style(
      position.relative,
      zIndex(1),
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent)
    )

  val explanationIll: StyleA =
    style(width(100.%%), ThemeStyles.MediaQueries.belowSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm())))

  val explanationTextWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))

  val explanationText: StyleA = style(color(ThemeStyles.TextColor.white))

  val ctaWrapper: StyleA = style(marginTop(ThemeStyles.SpacingValue.small.pxToEm()))
}
