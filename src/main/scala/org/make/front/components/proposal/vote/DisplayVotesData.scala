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
import org.make.front.components.proposal.vote.DisplayVotesQualifications.DisplayVotesQualificationsProps
import org.make.front.components.proposal.vote.DisplayVotesResults.DisplayVotesResultsProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Qualification, Vote => VoteModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object DisplayVotesData {

  case class DisplayVotesDataProps(vote: js.Array[VoteModel],
                                   voteKeyMap: Map[String, String],
                                   voteCountMap: Map[String, Int],
                                   index: Int)
  case class DisplayVotesDataState(votesAgreeCount: Int,
                                   votesDisagreeCount: Int,
                                   votesNeutralCount: Int,
                                   votesAgreeKey: String,
                                   votesDisagreeKey: String,
                                   votesNeutralKey: String,
                                   agreeQualifications: js.Array[Qualification],
                                   disagreeQualifications: js.Array[Qualification],
                                   neutralQualifications: js.Array[Qualification],
                                   enableProgressBar: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[DisplayVotesDataProps, DisplayVotesDataState](
        displayName = "DisplayVotesData",
        getInitialState = { self =>
          DisplayVotesDataState(
            votesAgreeCount = self.props.wrapped.voteCountMap.getOrElse("agree", 0),
            votesDisagreeCount = self.props.wrapped.voteCountMap.getOrElse("disagree", 0),
            votesNeutralCount = self.props.wrapped.voteCountMap.getOrElse("neutral", 0),
            votesAgreeKey = self.props.wrapped.voteKeyMap("agree"),
            votesDisagreeKey = self.props.wrapped.voteKeyMap("disagree"),
            votesNeutralKey = self.props.wrapped.voteKeyMap("neutral"),
            agreeQualifications = self.props.wrapped.vote.apply(0).qualifications,
            disagreeQualifications = self.props.wrapped.vote.apply(1).qualifications,
            neutralQualifications = self.props.wrapped.vote.apply(2).qualifications,
            enableProgressBar = false
          )
        },
        componentDidMount = { self =>
          self.setState(_.copy(enableProgressBar = false))
        },
        render = { self =>
          val totalOfVotes
            : Int = self.state.votesAgreeCount + self.state.votesDisagreeCount + self.state.votesNeutralCount

          val partOfAgreeVotes: Int = self.state.votesAgreeCount * 100 / totalOfVotes
          val partOfDisagreeVotes: Int = self.state.votesDisagreeCount * 100 / totalOfVotes
          val partOfNeutralVotes: Int = self.state.votesNeutralCount * 100 / totalOfVotes

          object DynamicResultsOfVoteStyles extends StyleSheet.Inline {
            import dsl._

            val progressAgreeItem: StyleA =
              style(width(partOfAgreeVotes.%%))

            val progressDisagreeItem: StyleA =
              style(width(partOfDisagreeVotes.%%))

            val progressNeutralItem: StyleA =
              style(width(partOfNeutralVotes.%%))

          }

          <("DisplayVotesData")()(
            <.h2(^.className := DisplayVotesDataStyles.totalCount)(
              totalOfVotes,
              unescape("&nbsp;"),
              if (totalOfVotes > 1) {
                <.span(^.className := DisplayVotesDataStyles.totalVotes)(unescape(I18n.t("user.profile.votes")))
              } else {
                <.span(^.className := DisplayVotesDataStyles.totalVotes)(unescape(I18n.t("user.profile.votes")))
              }
            ),
            <.div(
              ^.className := js
                .Array(DisplayVotesDataStyles.expandingProgressBar, DisplayVotesDataStyles.expandedProgressBar)
            )(
              <.div(^.className := DisplayVotesDataStyles.progressBar)(
                <.div(
                  ^.className := js
                    .Array(
                      DisplayVotesDataStyles.progressItem,
                      DisplayVotesDataStyles.progressAgreeItem,
                      DynamicResultsOfVoteStyles.progressAgreeItem
                    )
                )(),
                <.div(
                  ^.className := js.Array(
                    DisplayVotesDataStyles.progressItem,
                    DisplayVotesDataStyles.progressDisagreeItem,
                    DynamicResultsOfVoteStyles.progressDisagreeItem
                  )
                )(),
                <.div(
                  ^.className := js
                    .Array(
                      DisplayVotesDataStyles.progressItem,
                      DisplayVotesDataStyles.progressNeutralItem,
                      DynamicResultsOfVoteStyles.progressNeutralItem
                    )
                )()
              )
            ),
            <.ul(^.className := DisplayVotesDataStyles.list)(
              <.li(^.className := DisplayVotesDataStyles.listItem)(
                <.article(^.className := DisplayVotesDataStyles.resultsWrapper)(
                  <.DisplayVotesResultsComponent(
                    ^.wrapped := DisplayVotesResultsProps(
                      voteKeyClass = DisplayVotesDataStyles.agreeVoteButton,
                      voteIconClass = FontAwesomeStyles.thumbsUp,
                      numberOfVotes = self.state.votesAgreeCount,
                      percentageOfVotes = partOfAgreeVotes
                    )
                  )(),
                  <.div(^.className := DisplayVotesDataStyles.qualificationWrapper)(self.state.agreeQualifications.map {
                    qualification =>
                      <.DisplayVotesQualificationsComponent(
                        ^.wrapped := DisplayVotesQualificationsProps(
                          voteKey = self.state.votesAgreeKey,
                          qualification = qualification,
                          resultsColor = DisplayVotesDataStyles.agreeVoteQualification
                        ),
                        ^.key := s"qualification_agree_${self.props.wrapped.index}_${qualification.key}"
                      )()
                  })
                )
              ),
              <.li(^.className := DisplayVotesDataStyles.listSep)(),
              <.li(^.className := DisplayVotesDataStyles.listItem)(
                <.article(^.className := DisplayVotesDataStyles.resultsWrapper)(
                  <.DisplayVotesResultsComponent(
                    ^.wrapped := DisplayVotesResultsProps(
                      voteKeyClass = DisplayVotesDataStyles.disagreeVoteButton,
                      voteIconClass = FontAwesomeStyles.thumbsDown,
                      numberOfVotes = self.state.votesDisagreeCount,
                      percentageOfVotes = partOfDisagreeVotes
                    )
                  )(),
                  <.div(^.className := DisplayVotesDataStyles.qualificationWrapper)(
                    self.state.disagreeQualifications.map { qualification =>
                      <.DisplayVotesQualificationsComponent(
                        ^.wrapped := DisplayVotesQualificationsProps(
                          voteKey = self.state.votesDisagreeKey,
                          qualification = qualification,
                          resultsColor = DisplayVotesDataStyles.disagreeVoteQualification
                        ),
                        ^.key := s"qualification_disagree_${self.props.wrapped.index}_${qualification.key}}"
                      )()
                    }
                  )
                )
              ),
              <.li(^.className := DisplayVotesDataStyles.listSep)(),
              <.li(^.className := DisplayVotesDataStyles.listItem)(
                <.article(^.className := DisplayVotesDataStyles.resultsWrapper)(
                  <.DisplayVotesResultsComponent(
                    ^.wrapped := DisplayVotesResultsProps(
                      voteKeyClass = DisplayVotesDataStyles.neutralVoteButton,
                      voteIconClass = DisplayVotesDataStyles.neutralIcon,
                      numberOfVotes = self.state.votesNeutralCount,
                      percentageOfVotes = partOfNeutralVotes
                    )
                  )(),
                  <.div(^.className := DisplayVotesDataStyles.qualificationWrapper)(
                    self.state.neutralQualifications.map { qualification =>
                      <.DisplayVotesQualificationsComponent(
                        ^.wrapped := DisplayVotesQualificationsProps(
                          voteKey = self.state.votesNeutralKey,
                          qualification = qualification,
                          resultsColor = DisplayVotesDataStyles.neutralVoteQualification
                        ),
                        ^.key := s"qualification_neutral_${self.props.wrapped.index}_${qualification.key}"
                      )()
                    }
                  )
                )
              ),
              <.style()(DisplayVotesDataStyles.render[String], DynamicResultsOfVoteStyles.render[String])
            )
          )
        }
      )

}

object DisplayVotesDataStyles extends StyleSheet.Inline {

  import dsl._

  val totalCount: StyleA =
    style(TextStyles.smallText, textAlign.center, color(ThemeStyles.TextColor.lighter), margin(5.pxToEm(13), `0`))

  val totalVotes: StyleA =
    style(textTransform.lowercase)

  val expandAnim =
    keyframes(
      0.%%   -> keyframe(width(`0`), opacity(0)),
      75.%%  -> keyframe(opacity(1)),
      100.%% -> keyframe(width(100.%%))
    )

  val expandingProgressBar: StyleA =
    style(animationName(expandAnim), animationTimingFunction.easeIn, animationDuration :=! s"1s", overflow.hidden)

  val expandedProgressBar: StyleA = style(width(100.%%), opacity(1))

  val progressBar: StyleA =
    style(display.flex, justifyContent.flexStart)

  val progressItem: StyleA =
    style(height(5.pxToEm()))

  val progressAgreeItem: StyleA =
    style(backgroundColor(ThemeStyles.ThemeColor.positive))

  val progressDisagreeItem: StyleA =
    style(backgroundColor(ThemeStyles.ThemeColor.negative))

  val progressNeutralItem: StyleA =
    style(backgroundColor(ThemeStyles.TextColor.grey))

  val list: StyleA =
    style(display.flex, justifyContent.flexStart, alignItems.center, marginTop(20.pxToEm()), overflow.auto)

  val listItem: StyleA =
    style(
      display.flex,
      minWidth(220.pxToEm()),
      width(240.pxToPercent(710)),
      padding(`0`, ThemeStyles.SpacingValue.medium.pxToPercent(710)),
      ThemeStyles.MediaQueries.beyondMedium(minWidth(`0`))
    )

  val listSep: StyleA =
    style(
      display.flex,
      alignSelf.stretch,
      minWidth(1.px),
      width(1.pxToEm()),
      backgroundColor(ThemeStyles.BorderColor.veryLight)
    )

  val resultsWrapper: StyleA =
    style(width(100.%%))

  val qualificationWrapper: StyleA =
    style(marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val agreeVoteButton: StyleA =
    style(backgroundColor(ThemeStyles.ThemeColor.positive))

  val disagreeVoteButton: StyleA =
    style(backgroundColor(ThemeStyles.ThemeColor.negative))

  val neutralVoteButton: StyleA =
    style(backgroundColor(ThemeStyles.TextColor.grey), transform := s"rotate(-90deg)")

  val agreeVoteQualification: StyleA =
    style(color(ThemeStyles.ThemeColor.positive))

  val disagreeVoteQualification: StyleA =
    style(color(ThemeStyles.ThemeColor.negative))

  val neutralVoteQualification: StyleA =
    style(color(ThemeStyles.TextColor.grey))

  val neutralIcon: StyleA =
    style(FontAwesomeStyles.thumbsUp)
}
