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
import scalacss.internal.ValueT

object VoteButtonComponent {

  final case class Button(color: ValueT[ValueT.Color], thumbFont: StyleA, thumbStyle: Map[String, Any])

  final case class VoteButtonProps(voteAgreeStats: VoteStats,
                                   voteDisagreeStats: VoteStats,
                                   voteNeutralStats: VoteStats,
                                   totalVote: Int)

  final case class VoteButtonState(isClickButtonVote: Boolean,
                                   button: Button,
                                   voteStats: VoteStats,
                                   qualifType: VoteType)

  def onClickButton(self: Self[VoteButtonProps, VoteButtonState],
                    button: Button,
                    voteStats: VoteStats,
                    qualifType: VoteType)(): Unit = {
    self.setState(_.copy(isClickButtonVote = true, button = button, voteStats = voteStats, qualifType = qualifType))
  }

  def onClickButtonStats(self: Self[VoteButtonProps, VoteButtonState])(): Unit = {
    self.setState(_.copy(isClickButtonVote = false))
  }

  def renderVoteAgreeButton(self: Self[VoteButtonProps, VoteButtonState]): ReactElement = {
    <.a(
      ^.className := VoteButtonStyle.button(MakeStyles.Color.green),
      ^.onClick := onClickButton(
        self,
        Button(MakeStyles.Color.green, FontAwesomeStyles.thumbsUpTransparent, Map.empty),
        self.props.wrapped.voteAgreeStats,
        VoteAgree
      ) _
    )(
      <.i(^.className := Seq(FontAwesomeStyles.thumbsUpTransparent, VoteButtonStyle.thumbs), ^.style := Map.empty)(),
      <.span(^.className := VoteButtonStyle.textButton)(I18n.t("content.proposal.agree"))
    )
  }

  def renderVoteDisagreeButton(self: Self[VoteButtonProps, VoteButtonState]): ReactElement = {
    <.a(
      ^.className := VoteButtonStyle.button(MakeStyles.Color.red),
      ^.onClick := onClickButton(
        self,
        Button(MakeStyles.Color.red, FontAwesomeStyles.thumbsDownTransparent, Map.empty),
        self.props.wrapped.voteDisagreeStats,
        VoteDisagree
      ) _
    )(
      <.i(^.className := Seq(FontAwesomeStyles.thumbsDownTransparent, VoteButtonStyle.thumbs), ^.style := Map.empty)(),
      <.span(^.className := VoteButtonStyle.textButton)(I18n.t("content.proposal.disagree"))
    )
  }

  def renderVoteNeutralButton(self: Self[VoteButtonProps, VoteButtonState]): ReactElement = {
    <.a(
      ^.className := VoteButtonStyle.button(MakeStyles.Color.greyVote),
      ^.onClick := onClickButton(
        self,
        Button(MakeStyles.Color.greyVote, FontAwesomeStyles.thumbsUpTransparent, Map("transform" -> "rotate(-90deg)")),
        self.props.wrapped.voteDisagreeStats,
        VoteNeutral
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
                           color: ValueT[ValueT.Color],
                           thumbFont: StyleA,
                           thumbStyle: Map[String, Any],
                           voteStats: VoteStats): ReactElement = {
    <.div()(
      <.a(^.className := VoteButtonStyle.buttonStats(color), ^.onClick := onClickButtonStats(self) _)(
        <.i(^.className := Seq(thumbFont, VoteButtonStyle.thumbs), ^.style := thumbStyle)()
      ),
      <.div(^.className := VoteButtonStyle.statsBox(color))(
        <.span()(formatToKilo(voteStats.totalVote)),
        <.span(^.className := VoteButtonStyle.pourcentages)(
          s"${formatToKilo(voteStats.totalVote * 100 / self.props.wrapped.totalVote)}%"
        )
      )
    )
  }

  lazy val reactClass: ReactClass = React.createClass[VoteButtonProps, VoteButtonState](
    getInitialState = (_) =>
      VoteButtonState(
        isClickButtonVote = false,
        button = Button(MakeStyles.Color.green, FontAwesomeStyles.thumbsUpTransparent, Map.empty),
        voteStats = VoteStats(totalVote = 0, nbQualifTop = 0, nbQualifMiddle = 0, nbQualifBottom = 0),
        qualifType = VoteAgree
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
              self.state.button.color,
              self.state.button.thumbFont,
              self.state.button.thumbStyle,
              self.state.voteStats
            ),
            <.div(^.className := VoteButtonStyle.proposalQualif)(
              <.QualificationButtonComponent(
                ^.wrapped := QualificationButtonComponent
                  .QualificationButtonProps(self.state.button.color, self.state.qualifType, self.state.voteStats)
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

  def button(buttonColor: ValueT[ValueT.Color]): StyleA = style(
    position.relative,
    border :=! s"0.2rem solid ${buttonColor.value}",
    borderRadius(5.rem),
    fontSize(2.4.rem),
    textAlign.center,
    width(4.8.rem),
    height(4.8.rem),
    margin :=! "0 0.5rem",
    color :=! buttonColor,
    display.flex,
    alignItems.center,
    justifyContent.center,
    backgroundColor :=! "#fff",
    (&.hover)(
      backgroundColor :=! buttonColor,
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

  def buttonStats(buttonColor: ValueT[ValueT.Color]): StyleA = style(
    position.relative,
    border :=! s"0.2rem solid ${buttonColor.value}",
    borderRadius(5.rem),
    fontSize(2.4.rem),
    backgroundColor :=! buttonColor,
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

  def statsBox(textColor: ValueT[ValueT.Color]): StyleA = style(
    position.absolute,
    top(5.rem),
    width(4.8.rem),
    lineHeight(2.rem),
    textAlign.center,
    fontSize(1.6.rem),
    color :=! textColor,
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
