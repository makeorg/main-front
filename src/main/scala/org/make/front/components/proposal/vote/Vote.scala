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
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.VoteButton.VoteButtonProps
import org.make.front.models.{
  OperationExpanded,
  Proposal      => ProposalModel,
  Qualification => QualificationModel,
  Vote          => VoteModel
}
import org.make.front.styles._
import org.make.front.styles.utils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Vote {

  // TODO: figure out how to handle that gracefully
  final case class VoteProps(proposal: ProposalModel,
                             maybeOperation: Option[OperationExpanded],
                             index: Int,
                             updateState: Boolean = true,
                             vote: (String)                            => Future[VoteModel],
                             unvote: (String)                          => Future[VoteModel],
                             qualifyVote: (String, String)             => Future[QualificationModel],
                             removeVoteQualification: (String, String) => Future[QualificationModel],
                             guideToVote: Option[String] = None,
                             guideToQualification: Option[String] = None,
                             isProposalSharable: Boolean)

  final case class VoteState(votes: Map[String, VoteModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[VoteProps, VoteState](
        displayName = "Vote",
        getInitialState = { self =>
          VoteState(votes = self.props.wrapped.proposal.votes.map(vote => vote.key -> vote).toMap)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(VoteState(votes = props.wrapped.proposal.votes.map(vote => vote.key -> vote).toMap))
        },
        render = { (self) =>
          def vote(key: String): Future[VoteModel] = {
            val future = self.props.wrapped.vote(key)

            if (self.props.wrapped.updateState) {
              future.onComplete {
                case Failure(_) =>
                case Success(result) =>
                  self.setState(state => state.copy(votes = state.votes + (result.key -> result)))
              }
            }
            future
          }

          def unvote(key: String): Future[VoteModel] = {
            val future = self.props.wrapped.unvote(key)

            if (self.props.wrapped.updateState) {
              future.onComplete {
                case Failure(_) =>
                case Success(result) =>
                  self.setState(state => state.copy(votes = state.votes + (result.key -> result)))
              }
            }
            future
          }

          val voteAgree: VoteModel = self.state.votes("agree")
          val voteDisagree: VoteModel = self.state.votes("disagree")
          val voteNeutral: VoteModel = self.state.votes("neutral")

          val hasVoted = self.state.votes.values.exists(_.hasVoted)

          val votes = self.state.votes.map {
            case (key, vote) =>
              key -> vote.count
          }

          <.ul(^.className := VoteStyles.voteButtonsList)(
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(hasVoted && !self.state.votes("agree").hasVoted)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  proposal = self.props.wrapped.proposal,
                  maybeOperation = self.props.wrapped.maybeOperation,
                  updateState = self.props.wrapped.updateState,
                  proposalId = self.props.wrapped.proposal.id,
                  votes = votes,
                  vote = voteAgree,
                  handleVote = vote,
                  handleUnvote = unvote,
                  qualifyVote = self.props.wrapped.qualifyVote,
                  removeVoteQualification = self.props.wrapped.removeVoteQualification,
                  index = self.props.wrapped.index,
                  guideToQualification = self.props.wrapped.guideToQualification,
                  isProposalSharable = self.props.wrapped.isProposalSharable
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(hasVoted && !self.state.votes("disagree").hasVoted)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  proposal = self.props.wrapped.proposal,
                  maybeOperation = self.props.wrapped.maybeOperation,
                  updateState = self.props.wrapped.updateState,
                  proposalId = self.props.wrapped.proposal.id,
                  votes = votes,
                  vote = voteDisagree,
                  handleVote = vote,
                  handleUnvote = unvote,
                  qualifyVote = self.props.wrapped.qualifyVote,
                  removeVoteQualification = self.props.wrapped.removeVoteQualification,
                  guideToVote = self.props.wrapped.guideToVote,
                  guideToQualification = self.props.wrapped.guideToQualification,
                  index = self.props.wrapped.index,
                  isProposalSharable = self.props.wrapped.isProposalSharable
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(hasVoted && !self.state.votes("neutral").hasVoted)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  proposal = self.props.wrapped.proposal,
                  maybeOperation = self.props.wrapped.maybeOperation,
                  updateState = self.props.wrapped.updateState,
                  proposalId = self.props.wrapped.proposal.id,
                  votes = votes,
                  vote = voteNeutral,
                  handleVote = vote,
                  handleUnvote = unvote,
                  qualifyVote = self.props.wrapped.qualifyVote,
                  removeVoteQualification = self.props.wrapped.removeVoteQualification,
                  guideToQualification = self.props.wrapped.guideToQualification,
                  index = self.props.wrapped.index,
                  isProposalSharable = self.props.wrapped.isProposalSharable
                )
              )()
            ),
            <.style()(VoteStyles.render[String])
          )
        }
      )
}

object VoteStyles extends StyleSheet.Inline {

  import dsl._

  val voteButtonsList: StyleA =
    style(display.table, minHeight(115.pxToEm()), maxWidth(300.pxToEm()), margin(`0`, auto))

  val voteButtonItem: (Boolean) => StyleA = styleF.bool(
    hidden =>
      if (hidden) {
        styleS(display.none)
      } else {
        styleS(
          display.tableCell,
          verticalAlign.top,
          textAlign.center,
          padding(ThemeStyles.SpacingValue.small.pxToEm(), ThemeStyles.SpacingValue.smaller.pxToEm(), `0`)
        )
    }
  )
}
