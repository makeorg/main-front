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

package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.actorProfile.ActorProfileContributionsStyles
import org.make.front.components.userProfile.ProfileProposalListStyles
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._
import org.make.front.models.{ExtraOperations => ExtraOperationsModel}
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object ExtraOperations {

  final case class ExtraOperationsProps(operations: js.Array[ExtraOperationsModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[ExtraOperationsProps, Unit](
        displayName = "FeaturedExtraOperations",
        render = { self =>
          def extraOperationTile(operation: ExtraOperationsModel) =
            <.article(^.className := ExtraOperationsStyles.operation)(
              <.div(
                ^.className := ExtraOperationsStyles.gradient,
                ^.style := Map("background" -> operation.operationGradient)
              )(),
              <.a(^.href := operation.operationLink, ^.className := ExtraOperationsStyles.operationLink)(
                <.h3()(operation.title),
                <.span(
                  ^.className := js
                    .Array(FontAwesomeStyles.angleRight, ExtraOperationsStyles.arrow)
                )()
              )
            )

          <.section(^.className := js.Array(ExtraOperationsStyles.wrapper, LayoutRulesStyles.centeredRow))(
            <.header()(<.h2(^.className := TextStyles.mediumTitle)(unescape(I18n.t("home.extra-operations.title")))),
            <.div(^.className := ExtraOperationsStyles.operationList)(
              self.props.wrapped.operations
                .map(operation => extraOperationTile(operation))
                .toSeq
            ),
            <.style()(ExtraOperationsStyles.render[String])
          )
        }
      )

}

object ExtraOperationsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondSmall(
        padding(ThemeStyles.SpacingValue.larger.pxToEm(), ThemeStyles.SpacingValue.medium.pxToEm())
      )
    )

  val operationList: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(display.flex, justifyContent.spaceBetween, alignItems.center)
    )

  val operation: StyleA =
    style(
      display.flex,
      width(100.%%),
      marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()),
      ThemeStyles.MediaQueries.beyondLargeMedium(maxWidth(360.pxToEm()), margin(`0`))
    )

  val operationLink: StyleA =
    style(
      TextStyles.smallerText,
      ThemeStyles.Font.circularStdBold,
      color(ThemeStyles.TextColor.base),
      display.flex,
      justifyContent.spaceBetween,
      alignItems.center,
      width(100.%%),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      minHeight(70.pxToEm(13)),
      padding(`0`, 20.pxToEm(13)),
      ThemeStyles.MediaQueries.beyondLargeMedium(minHeight(70.pxToEm(14)), padding(`0`, 20.pxToEm(14)))
    )

  val gradient: StyleA =
    style(width(4.pxToEm()), height(100.%%), minHeight(70.pxToEm()))

  val arrow: StyleA =
    style(marginLeft(ThemeStyles.SpacingValue.smaller.pxToEm()))

}
