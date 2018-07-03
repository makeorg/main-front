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
import org.make.front.styles.utils._

object TextStyles extends StyleSheet.Inline {

  import dsl._

  val smallerText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(13.pxToEm()),
    lineHeight(20.0 / 13.0),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(14.pxToEm()))
  )

  val smallText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(13.pxToEm()),
    lineHeight(20.0 / 13.0),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(16.pxToEm()), lineHeight(20.0 / 16.0))
  )

  val mediumText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(15.pxToEm()),
    lineHeight(20.0 / 15.0),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()), lineHeight(23.0 / 18.0))
  )

  val biggerMediumText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(15.pxToEm()),
    lineHeight(20.0 / 15.0),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(24.pxToEm()), lineHeight(32.0 / 24.0))
  )

  val bigText: StyleA = style(
    ThemeStyles.Font.circularStdBook,
    fontSize(18.pxToEm()),
    lineHeight(28.0 / 18.0),
    ThemeStyles.MediaQueries.beyondSmall(fontSize(28.pxToEm()), lineHeight(38.0 / 28.0))
  )

  val veryBigText: StyleA =
    style(ThemeStyles.Font.circularStdBook, fontSize(44.pxToEm()), lineHeight(1))

  val boldText: StyleA = style(ThemeStyles.Font.circularStdBold)

  val intro: StyleA = style(ThemeStyles.Font.playfairDisplayItalic, fontStyle.italic)

  val mediumIntro: StyleA =
    style(
      intro,
      fontSize(15.pxToEm()),
      lineHeight(20.0 / 15.0),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(24.pxToEm()), lineHeight(32.0 / 24.0))
    )

  val bigIntro: StyleA =
    style(
      intro,
      fontSize(18.pxToEm()),
      lineHeight(23.0 / 18.0),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(36.pxToEm()), lineHeight(1))
    )

  val title: StyleA =
    style(ThemeStyles.Font.tradeGothicLTStd, textTransform.uppercase)

  val verySmallTitle: StyleA =
    style(
      title,
      fontSize(13.pxToEm()),
      lineHeight(20.0 / 13.0),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()), lineHeight(23.0 / 18.0))
    )

  val smallerTitleAlt: StyleA =
    style(title, fontSize(15.pxToEm()), lineHeight(1), ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm())))

  val smallerTitle: StyleA =
    style(title, fontSize(15.pxToEm()), lineHeight(1), ThemeStyles.MediaQueries.beyondSmall(fontSize(20.pxToEm())))

  val smallTitle: StyleA =
    style(title, fontSize(18.pxToEm()), lineHeight(1), ThemeStyles.MediaQueries.beyondSmall(fontSize(22.pxToEm())))

  val mediumTitle: StyleA =
    style(title, fontSize(20.pxToEm()), lineHeight(1), ThemeStyles.MediaQueries.beyondSmall(fontSize(34.pxToEm())))

  val bigTitle: StyleA =
    style(
      title,
      fontSize(20.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(34.pxToEm())),
      ThemeStyles.MediaQueries.beyondMedium(fontSize(46.pxToEm()))
    )

  val veryBigTitle: StyleA =
    style(
      title,
      fontSize(30.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(40.pxToEm())),
      ThemeStyles.MediaQueries.beyondMedium(fontSize(60.pxToEm()))
    )

  val label: StyleA =
    style(
      display.inlineBlock,
      padding(4.pxToEm(13), 5.pxToEm(13), `0`),
      ThemeStyles.Font.tradeGothicLTStd,
      fontSize(13.pxToEm()),
      lineHeight(20.0 / 13.0),
      textTransform.uppercase,
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.TextColor.base),
      ThemeStyles.MediaQueries
        .beyondSmall(padding(7.pxToEm(16), 10.pxToEm(16), 3.pxToEm(16)), fontSize(16.pxToEm()), lineHeight(20.0 / 16.0))
    )
}
