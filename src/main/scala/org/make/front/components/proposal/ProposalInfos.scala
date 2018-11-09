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

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalAuthorInfos.ProposalAuthorInfosProps
import org.make.front.components.proposal.ProposalTileStyles.style
import org.make.front.components.userNav.UserNavStyles
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.facades.userPlaceholder
import org.make.front.styles._
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import scalacss.internal.{Attr, ValueT}

import scala.scalajs.js

object ProposalInfos {

  final case class ProposalInfosProps(proposal: ProposalModel, country: Option[String])

  val reactClass: ReactClass =
    React
      .createClass[ProposalInfosProps, Unit](
        displayName = "ProposalInfos",
        render = (self) => {

          def label(trending: String): js.Array[ReactElement] = {
            trending match {
              case "hot" =>
                js.Array(
                  <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                    <.p(^.className := ProposalInfosStyles.label)(
                      <("svg")(
                        ^("xmlns") := "http://www.w3.org/2000/svg",
                        ^("x") := "0px",
                        ^("y") := "0px",
                        ^("width") := "24",
                        ^("height") := "24",
                        ^("viewBox") := "0 0 24 24"
                      )(
                        <("path")(
                          ^("d") := "M9.2,19.5c-1-2.1-0.5-3.3,0.3-4.4c0.8-1.2,1.1-2.4,1.1-2.4s0.7,0.9,0.4,2.2 c1.2-1.3,1.4-3.4,1.2-4.2c2.6,1.8,3.8,5.8,2.2,8.8c8.1-4.6,2-11.4,1-12.2c0.4,0.8,0.4,2.1-0.3,2.7c-1.2-4.6-4.2-5.5-4.2-5.5 c0.4,2.4-1.3,4.9-2.8,6.9c-0.1-0.9-0.1-1.6-0.6-2.5C7.3,10.6,6,12,5.6,13.7C5.1,16,6,17.7,9.2,19.5z"
                        )()
                      )
                    )
                  )
                )
              case "trending" =>
                js.Array(
                  <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                    <.p(^.className := ProposalInfosStyles.label)(
                      <.i(^.className := js.Array(FontAwesomeStyles.lineChart))()
                    )
                  )
                )
              case "new" =>
                js.Array(
                  <.div(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                    <.p(^.className := ProposalInfosStyles.label)("new")
                  )
                )
              case _ => js.Array()
            }
          }

          val avatarUrl: String = self.props.wrapped.proposal.author.avatarUrl.getOrElse(userPlaceholder.toString)
          <.div(^.className := js.Array(ProposalInfosStyles.displayInlineBlock))(
            <.div(^.className := js.Array(TableLayoutStyles.cellVerticalAlignMiddle, ProposalInfosStyles.infosWrapper))(
              <.div(^.className := js.Array(UserNavStyles.avatarWrapper, ProposalInfosStyles.avatar))(
                <.img(^.src := avatarUrl, ^.className := UserNavStyles.avatar, ^("data-pin-no-hover") := "true")()
              ),
              <.ProposalAuthorInfos(
                ^.wrapped := ProposalAuthorInfosProps(
                  proposal = self.props.wrapped.proposal,
                  country = self.props.wrapped.country
                )
              )()
            ),
            self.props.wrapped.proposal.trending.map(label).getOrElse(js.Array()),
            <.style()(ProposalInfosStyles.render[String])
          )

        }
      )
}

object ProposalInfosStyles extends StyleSheet.Inline {

  import dsl._

  val infosWrapper: StyleA =
    style(width(100.%%))

  val avatar: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val infos: StyleA =
    style(
      ThemeStyles.Font.circularStdBook,
      fontSize(14.pxToEm()),
      lineHeight(30.pxToEm()),
      color(ThemeStyles.TextColor.lighter)
    )

  val blue: ValueT[ValueT.Color] = rgb(74, 144, 226)

  val checkCircle: StyleA =
    style(color(blue), marginLeft(5.pxToEm()))

  val label: StyleA = style(
    ThemeStyles.Font.circularStdBold,
    fontSize(11.pxToEm()),
    display.inlineBlock,
    verticalAlign.middle,
    width(24.pxToEm(11)),
    height(24.pxToEm(11)),
    lineHeight(24.pxToEm(11)),
    borderRadius(50.%%),
    backgroundColor(ThemeStyles.ThemeColor.prominent),
    color(ThemeStyles.TextColor.white),
    overflow.hidden,
    textAlign.center,
    unsafeChild("path")(Attr.real("fill") := ThemeStyles.TextColor.white)
  )

  val displayInlineBlock: StyleA = style(display.inlineBlock)

  val actorLink: StyleA =
    style(color(ThemeStyles.ThemeColor.primary))

}
