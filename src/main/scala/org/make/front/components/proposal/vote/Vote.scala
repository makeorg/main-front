package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.VoteButton.VoteButtonProps
import org.make.front.models.{Proposal => ProposalModel, Vote => VoteModel}
import org.make.front.styles._
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.VoteResponse

import scala.concurrent.Future
import scalacss.DevDefaults._

object Vote {

  final case class VoteProps(proposal: ProposalModel,
                             vote: (String)   => Future[VoteResponse],
                             unvote: (String) => Future[VoteResponse])

  final case class VoteState(activeVoteKey: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[VoteProps, VoteState](
        displayName = "Vote",
        getInitialState = (_) => VoteState(activeVoteKey = ""),
        render = { (self) =>
          def vote(key: String): Future[_] = {
            self.setState(_.copy(activeVoteKey = key))
            self.props.wrapped.vote(key)
          }

          def unvote(key: String): Future[_] = {
            self.setState(_.copy(activeVoteKey = ""))
            self.props.wrapped.unvote(key)
          }

          val voteAgree: VoteModel = self.props.wrapped.proposal.votesAgree
          val voteDisagree: VoteModel = self.props.wrapped.proposal.votesDisagree
          val voteNeutral: VoteModel = self.props.wrapped.proposal.votesNeutral

          <.ul(^.className := VoteStyles.voteButtonsList)(
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(self.state.activeVoteKey.nonEmpty && self.state.activeVoteKey != voteAgree.key)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  votes = self.props.wrapped.proposal.votes,
                  vote = voteAgree,
                  handleVote = vote,
                  handleUnvote = unvote
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(self.state.activeVoteKey.nonEmpty && self.state.activeVoteKey != voteDisagree.key)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  votes = self.props.wrapped.proposal.votes,
                  vote = voteDisagree,
                  handleVote = vote,
                  handleUnvote = unvote
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(self.state.activeVoteKey.nonEmpty && self.state.activeVoteKey != voteNeutral.key)
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  votes = self.props.wrapped.proposal.votes,
                  vote = voteNeutral,
                  handleVote = vote,
                  handleUnvote = unvote
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
