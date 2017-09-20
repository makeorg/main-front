package org.make.front.components.proposals.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.presentationals._
import org.make.front.components.proposals.vote.VoteButton.VoteButtonProps
import org.make.front.models.{Vote => VoteModel}
import org.make.front.styles.ThemeStyles

import scalacss.DevDefaults._
import scalacss.internal.Length

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

          <.ul(^.className := VoteStyles.voteButtonsList)(
            <.li(^.className := VoteStyles.voteButtonItem)(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(vote = self.props.wrapped.voteAgreeStats, handleVote = saveVote)
              )()
            ),
            <.li(^.className := VoteStyles.voteButtonItem)(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(vote = self.props.wrapped.voteDisagreeStats, handleVote = saveVote)
              )()
            ),
            <.li(^.className := VoteStyles.voteButtonItem)(
              <.VoteButtonComponent(
                ^.wrapped := VoteButtonProps(vote = self.props.wrapped.voteNeutralStats, handleVote = saveVote)
              )()
            ),
            <.style()(VoteStyles.render[String])
          )
        }
      )

}

object VoteStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val voteButtonsList: StyleA = style(
    margin := s"0 ${(ThemeStyles.SpacingValue.small - ThemeStyles.SpacingValue.smaller).pxToEm().value}",
    textAlign.center
  )

  val voteButtonItem: StyleA =
    style(
      display.inlineBlock,
      verticalAlign.middle,
      margin := s"${ThemeStyles.SpacingValue.smaller.pxToEm().value} ${ThemeStyles.SpacingValue.smaller.pxToEm().value} 0"
    )
}
