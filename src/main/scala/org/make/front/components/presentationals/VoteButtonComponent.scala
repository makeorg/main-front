package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.facades.I18n
import org.make.front.helpers.NumberFormat.formatToKilo
import org.make.front.models._
import org.make.front.styles.{FontAwesomeStyles, MakeStyles}

import scalacss.DevDefaults._

sealed trait VoteType

case object VoteAgree extends VoteType
case object VoteDisagree extends VoteType
case object VoteNeutral extends VoteType

object VoteButtonComponent {

  final case class Button(voteType: VoteType, thumbFont: StyleA)

  final case class VoteButtonProps(voteAgreeStats: Vote,
                                   voteDisagreeStats: Vote,
                                   voteNeutralStats: Vote,
                                   totalVote: Int)

  final case class VoteButtonState(isClickButtonVote: Boolean, button: Button, voteStats: Vote)

  def onClickButton(self: Self[VoteButtonProps, VoteButtonState], button: Button, voteStats: Vote)(): Unit = {
    self.setState(_.copy(isClickButtonVote = true, button = button, voteStats = voteStats))
  }

  def onClickButtonStats(self: Self[VoteButtonProps, VoteButtonState])(): Unit = {
    self.setState(_.copy(isClickButtonVote = false))
  }

  def renderVoteAgreeButton(self: Self[VoteButtonProps, VoteButtonState]): ReactElement = {
    <.a(
      ^.className := VoteButtonStyle.buttonAgree,
      ^.onClick := onClickButton(self, Button(VoteAgree, FontAwesomeStyles.thumbsUp), self.props.wrapped.voteAgreeStats) _
    )(<.i(^.className := FontAwesomeStyles.thumbsUp)(), <.span()(I18n.t("content.proposal.agree")))
  }

  def renderVoteDisagreeButton(self: Self[VoteButtonProps, VoteButtonState]): ReactElement = {
    <.a(
      ^.className := VoteButtonStyle.buttonDisagree,
      ^.onClick := onClickButton(
        self,
        Button(VoteDisagree, FontAwesomeStyles.thumbsDown),
        self.props.wrapped.voteDisagreeStats
      ) _
    )(<.i(^.className := FontAwesomeStyles.thumbsDown)(), <.span()(I18n.t("content.proposal.disagree")))
  }

  def renderVoteNeutralButton(self: Self[VoteButtonProps, VoteButtonState]): ReactElement = {

    <.a(
      ^.className := Seq(VoteButtonStyle.buttonNeutral),
      ^.onClick := onClickButton(
        self,
        Button(VoteNeutral, FontAwesomeStyles.thumbsUp),
        self.props.wrapped.voteNeutralStats
      ) _
    )(<.i(^.className := FontAwesomeStyles.thumbsUp)(), <.span()(I18n.t("content.proposal.blank")))
  }

  def renderVoteButtonStat(self: Self[VoteButtonProps, VoteButtonState],
                           voteType: VoteType,
                           thumbFont: StyleA,
                           voteStats: Vote): ReactElement = {
    val buttonStatsStyle = voteType match {
      case VoteAgree    => Seq(VoteButtonStyle.buttonVoteAgreeStats)
      case VoteDisagree => Seq(VoteButtonStyle.buttonVoteDisagreeStats)
      case VoteNeutral  => Seq(VoteButtonStyle.buttonVoteNeutralStats)
    }

    val statsBoxStyle = voteType match {
      case VoteAgree    => Seq(VoteButtonStyle.statsBoxAgree)
      case VoteDisagree => Seq(VoteButtonStyle.statsBoxDisagree)
      case VoteNeutral  => Seq(VoteButtonStyle.statsBoxNeutral)
    }

    <.div()(
      <.a(^.className := buttonStatsStyle, ^.onClick := onClickButtonStats(self) _)(
        <.i(^.className := Seq(thumbFont))()
      ),
      <.div(^.className := statsBoxStyle)(
        <.span()(formatToKilo(voteStats.count)),
        <.span()(s"${Proposal.getPercentageVote(voteStats.count, self.props.wrapped.totalVote)}%")
      )
    )
  }

  lazy val reactClass: ReactClass = React.createClass[VoteButtonProps, VoteButtonState](
    getInitialState = (_) =>
      VoteButtonState(
        isClickButtonVote = false,
        button = Button(VoteAgree, FontAwesomeStyles.thumbsUp, Map.empty),
        voteStats = Vote(key = "", qualifications = Seq.empty)
    ),
    render = (self) =>
      <.div()(
        if (!self.state.isClickButtonVote) {
          <.div(^.style := Map("display" -> "flex"))(
            renderVoteAgreeButton(self),
            renderVoteDisagreeButton(self),
            renderVoteNeutralButton(self)
          )
        } else {
          <.div(^.style := Map("display" -> "flex"))(
            renderVoteButtonStat(self, self.state.button.voteType, self.state.button.thumbFont, self.state.voteStats),
            <.div()(
              <.QualificationButtonComponent(
                ^.wrapped := QualificationButtonComponent
                  .QualificationButtonProps(self.state.button.voteType, self.state.voteStats)
              )()
            )
          )
        },
        <.style()(VoteButtonStyle.render[String])
    )
  )

}

object VoteButtonStyle extends StyleSheet.Inline {

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
