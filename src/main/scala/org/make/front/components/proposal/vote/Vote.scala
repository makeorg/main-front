package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.VoteButton.VoteButtonProps
import org.make.front.models.{Proposal => ProposalModel, Vote => VoteModel}
import org.make.front.styles._
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.{QualificationResponse, VoteResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._

object Vote {

  final case class VoteProps(proposal: ProposalModel,
                             vote: (String)                            => Future[VoteResponse],
                             unvote: (String)                          => Future[VoteResponse],
                             qualifyVote: (String, String)             => Future[QualificationResponse],
                             removeVoteQualification: (String, String) => Future[QualificationResponse])

  final case class VoteState(activeVoteKey: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[VoteProps, VoteState](
        displayName = "Vote",
        getInitialState = (_) => VoteState(activeVoteKey = ""),
        render = { (self) =>
          def vote(key: String): Future[VoteResponse] = {
            val future = self.props.wrapped.vote(key)

            future.onComplete {
              case Failure(_)      =>
              case Success(result) => self.setState(_.copy(activeVoteKey = result.voteKey))
            }
            future
          }

          def unvote(key: String): Future[VoteResponse] = {
            self.setState(_.copy(activeVoteKey = ""))
            val future = self.props.wrapped.unvote(key)

            future.onComplete {
              case Failure(_) =>
              case Success(_) => self.setState(_.copy(activeVoteKey = ""))
            }
            future
          }

          val voteAgree: VoteModel = self.props.wrapped.proposal.votesAgree
          val voteDisagree: VoteModel = self.props.wrapped.proposal.votesDisagree
          val voteNeutral: VoteModel = self.props.wrapped.proposal.votesNeutral

          val votes = self.props.wrapped.proposal.votes.map { vote =>
            vote.key -> vote.count
          }.toMap

          <.ul(^.className := VoteStyles.voteButtonsList)(
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(self.state.activeVoteKey.nonEmpty && self.state.activeVoteKey != voteAgree.key)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  proposalId = self.props.wrapped.proposal.id,
                  votes = votes,
                  vote = voteAgree,
                  handleVote = vote,
                  handleUnvote = unvote,
                  qualifyVote = self.props.wrapped.qualifyVote,
                  removeVoteQualification = self.props.wrapped.removeVoteQualification
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(self.state.activeVoteKey.nonEmpty && self.state.activeVoteKey != voteDisagree.key)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  proposalId = self.props.wrapped.proposal.id,
                  votes = votes,
                  vote = voteDisagree,
                  handleVote = vote,
                  handleUnvote = unvote,
                  qualifyVote = self.props.wrapped.qualifyVote,
                  removeVoteQualification = self.props.wrapped.removeVoteQualification
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(self.state.activeVoteKey.nonEmpty && self.state.activeVoteKey != voteNeutral.key)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  proposalId = self.props.wrapped.proposal.id,
                  votes = votes,
                  vote = voteNeutral,
                  handleVote = vote,
                  handleUnvote = unvote,
                  qualifyVote = self.props.wrapped.qualifyVote,
                  removeVoteQualification = self.props.wrapped.removeVoteQualification
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
    style(display.table, width(240.pxToEm()), margin :=! "0 auto")

  val voteButtonItem: (Boolean) => StyleA = styleF.bool(
    hidden =>
      if (hidden) {
        styleS(display.none)
      } else {
        styleS(
          display.tableCell,
          verticalAlign.top,
          textAlign.center,
          padding :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} ${ThemeStyles.SpacingValue.small.pxToEm().value} 0"
        )
    }
  )
}
