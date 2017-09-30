package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.VoteButton.VoteButtonProps
import org.make.front.models.{ProposalId, Vote => VoteModel}
import org.make.front.styles._
import org.make.services.proposal.ProposalService

import scala.concurrent.Future
import scalacss.DevDefaults._

object Vote {

  final case class VoteProps(proposalId: ProposalId,
                             voteAgreeStats: VoteModel,
                             voteDisagreeStats: VoteModel,
                             voteNeutralStats: VoteModel)

  final case class VoteState()

  lazy val reactClass: ReactClass =
    React
      .createClass[VoteProps, VoteState](
        displayName = "Vote",
        getInitialState = (_) => VoteState(),
        render = { (self) =>
          def vote(key: String): Future[_] = {
            ProposalService.vote(proposalId = self.props.wrapped.proposalId, key)
          }

          def unvote(key: String): Future[_] = {
            ProposalService.unvote(proposalId = self.props.wrapped.proposalId, key)
          }

          <.ul(^.className := VoteStyles.voteButtonsList)(
            <.li(^.className := VoteStyles.voteButtonItem)(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  vote = self.props.wrapped.voteAgreeStats,
                  handleVote = vote,
                  handleUnvote = unvote
                )
              )()
            ),
            <.li(^.className := VoteStyles.voteButtonItem)(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(
                  vote = self.props.wrapped.voteDisagreeStats,
                  handleVote = vote,
                  handleUnvote = unvote
                )
              )()
            ),
            <.li(^.className := VoteStyles.voteButtonItem)(
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

  val voteButtonsList: StyleA = style(
    margin :=! s"0 ${(ThemeStyles.SpacingValue.small - ThemeStyles.SpacingValue.smaller).pxToEm().value}",
    textAlign.center
  )

  val voteButtonItem: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.middle,
      margin :=! s"${ThemeStyles.SpacingValue.smaller.pxToEm().value} ${ThemeStyles.SpacingValue.smaller.pxToEm().value} 0"
    )
}
