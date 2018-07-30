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

package org.make.front.components.proposal

import org.make.front.Main.CssSettings._
import org.make.front.styles._
import org.make.front.styles.utils._

object ProposalTileStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      position.relative,
      height(100.%%),
      ThemeStyles.MediaQueries.belowMedium(minHeight.inherit),
      minWidth(240.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.white),
      boxShadow := "0 1px 1px 0 rgba(0,0,0,0.50)",
      ThemeStyles.MediaQueries.beyondLargeMedium(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))
    )

  val innerWrapper: StyleA =
    style(tableLayout.fixed)

  val proposalInfosWrapper: StyleA = style(
    margin(`0`, 20.pxToEm()),
    padding(20.pxToEm(), `0`, ThemeStyles.SpacingValue.smaller.pxToEm()),
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
    style(padding(ThemeStyles.SpacingValue.small.pxToEm(), 20.pxToEm()))

  val footer: StyleA = style(
    margin(`0`, ThemeStyles.SpacingValue.small.pxToEm()),
    padding(ThemeStyles.SpacingValue.smaller.pxToEm(), `0`, 20.pxToEm()),
    borderTop(1.px, solid, ThemeStyles.BorderColor.veryLight)
  )

  val tileWithVideoWrapper: StyleA =
    style(display.flex, alignItems.center, flexFlow := s"row")

  val tileWidth: Int = 750
  val videoWith: Int = 312
  val proposalWidth: Int = tileWidth - videoWith

  val proposalWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(width(proposalWidth.pxToPercent(tileWidth))))

  val videoWrapper: StyleA =
    style(ThemeStyles.MediaQueries.beyondSmall(width(videoWith.pxToPercent(tileWidth))))

  val specialRatioVideoContainer: StyleA =
    style(position.relative, paddingBottom(125.%%), width(100.%%), height(`0`), overflow.hidden)

  val verticalVideoContainer: StyleA =
    style(position.relative, paddingBottom(177.77.%%), width(100.%%), height(`0`), overflow.hidden)

  val horizontalVideoContainer: StyleA =
    style(position.relative, paddingBottom(56.25.%%), width(100.%%), height(`0`), overflow.hidden)

  val videoIframe: StyleA =
    style(position.absolute, top(`0`), left(`0`), width(100.%%), height(100.%%))
}
