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

package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._

import scala.scalajs.js

object DisplayVotesResults {

  case class DisplayVotesResultsProps(voteKeyClass: StyleA,
                                      voteIconClass: StyleA,
                                      numberOfVotes: Int,
                                      percentageOfVotes: Int)
  case class DisplayVotesResultsState()

  lazy val reactClass: ReactClass =
    React
      .createClass[DisplayVotesResultsProps, Unit](
        displayName = "DisplayVotesResults",
        render = { self =>
          <.div(^.className := DisplayVotesResultsStyles.head)(
            <.div(^.className := js.Array(DisplayVotesResultsStyles.iconWrapper, self.props.wrapped.voteKeyClass))(
              <.i(^.className := js.Array(DisplayVotesResultsStyles.icon, self.props.wrapped.voteIconClass))()
            ),
            <.div(^.className := DisplayVotesResultsStyles.dataWrapper)(
              <.p(^.className := DisplayVotesResultsStyles.percentCount)(
                self.props.wrapped.percentageOfVotes,
                unescape("%")
              ),
              <.p(^.className := DisplayVotesResultsStyles.numberCount)(
                self.props.wrapped.numberOfVotes,
                unescape("&nbsp;"),
                if (self.props.wrapped.numberOfVotes > 1) {
                  unescape(I18n.t("user.profile.votes"))
                } else {
                  unescape(I18n.t("user.profile.votes"))
                }
              )
            ),
            <.style()(DisplayVotesResultsStyles.render[String])
          )
        }
      )

}

object DisplayVotesResultsStyles extends StyleSheet.Inline {

  import dsl._

  val head: StyleA =
    style(display.flex, alignItems.center)

  val iconWrapper: StyleA =
    style(
      display.flex,
      width(48.pxToEm()),
      height(48.pxToEm()),
      alignItems.center,
      justifyContent.center,
      borderRadius(48.pxToEm()),
      overflow.hidden,
      marginRight(ThemeStyles.SpacingValue.smaller.pxToEm())
    )

  val percentCount: StyleA =
    style(TextStyles.smallText, color(ThemeStyles.TextColor.lighter))

  val numberCount: StyleA =
    style(TextStyles.smallerText, color(ThemeStyles.TextColor.lighter), textTransform.lowercase)

  val icon: StyleA =
    style(fontSize(24.pxToEm()), color(ThemeStyles.TextColor.white))

  val dataWrapper: StyleA =
    style(display.flex, flexFlow := s"column")
}
