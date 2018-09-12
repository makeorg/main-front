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
import org.make.front.styles.base.TextStyles
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

  val wrapperOverlay: StyleA = style(
    position.absolute,
    width(100.%%),
    height(100.%%),
    top(`0`),
    left(`0`),
    zIndex(1),
    backgroundColor(ThemeStyles.BackgroundColor.white)
  )

  val noOverlay: StyleA = style(display.none)

  val hardOpacity: StyleA = style(opacity(0.75))

  val lightOpacity: StyleA = style(opacity(0.5))

  val innerWrapper: StyleA =
    style(display.flex, flexFlow := "column", justifyContent.spaceBetween)

  val proposalInfosWrapper: StyleA = style(
    display.flex,
    flexFlow := s"column",
    alignItems.center,
    margin(`0`, 20.pxToEm()),
    padding(20.pxToEm(), `0`, ThemeStyles.SpacingValue.smaller.pxToEm()),
    borderBottom(1.px, solid, ThemeStyles.BorderColor.veryLight),
    minHeight(60.pxToEm()),
    ThemeStyles.MediaQueries.beyondSmall(flexFlow := s"row")
  )

  val proposalStatus: StyleA =
    style(
      display.flex,
      alignSelf.flexEnd,
      textAlign.center,
      TextStyles.smallerText,
      position.relative,
      zIndex(2),
      color(ThemeStyles.TextColor.white),
      padding(3.pxToEm(13), 10.pxToEm(13)),
      ThemeStyles.MediaQueries.beyondSmall(padding(3.pxToEm(14), 10.pxToEm(14)))
    )

  val proposalAccepted: StyleA = style(backgroundColor(ThemeStyles.ThemeColor.positive))

  val proposalRefused: StyleA = style(backgroundColor(ThemeStyles.ThemeColor.assertive))

  val proposalWaiting: StyleA = style(
    width(100.%%),
    maxWidth(180.pxToEm(13)),
    backgroundColor(ThemeStyles.ThemeColor.prominent),
    ThemeStyles.MediaQueries.beyondSmall(maxWidth(180.pxToEm(14)))
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
    style(display.flex, flexFlow := "column", padding(ThemeStyles.SpacingValue.small.pxToEm(), 20.pxToEm()))

  val orgVotesWrapper: StyleA =
    style(
      marginLeft(ThemeStyles.SpacingValue.small.pxToEm()),
      marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm())
    )

  val footer: StyleA = style(
    display.flex,
    minHeight(55.pxToEm()),
    margin(`0`, ThemeStyles.SpacingValue.small.pxToEm()),
    padding(ThemeStyles.SpacingValue.smaller.pxToEm(), `0`, 20.pxToEm()),
    borderTop(1.px, solid, ThemeStyles.BorderColor.veryLight)
  )

  val tileWithVideoWrapper: StyleA =
    style(display.grid, gridTemplateColumns := s"repeat(50, 1fr)", alignContent.stretch)

  val tileWidth: Int = 750
  val videoWith: Int = 316
  val proposalWidth: Int = tileWidth - videoWith

  val proposalWrapper: StyleA =
    style(gridColumn := "1/51", ThemeStyles.MediaQueries.beyondSmall(gridColumn := "1/28", gridRow := "1/2"))

  val videoWrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent),
      ThemeStyles.MediaQueries.beyondSmall(gridColumn := "28/51", gridRow := "1/2", display.flex, alignItems.center)
    )

  val specialRatioVideoContainer: StyleA =
    style(position.relative, paddingBottom(125.%%), width(100.%%), height(`0`), overflow.hidden)

  val verticalVideoContainer: StyleA =
    style(position.relative, paddingBottom(177.77.%%), width(100.%%), height(`0`), overflow.hidden)

  val horizontalVideoContainer: StyleA =
    style(position.relative, paddingBottom(56.25.%%), width(100.%%), height(`0`), overflow.hidden)

  val videoIframe: StyleA =
    style(position.absolute, top(`0`), left(`0`), width(100.%%), height(100.%%))
}
