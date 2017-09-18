package org.make.front.components.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.Element
import org.make.front.components.presentationals._
import org.make.front.models.{Vote => VoteModel}
import org.make.front.styles.{FontAwesomeStyles, MakeStyles}

import scalacss.DevDefaults._
import scalacss.internal.ValueT

sealed trait VoteType

case object VoteAgree extends VoteType
case object VoteDisagree extends VoteType
case object VoteNeutral extends VoteType

object Vote {

  final case class Button(color: ValueT[ValueT.Color], thumbFont: StyleA, thumbStyle: Option[Seq[StyleA]])

  final case class VoteProps(voteAgreeStats: VoteModel,
                             voteDisagreeStats: VoteModel,
                             voteNeutralStats: VoteModel,
                             totalVote: Int)

  final case class VoteState(isClickButtonVoteAgree: Boolean,
                             isClickButtonVoteDisagree: Boolean,
                             isClickButtonVoteNeutral: Boolean)

  def isNotSelectedButtonVote(voteState: VoteState): Boolean = {
    !(voteState.isClickButtonVoteAgree || voteState.isClickButtonVoteDisagree || voteState.isClickButtonVoteNeutral)
  }

  def listButtonVote(self: React.Self[VoteProps, VoteState]): Element =
    <.ul(^.className := VoteStyles.voteButtonsList)(
      <.li(^.className := VoteStyles.voteButtonItem)(
        <.VoteButtonComponent(
          ^.wrapped := VoteButton.VoteButtonProps(
            parentNode = self,
            button = Button(color = MakeStyles.Color.green, thumbFont = FontAwesomeStyles.thumbsUp, thumbStyle = None),
            vote = self.props.wrapped.voteAgreeStats,
            voteType = VoteAgree,
            isSelected = false
          )
        )()
      ),
      <.li(^.className := VoteStyles.voteButtonItem)(
        <.VoteButtonComponent(
          ^.wrapped := VoteButton.VoteButtonProps(
            parentNode = self,
            button = Button(color = MakeStyles.Color.red, thumbFont = FontAwesomeStyles.thumbsDown, thumbStyle = None),
            vote = self.props.wrapped.voteDisagreeStats,
            voteType = VoteDisagree,
            isSelected = false
          )
        )()
      ),
      <.li(^.className := VoteStyles.voteButtonItem)(
        <.VoteButtonComponent(
          ^.wrapped := VoteButton.VoteButtonProps(
            parentNode = self,
            button = Button(
              color = MakeStyles.Color.greyVote,
              thumbFont = FontAwesomeStyles.thumbsUp,
              thumbStyle = Some(Seq(VoteStyles.buttonIconNeutralVote))
            ),
            vote = self.props.wrapped.voteNeutralStats,
            voteType = VoteNeutral,
            isSelected = false
          )
        )()
      )
    )

  lazy val reactClass: ReactClass = React.createClass[VoteProps, VoteState](
    getInitialState = (_) =>
      VoteState(isClickButtonVoteAgree = false, isClickButtonVoteDisagree = false, isClickButtonVoteNeutral = false),
    render = (self) =>
      <.div(^.className := VoteStyles.voteWrapper)(
        <.div(^.className := VoteStyles.voteButtonsWrapper)(
          if (isNotSelectedButtonVote(self.state)) {
            listButtonVote(self)
          } else {
            (
              self.state.isClickButtonVoteAgree,
              self.state.isClickButtonVoteDisagree,
              self.state.isClickButtonVoteNeutral
            ) match {
              case (true, false, false) =>
                <.VoteButtonComponent(
                  ^.wrapped := VoteButton.VoteButtonProps(
                    parentNode = self,
                    button =
                      Button(color = MakeStyles.Color.green, thumbFont = FontAwesomeStyles.thumbsUp, thumbStyle = None),
                    vote = self.props.wrapped.voteAgreeStats,
                    voteType = VoteAgree,
                    isSelected = true
                  )
                )()
              case (false, true, false) =>
                <.VoteButtonComponent(
                  ^.wrapped := VoteButton.VoteButtonProps(
                    parentNode = self,
                    button =
                      Button(color = MakeStyles.Color.red, thumbFont = FontAwesomeStyles.thumbsDown, thumbStyle = None),
                    vote = self.props.wrapped.voteDisagreeStats,
                    voteType = VoteDisagree,
                    isSelected = true
                  )
                )()
              case (false, false, true) =>
                <.VoteButtonComponent(
                  ^.wrapped := VoteButton.VoteButtonProps(
                    parentNode = self,
                    button = Button(
                      color = MakeStyles.Color.greyVote,
                      thumbFont = FontAwesomeStyles.thumbsUp,
                      thumbStyle = Some(Seq(VoteStyles.buttonIconNeutralVote))
                    ),
                    vote = self.props.wrapped.voteNeutralStats,
                    voteType = VoteNeutral,
                    isSelected = true
                  )
                )()
              case (_, _, _) =>
            }
          }
        ),
        <.style()(VoteStyles.render[String])
    )
  )

}

object VoteStyles extends StyleSheet.Inline {

  import dsl._

  val voteWrapper: StyleA = style(display.table, width(100.%%), margin :=! "1rem 0")

  val voteButtonsWrapper: StyleA = style(display.tableCell, verticalAlign.top)

  val voteDetailWrapper: StyleA = style(display.tableCell, verticalAlign.top, width(100.%%))

  val voteButtonsList: StyleA = style(textAlign.center)

  val voteButtonItem: StyleA = style(display.inlineBlock, margin :=! "0 1rem", verticalAlign.top)

  val buttonIconNeutralVote: StyleA = style(transform := "rotate(-90deg)")

  val mainStats: StyleA =
    style(lineHeight(1), textAlign.center, margin :=! "1rem 0")

  def mainStatsValue(textColor: ValueT[ValueT.Color]): StyleA =
    style()

  val mainStatsPart: StyleA =
    style()

}
