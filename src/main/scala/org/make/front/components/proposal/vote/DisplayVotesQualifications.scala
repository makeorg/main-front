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
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.helpers.NumberFormat.formatToKilo
import org.make.front.models.{Qualification, Vote => VoteModel}
import org.make.front.styles._
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.utils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import org.make.front.Main.CssSettings._

import scala.scalajs.js

object DisplayVotesQualifications {

  case class DisplayVotesQualificationsProps(voteKey: String, qualification: Qualification, resultsColor: StyleA)
  case class DisplayVotesQualificationsState(count: Int)

  lazy val reactClass: ReactClass = React.createClass[DisplayVotesQualificationsProps, DisplayVotesQualificationsState](
    displayName = "DisplayVotesQualifications",
    getInitialState = { self =>
      DisplayVotesQualificationsState(count = self.props.wrapped.qualification.count)
    },
    componentWillReceiveProps = { (self, props) =>
      self.setState(DisplayVotesQualificationsState(count = props.wrapped.qualification.count))
    },
    render = { (self) =>
      <.div(^.className := DisplayVotesQualificationsStyles.qualifiedDataRow)(
        <.span(
          ^.className := js.Array(DisplayVotesQualificationsStyles.qualificationsKey, self.props.wrapped.resultsColor),
          ^.dangerouslySetInnerHTML := I18n
            .t(s"proposal.vote.${self.props.wrapped.voteKey}.qualifications.${self.props.wrapped.qualification.key}")
        )(),
        <.span(^.className := DisplayVotesQualificationsStyles.qualificationsCounter)(self.state.count),
        <.style()(DisplayVotesQualificationsStyles.render[String])
      )
    }
  )
}

object DisplayVotesQualificationsStyles extends StyleSheet.Inline {

  import dsl._

  val qualifiedDataRow: StyleA =
    style(display.flex, alignItems.stretch, justifyContent.spaceBetween)

  val qualificationsKey: StyleA =
    style(TextStyles.smallerText, fontWeight.bold, unsafeChild(".fa-heart")(color(ThemeStyles.ThemeColor.negative)))

  val qualificationsCounter: StyleA =
    style(TextStyles.smallerText, color(ThemeStyles.TextColor.lighter))
}
