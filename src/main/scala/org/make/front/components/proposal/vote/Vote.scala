package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.VoteButton.VoteButtonProps
import org.make.front.facades.FacebookPixel
import org.make.front.models.{Proposal => ProposalModel, Qualification => QualificationModel, Vote => VoteModel}
import org.make.front.styles._
import org.make.front.styles.utils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSConverters._
import scala.util.{Failure, Success}
import scalacss.DevDefaults._

object Vote {

  // TODO: figure out how to handle that gracefully
  final case class VoteProps(index: Int,
                             updateState: Boolean = true,
                             proposal: ProposalModel,
                             vote: (String)                            => Future[VoteModel],
                             unvote: (String)                          => Future[VoteModel],
                             qualifyVote: (String, String)             => Future[QualificationModel],
                             removeVoteQualification: (String, String) => Future[QualificationModel],
                             guideToVote: Option[String] = None,
                             guideToQualification: Option[String] = None)

  final case class VoteState(votes: Map[String, VoteModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[VoteProps, VoteState](displayName = "Vote", getInitialState = { self =>
        VoteState(votes = self.props.wrapped.proposal.votes.map(vote => vote.key -> vote).toMap)
      }, componentWillReceiveProps = { (self, props) =>
        self.setState(VoteState(votes = props.wrapped.proposal.votes.map(vote => vote.key -> vote).toMap))
      }, render = {
        (self) =>
          def vote(key: String): Future[VoteModel] = {
            FacebookPixel.fbq(
              "trackCustom",
              "click-proposal-vote",
              Map(
                "location" -> "sequence",
                "nature" -> key,
                "proposalId" -> self.props.wrapped.proposal.id.value.toString
              ).toJSDictionary
            )
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
            FacebookPixel.fbq(
              "trackCustom",
              "click-proposal-unvote",
              Map(
                "location" -> "sequence",
                "nature" -> key,
                "proposalId" -> self.props.wrapped.proposal.id.value.toString
              ).toJSDictionary
            )
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
                  updateState = self.props.wrapped.updateState,
                  proposalId = self.props.wrapped.proposal.id,
                  votes = votes,
                  vote = voteAgree,
                  handleVote = vote,
                  handleUnvote = unvote,
                  qualifyVote = self.props.wrapped.qualifyVote,
                  removeVoteQualification = self.props.wrapped.removeVoteQualification,
                  index = self.props.wrapped.index,
                  guideToQualification = self.props.wrapped.guideToQualification
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(hasVoted && !self.state.votes("disagree").hasVoted)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
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
                  index = self.props.wrapped.index
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(hasVoted && !self.state.votes("neutral").hasVoted)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  updateState = self.props.wrapped.updateState,
                  proposalId = self.props.wrapped.proposal.id,
                  votes = votes,
                  vote = voteNeutral,
                  handleVote = vote,
                  handleUnvote = unvote,
                  qualifyVote = self.props.wrapped.qualifyVote,
                  removeVoteQualification = self.props.wrapped.removeVoteQualification,
                  guideToQualification = self.props.wrapped.guideToQualification,
                  index = self.props.wrapped.index
                )
              )()
            ),
            <.style()(VoteStyles.render[String])
          )
      })
}

object VoteStyles extends StyleSheet.Inline {

  import dsl._

  val voteButtonsList: StyleA =
    style(display.table, height(115.pxToEm()), width(240.pxToEm()), margin :=! "0 auto")

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
