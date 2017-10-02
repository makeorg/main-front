package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.QualificateVote.QualificateVoteProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Vote => VoteModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

object VoteButton {

  case class VoteButtonProps(vote: VoteModel, handleVote: (String) => Future[_], handleUnvote: (String) => Future[_])

  case class VoteButtonState(isActivated: Boolean, resultsOfVoteAreDisplayed: Boolean)

  lazy val reactClass: ReactClass = React.createClass[VoteButtonProps, VoteButtonState](
    displayName = "VoteButton",
    getInitialState = (_) => VoteButtonState(isActivated = false, resultsOfVoteAreDisplayed = false),
    render = (self) => {

      def vote() = (e: SyntheticEvent) => {
        e.preventDefault()
        if (self.state.isActivated) {
          self.setState(_.copy(isActivated = false))
          self.props.wrapped.handleUnvote(self.props.wrapped.vote.key)
          /*self.props.wrapped.handleUnvote(self.props.wrapped.vote.key).onComplete {
            case Success(_) => self.setState(_.copy(isSelected = false))
            case Failure(_) =>
          }*/
        } else {
          self.setState(_.copy(isActivated = true))
          self.props.wrapped.handleVote(self.props.wrapped.vote.key)
          /*self.props.wrapped.handleVote(self.props.wrapped.vote.key).onComplete {
            case Success(_) => self.setState(_.copy(isSelected = true))
            case Failure(_) =>
          }*/
        }
      }

      def toggleResultsOfVote() = (e: SyntheticEvent) => {
        e.preventDefault()
        self.setState(_.copy(resultsOfVoteAreDisplayed = !self.state.resultsOfVoteAreDisplayed))
      }

      val buttonClasses =
        Seq(
          VoteButtonStyles.button.htmlClass,
          if (!self.state.isActivated) VoteButtonStyles.buttonNotActivated.htmlClass else "",
          self.props.wrapped.vote.key match {
            case "agree" =>
              VoteButtonStyles.agree.htmlClass + " " + (if (self.state.isActivated)
                                                          VoteButtonStyles.agreeActivated.htmlClass
                                                        else "")
            case "disagree" =>
              VoteButtonStyles.disagree.htmlClass + " " + (if (self.state.isActivated)
                                                             VoteButtonStyles.disagreeActivated.htmlClass
                                                           else "")
            case "neutral" =>
              VoteButtonStyles.neutral.htmlClass + " " + (if (self.state.isActivated)
                                                            VoteButtonStyles.neutralActivated.htmlClass
                                                          else "")
            case _ =>
              VoteButtonStyles.neutral.htmlClass + " " + (if (self.state.isActivated)
                                                            VoteButtonStyles.neutralActivated.htmlClass
                                                          else "")
          }
        ).mkString(" ")

      val totalOfVotesClasses =
        Seq(VoteButtonStyles.totalOfVotes.htmlClass, self.props.wrapped.vote.key match {
          case "agree" =>
            VoteButtonStyles.totalOfAgreeVotes.htmlClass
          case "disagree" =>
            VoteButtonStyles.totalOfDisagreeVotes.htmlClass
          case "neutral" =>
            VoteButtonStyles.totalOfNeutralVotes.htmlClass
          case _ =>
            VoteButtonStyles.totalOfNeutralVotes.htmlClass
        }).mkString(" ")

      <.div(^.className := VoteButtonStyles.wrapper(self.state.isActivated))(
        <.div(^.className := VoteButtonStyles.buttonAndResultsOfCurrentVoteWrapper)(
          <.button(^.className := buttonClasses, ^.onClick := vote())(
            <.span(^.className := VoteButtonStyles.label)(
              <.span(^.className := TextStyles.smallerText)(
                unescape(I18n.t(s"content.proposal.${self.props.wrapped.vote.key}"))
              )
            )
          ),
          if (self.state.isActivated) {
            Seq(
              <.p(^.className := totalOfVotesClasses)("6,5K"),
              <.p(^.className := Seq(VoteButtonStyles.partOfVotes))("(84%)"),
              <.button(
                ^.className := Seq(
                  VoteButtonStyles.resultsOfVoteAccessButton,
                  FontAwesomeStyles.fa,
                  FontAwesomeStyles.barChart
                ),
                ^.onClick := toggleResultsOfVote()
              )()
            )
          }
        ),
        <.div(^.className := VoteButtonStyles.qualificateVoteAndResultsOfVoteWrapper)(
          <.div(^.className := VoteButtonStyles.qualificateVoteAndResultsOfVoteInnerWrapper(self.state.isActivated))(
            if (self.state.isActivated && self.state.resultsOfVoteAreDisplayed) {
              <.div(^.className := VoteButtonStyles.resultsOfVoteWrapper)(
                <.p()("6,5K (84%)"),
                <.p()("851 (11%)"),
                <.p()("387 (5%)")
              )
            } else {
              <.QualificateVoteComponent(^.wrapped := QualificateVoteProps(vote = self.props.wrapped.vote))()
            }
          )
        ),
        <.style()(VoteButtonStyles.render[String])
      )
    }
  )
}

object VoteButtonStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: (Boolean) => StyleA = styleF.bool(
    voted =>
      if (voted) {
        styleS(position.relative, display.table, height(100.%%), width(100.%%))
      } else {
        styleS(position.relative, display.table, height(100.%%))
    }
  )

  val buttonAndResultsOfCurrentVoteWrapper: StyleA =
    style(display.tableCell, height(100.%%), verticalAlign.top, width(50.pxToEm()))

  val qualificateVoteAndResultsOfVoteWrapper: StyleA = style(display.tableCell, height(100.%%), verticalAlign.top)

  val qualificateVoteAndResultsOfVoteInnerWrapper: (Boolean) => StyleA = styleF.bool(
    voted =>
      if (voted) {
        styleS(
          position.relative,
          display.table,
          height(100.%%),
          width(100.%%),
          paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm()),
          overflow.hidden
        )
      } else {
        styleS(overflow.hidden, width(0.pxToEm()))
    }
  )

  val resultsOfVoteWrapper: StyleA =
    style(
      position.relative,
      display.tableCell,
      verticalAlign.middle,
      width(100.%%),
      height(100.%%),
      padding(ThemeStyles.SpacingValue.smaller.pxToEm()),
      borderRadius :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} ${ThemeStyles.SpacingValue.small
        .pxToEm()
        .value} ${ThemeStyles.SpacingValue.small.pxToEm().value} 0",
      backgroundColor(rgb(74, 74, 74)),
      color(ThemeStyles.TextColor.white),
      (&.after)(
        content := "''",
        position.absolute,
        right(100.%%),
        bottom(0.%%),
        borderRight :=! s"${5.pxToEm().value} solid ${rgb(74, 74, 74).value}",
        borderBottom :=! s"${5.pxToEm().value} solid ${rgb(74, 74, 74).value}",
        borderLeft :=! s"${5.pxToEm().value} solid transparent",
        borderTop :=! s"${5.pxToEm().value} solid transparent"
      )
    )

  val button: StyleA = style(
    display.block,
    position.relative,
    width(50.pxToEm()),
    height(50.pxToEm()),
    boxSizing.borderBox,
    border :=! s"1px solid ${ThemeStyles.TextColor.base.value}",
    borderRadius(50.%%),
    textAlign.center,
    backgroundColor.transparent,
    boxShadow := "0 0 0 0 rgba(0, 0, 0, 0)",
    transition := "color .2s ease-in-out, background-color .2s ease-in-out, box-shadow .2s ease-in-out",
    (&.before)(
      content := "'\\f087'",
      position.absolute,
      top(`0`),
      left(`0`),
      lineHeight(48.pxToEm(24)),
      width(48.pxToEm(24)),
      fontSize(24.pxToEm()),
      ThemeStyles.Font.fontAwesome,
      textAlign.center
    ),
    unsafeChild("[class$=\"label\"]")(display.none),
    (&.hover)(
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.TextColor.base),
      boxShadow := s"0 ${2.pxToEm().value} ${5.pxToEm().value} 0 rgba(0, 0, 0, .5)"
    )
  )

  val buttonNotActivated: StyleA = style(
    unsafeChild("[class$=\"label\"]")(display.block, opacity(0), transition := "opacity .2s ease-in-out"),
    (&.hover)(unsafeChild("[class$=\"label\"]")(opacity(1)))
  )

  val label: StyleA = style(
    position.absolute,
    top(100.%%),
    left(50.%%),
    transform := "translateX(-50%)",
    zIndex(1),
    marginTop(12.pxToEm()),
    padding :=! s"0 ${15.pxToEm().value}",
    lineHeight(24.pxToEm()),
    textAlign.center,
    whiteSpace.nowrap,
    color :=! "#fff",
    backgroundColor :=! "#333",
    (&.after)(
      content := "''",
      position.absolute,
      left(50.%%),
      bottom(100.%%),
      marginLeft(-6.pxToEm()),
      borderRight :=! s"${6.pxToEm().value} solid transparent",
      borderBottom :=! s"${6.pxToEm().value} solid #333",
      borderLeft :=! s"${6.pxToEm().value} solid transparent"
    )
  )

  val agree: StyleA = style(
    color(ThemeStyles.ThemeColor.positive),
    borderColor(ThemeStyles.ThemeColor.positive),
    (&.hover)(backgroundColor(ThemeStyles.ThemeColor.positive), color(ThemeStyles.TextColor.white)),
    (&.before)(content := s"'\\F087'")
  )

  val agreeActivated: StyleA = style(
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.ThemeColor.positive),
    boxShadow := "0 0 0 0 rgba(0, 0, 0, 0)"
  )

  val disagree: StyleA = style(
    color(ThemeStyles.ThemeColor.negative),
    borderColor(ThemeStyles.ThemeColor.negative),
    (&.hover)(backgroundColor(ThemeStyles.ThemeColor.negative), color(ThemeStyles.TextColor.white)),
    (&.before)(content := s"'\\F088'")
  )

  val disagreeActivated: StyleA = style(
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.ThemeColor.negative),
    boxShadow := "0 0 0 0 rgba(0, 0, 0, 0)"
  )

  val neutral: StyleA = style(
    color(ThemeStyles.TextColor.grey),
    borderColor(ThemeStyles.TextColor.grey),
    (&.hover)(backgroundColor(ThemeStyles.TextColor.grey), color(ThemeStyles.TextColor.white)),
    (&.before)(content := s"'\\F087'", transform := s"rotate(-90deg)")
  )

  val neutralActivated: StyleA = style(
    color(ThemeStyles.TextColor.white),
    backgroundColor(ThemeStyles.TextColor.grey),
    boxShadow := "0 0 0 0 rgba(0, 0, 0, 0)"
  )

  val totalOfVotes: StyleA =
    style(
      marginTop(5.pxToEm(13)),
      ThemeStyles.Font.circularStdBold,
      fontSize(13.pxToEm()),
      lineHeight(1),
      textAlign.center
    )

  val totalOfAgreeVotes: StyleA =
    style(color(ThemeStyles.ThemeColor.positive))

  val totalOfDisagreeVotes: StyleA =
    style(color(ThemeStyles.ThemeColor.negative))

  val totalOfNeutralVotes: StyleA =
    style(color(ThemeStyles.TextColor.grey))

  val partOfVotes: StyleA =
    style(
      ThemeStyles.Font.circularStdBook,
      fontSize(11.pxToEm()),
      lineHeight(1),
      textAlign.center,
      color(ThemeStyles.TextColor.grey)
    )

  val resultsOfVoteAccessButton: StyleA =
    style(
      height(16.pxToEm(10)),
      width(50.pxToEm(10)),
      marginTop(5.pxToEm(10)),
      boxSizing.borderBox,
      borderRadius(8.pxToEm(10)),
      padding :=! s"0 ${8.pxToEm(10).value}",
      fontSize(10.pxToEm()),
      textAlign.center,
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent)
    )
}
