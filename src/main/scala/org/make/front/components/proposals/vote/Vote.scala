package org.make.front.components.proposals.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.presentationals._
import org.make.front.components.proposals.vote.VoteButton.VoteButtonProps
import org.make.front.models.{Vote => VoteModel}

import scalacss.DevDefaults._

sealed trait VoteType

case object VoteAgree extends VoteType
case object VoteDisagree extends VoteType
case object VoteNeutral extends VoteType

import scalajs.js.Dynamic.{global => g}

object Vote {

  final case class VoteProps(voteAgreeStats: VoteModel, voteDisagreeStats: VoteModel, voteNeutralStats: VoteModel)

  final case class VoteState()

  lazy val reactClass: ReactClass =
    React
      .createClass[VoteProps, VoteState](
        getInitialState = (_) => VoteState(),
        render = { (self) =>
          def saveVote(vote: VoteModel): Unit = {
            g.console.log(vote.key.toString)
          }

          <.ul()(
            <.li()(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(vote = self.props.wrapped.voteAgreeStats, handleVote = saveVote)
              )()
            ),
            <.li()(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(vote = self.props.wrapped.voteDisagreeStats, handleVote = saveVote)
              )()
            ),
            <.li()(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(vote = self.props.wrapped.voteNeutralStats, handleVote = saveVote)
              )()
            ),
            <.style()(VoteStyles.render[String])
          )
        }
      )

}

object VoteStyles extends StyleSheet.Inline {}
