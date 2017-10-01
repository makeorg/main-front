package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.VoteButton.VoteButtonProps
import org.make.front.models.{ProposalId, Vote => VoteModel}
import org.make.front.styles._
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalService

import scala.concurrent.Future
import scalacss.DevDefaults._

object Vote {

  final case class VoteProps(proposalId: ProposalId,
                             voteAgreeStats: VoteModel,
                             voteDisagreeStats: VoteModel,
                             voteNeutralStats: VoteModel)

  final case class VoteState(activeVoteKey: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[VoteProps, VoteState](
        displayName = "Vote",
        getInitialState = (_) => VoteState(activeVoteKey = ""),
        render = { (self) =>
          def vote(key: String): Future[_] = {
            self.setState(_.copy(activeVoteKey = key))
            ProposalService.vote(proposalId = self.props.wrapped.proposalId, key)
          }

          def unvote(key: String): Future[_] = {
            self.setState(_.copy(activeVoteKey = ""))
            ProposalService.unvote(proposalId = self.props.wrapped.proposalId, key)
          }

          <.ul(^.className := VoteStyles.voteButtonsList)(
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(
                  self.state.activeVoteKey.nonEmpty && self.state.activeVoteKey != self.props.wrapped.voteAgreeStats.key
                )
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  vote = self.props.wrapped.voteAgreeStats,
                  handleVote = vote,
                  handleUnvote = unvote
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(
                  self.state.activeVoteKey.nonEmpty && self.state.activeVoteKey != self.props.wrapped.voteDisagreeStats.key
                )
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  vote = self.props.wrapped.voteDisagreeStats,
                  handleVote = vote,
                  handleUnvote = unvote
                )
              )()
            ),
            <.li(
              ^.className := VoteStyles
                .voteButtonItem(
                  self.state.activeVoteKey.nonEmpty && self.state.activeVoteKey != self.props.wrapped.voteNeutralStats.key
                )
            )(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  vote = self.props.wrapped.voteNeutralStats,
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
