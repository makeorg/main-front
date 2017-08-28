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

  final case class Button(voteType: VoteType, thumbFont: StyleA, thumbStyle: Map[String, Any])

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
      ^.className := Seq(VoteButtonStyle.button, VoteButtonStyle.buttonAgree),
      ^.onClick := onClickButton(
        self,
        Button(VoteAgree, FontAwesomeStyles.thumbsUpTransparent, Map.empty),
        self.props.wrapped.voteAgreeStats
      ) _
    )(
      <.i(^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, VoteButtonStyle.thumbs), ^.style := Map.empty)(),
      <.span(^.className := VoteButtonStyle.textButton)(I18n.t("content.proposal.agree"))
    )
  }

  def renderVoteDisagreeButton(self: Self[VoteButtonProps, VoteButtonState]): ReactElement = {
    <.a(
      ^.className := Seq(VoteButtonStyle.button, VoteButtonStyle.buttonDisagree),
      ^.onClick := onClickButton(
        self,
        Button(VoteDisagree, FontAwesomeStyles.thumbsDownTransparent, Map.empty),
        self.props.wrapped.voteDisagreeStats
      ) _
    )(
      <.i(^.className := Seq(FontAwesomeStyles.thumbsDownTransparent, VoteButtonStyle.thumbs), ^.style := Map.empty)(),
      <.span(^.className := VoteButtonStyle.textButton)(I18n.t("content.proposal.disagree"))
    )
  }

  def renderVoteNeutralButton(self: Self[VoteButtonProps, VoteButtonState]): ReactElement = {
    <.a(
      ^.className := Seq(VoteButtonStyle.button, VoteButtonStyle.buttonNeutral),
      ^.onClick := onClickButton(
        self,
        Button(VoteNeutral, FontAwesomeStyles.thumbsUpTransparent, Map("transform" -> "rotate(-90deg)")),
        self.props.wrapped.voteNeutralStats
      ) _
    )(
      <.i(
        ^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, VoteButtonStyle.thumbs),
        ^.style := Map("transform" -> "rotate(-90deg)")
      )(),
      <.span(^.className := VoteButtonStyle.textButton)(I18n.t("content.proposal.blank"))
    )
  }

  def renderVoteButtonStat(self: Self[VoteButtonProps, VoteButtonState],
                           voteType: VoteType,
                           thumbFont: StyleA,
                           thumbStyle: Map[String, Any],
                           voteStats: Vote): ReactElement = {
    val buttonStatsStyle = voteType match {
      case VoteAgree    => Seq(VoteButtonStyle.buttonStats, VoteButtonStyle.buttonVoteAgreeStats)
      case VoteDisagree => Seq(VoteButtonStyle.buttonStats, VoteButtonStyle.buttonVoteDisagreeStats)
      case VoteNeutral  => Seq(VoteButtonStyle.buttonStats, VoteButtonStyle.buttonVoteNeutralStats)
    }

    val statsBoxStyle = voteType match {
      case VoteAgree    => Seq(VoteButtonStyle.statsBox, VoteButtonStyle.statsBoxAgree)
      case VoteDisagree => Seq(VoteButtonStyle.statsBox, VoteButtonStyle.statsBoxDisagree)
      case VoteNeutral  => Seq(VoteButtonStyle.statsBox, VoteButtonStyle.statsBoxNeutral)
    }

    <.div()(
      <.a(^.className := buttonStatsStyle, ^.onClick := onClickButtonStats(self) _)(
        <.i(^.className := Seq(thumbFont, VoteButtonStyle.thumbs), ^.style := thumbStyle)()
      ),
      <.div(^.className := statsBoxStyle)(
        <.span()(formatToKilo(voteStats.count)),
        <.span(^.className := VoteButtonStyle.pourcentages)(
          s"${Proposal.getPercentageVote(voteStats.count, self.props.wrapped.totalVote)}%"
        )
      )
    )
  }

  lazy val reactClass: ReactClass = React.createClass[VoteButtonProps, VoteButtonState](
    getInitialState = (_) =>
      VoteButtonState(
        isClickButtonVote = false,
        button = Button(VoteAgree, FontAwesomeStyles.thumbsUpTransparent, Map.empty),
        voteStats = Vote(key = "", qualifications = Seq.empty)
    ),
    render = (self) =>
      <.div(^.className := VoteButtonStyle.proposalButton)(
        if (!self.state.isClickButtonVote) {
          <.div(^.style := Map("display" -> "flex"))(
            renderVoteAgreeButton(self),
            renderVoteDisagreeButton(self),
            renderVoteNeutralButton(self)
          )
        } else {
          <.div(^.style := Map("display" -> "flex"))(
            renderVoteButtonStat(
              self,
              self.state.button.voteType,
              self.state.button.thumbFont,
              self.state.button.thumbStyle,
              self.state.voteStats
            ),
            <.div(^.className := VoteButtonStyle.proposalQualif)(
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

  val proposalButton: StyleA =
    style(position.relative, display.flex, justifyContent.center, alignItems.flexStart, minHeight(12.rem))

//  def button(buttonColor: ValueT[ValueT.Color]): StyleA = style(

  val buttonAgree: StyleA = style(
    border :=! s"0.2rem solid ${MakeStyles.Color.green.value}",
    color :=! MakeStyles.Color.green,
    (&.hover)(backgroundColor :=! MakeStyles.Color.green)
  )

  val buttonDisagree: StyleA = style(
    border :=! s"0.2rem solid ${MakeStyles.Color.red.value}",
    color :=! MakeStyles.Color.red,
    (&.hover)(backgroundColor :=! MakeStyles.Color.red)
  )

  val buttonNeutral: StyleA = style(
    border :=! s"0.2rem solid ${MakeStyles.Color.greyVote.value}",
    color :=! MakeStyles.Color.greyVote,
    (&.hover)(backgroundColor :=! MakeStyles.Color.greyVote)
  )

  val button: StyleA = style(
    position.relative,
    borderRadius(5.rem),
    fontSize(2.4.rem),
    textAlign.center,
    width(4.8.rem),
    height(4.8.rem),
    margin :=! "0 0.5rem",
    display.flex,
    alignItems.center,
    justifyContent.center,
    backgroundColor :=! "#fff",
    (&.hover)(
      color :=! "#fff",
      boxShadow := "0 -0.1rem 0.6rem 0 rgba(0, 0, 0, 0.3)",
      unsafeChild("span")(
        visibility.visible,
        position.absolute,
        padding :=! "0 0.5rem",
        backgroundColor :=! "black",
        color :=! "#fff",
        textAlign.center,
        borderRadius(0.6.rem),
        fontSize(1.4.rem),
        zIndex(1),
        top(111.%%)
      )
    )
  )

  val buttonVoteAgreeStats: StyleA =
    style(border :=! s"0.2rem solid ${MakeStyles.Color.green.value}", backgroundColor :=! MakeStyles.Color.green.value)

  val buttonVoteDisagreeStats: StyleA =
    style(border :=! s"0.2rem solid ${MakeStyles.Color.red.value}", backgroundColor :=! MakeStyles.Color.red.value)

  val buttonVoteNeutralStats: StyleA = style(
    border :=! s"0.2rem solid ${MakeStyles.Color.greyVote.value}",
    backgroundColor :=! MakeStyles.Color.greyVote.value
  )

  val buttonStats: StyleA = style(
    position.relative,
    borderRadius(5.rem),
    fontSize(2.4.rem),
    color :=! "#fff",
    textAlign.center,
    width(4.8.rem),
    height(4.8.rem),
    display.flex,
    alignItems.center,
    justifyContent.center,
    (&.hover)(boxShadow := "0 -0.1rem 0.6rem 0 rgba(0, 0, 0, 0.3)", color :=! "#fff")
  )

  val thumbs: StyleA = style(position.absolute)

  val textButton: StyleA = style(
    visibility.hidden,
    whiteSpace.nowrap,
    (&.after)(
      content := "''",
      position.absolute,
      bottom(100.%%),
      left(50.%%),
      marginLeft((-0.5).rem),
      borderWidth(0.5.rem),
      borderStyle.solid,
      borderColor :=! "transparent transparent black transparent"
    )
  )

  val statsBoxAgree: StyleA = style(color :=! MakeStyles.Color.green)

  val statsBoxDisagree: StyleA = style(color :=! MakeStyles.Color.red)

  val statsBoxNeutral: StyleA = style(color :=! MakeStyles.Color.greyVote)

  val statsBox: StyleA = style(
    position.absolute,
    top(5.rem),
    width(4.8.rem),
    lineHeight(2.rem),
    textAlign.center,
    fontSize(1.6.rem),
    MakeStyles.Font.circularStdBold
  )

  val pourcentages: StyleA = style(
    marginTop(-0.4.rem),
    display.block,
    MakeStyles.Font.circularStdBook,
    fontSize(1.4.rem),
    fontWeight :=! "300",
    color :=! "rgba(0, 0, 0, 0.3)"
  )

  val proposalQualif: StyleA = style(position.relative, width(15.rem), marginLeft(1.rem), flex := "0 0 auto")
}
