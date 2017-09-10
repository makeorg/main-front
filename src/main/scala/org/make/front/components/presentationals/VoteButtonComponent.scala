package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.presentationals.VoteComponent.{Button, VoteProps, VoteState}
import org.make.front.facades.Translate.TranslateVirtualDOMElements
import org.make.front.models.Vote

import scalacss.DevDefaults._

object VoteButtonComponent {

  def onClickButtonVote(self: Self[VoteButtonProps, Unit],
                        parentNode: Self[VoteProps, VoteState],
                        vote: Vote)(): Unit = {
    vote match {
      case v if v == parentNode.props.wrapped.voteAgreeStats =>
        parentNode.setState(
          VoteState(
            isClickButtonVoteAgree = !self.props.wrapped.isSelected,
            isClickButtonVoteDisagree = false,
            isClickButtonVoteNeutral = false
          )
        )
      case v if v == parentNode.props.wrapped.voteDisagreeStats =>
        parentNode.setState(
          VoteState(
            isClickButtonVoteAgree = false,
            isClickButtonVoteDisagree = !self.props.wrapped.isSelected,
            isClickButtonVoteNeutral = false
          )
        )
      case v if v == parentNode.props.wrapped.voteNeutralStats =>
        parentNode.setState(
          VoteState(
            isClickButtonVoteAgree = false,
            isClickButtonVoteDisagree = false,
            isClickButtonVoteNeutral = !self.props.wrapped.isSelected
          )
        )
      case _ =>
    }
  }

  final case class VoteButtonProps(parentNode: Self[VoteProps, VoteState],
                                   button: Button,
                                   vote: Vote,
                                   voteType: VoteType,
                                   isSelected: Boolean)

  def displayQualification(self: Self[VoteButtonProps, Unit]): ReactElement = {
    <.QualificationButtonComponent(
      ^.wrapped := QualificationButtonComponent
        .QualificationButtonProps(voteType = self.props.wrapped.voteType, qualifStats = self.props.wrapped.vote)
    )()
  }

  lazy val reactClass: ReactClass = React.createClass[VoteButtonProps, Unit](render = (self) => {
    val buttonStatsStyle = self.props.wrapped.voteType match {
      case VoteAgree    => VoteButtonStyles.buttonAgree
      case VoteDisagree => VoteButtonStyles.buttonDisagree
      case VoteNeutral  => VoteButtonStyles.buttonNeutral
    }

    <.div()(
      <.button(
        ^.onClick := onClickButtonVote(self, self.props.wrapped.parentNode, self.props.wrapped.vote) _,
        ^.className := buttonStatsStyle
      )(
        <.i(
          ^.className :=
            Seq(self.props.wrapped.button.thumbFont) ++ self.props.wrapped.button.thumbStyle
              .getOrElse(Seq())
        )(),
        <.Translate(^.value := s"content.proposal.${self.props.wrapped.vote.key}")()
      ),
      if (self.props.wrapped.isSelected)
        displayQualification(self),
      <.style()(VoteButtonStyles.render[String])
    )
  })

}

object VoteButtonStyles extends StyleSheet.Inline {

  import dsl._

  val buttonAgree: StyleA = style()
  val buttonDisagree: StyleA = style()
  val buttonNeutral: StyleA = style()

  val buttonVoteAgreeStats: StyleA = style()
  val buttonVoteDisagreeStats: StyleA = style()
  val buttonVoteNeutralStats: StyleA = style()

  val statsBoxAgree: StyleA = style()
  val statsBoxDisagree: StyleA = style()
  val statsBoxNeutral: StyleA = style()

}
