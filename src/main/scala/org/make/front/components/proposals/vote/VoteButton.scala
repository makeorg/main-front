package org.make.front.components.proposals.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.Components._
import org.make.front.components.proposals.vote.QualificateVote.QualificateVoteProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Vote => VoteModel}
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object VoteButton {

  case class VoteButtonProps(vote: VoteModel, handleVote: (VoteModel) => Unit)

  case class VoteButtonState(isSelected: Boolean)

  lazy val reactClass: ReactClass = React.createClass[VoteButtonProps, VoteButtonState](
    getInitialState = (_) => VoteButtonState(isSelected = false),
    render = (self) => {

      def vote() = (e: SyntheticEvent) => {
        e.preventDefault()
        self.setState(_.copy(isSelected = !self.state.isSelected))
        self.props.wrapped.handleVote(self.props.wrapped.vote)
      }

      val buttonClasses =
        Seq(
          VoteButtonStyles.button.htmlClass,
          if (!self.state.isSelected) VoteButtonStyles.buttonNotActivated.htmlClass else "",
          self.props.wrapped.vote.key match {
            case "agree" =>
              VoteButtonStyles.agree.htmlClass + " " + (if (self.state.isSelected)
                                                          VoteButtonStyles.agreeActivated.htmlClass
                                                        else "")
            case "disagree" =>
              VoteButtonStyles.disagree.htmlClass + " " + (if (self.state.isSelected)
                                                             VoteButtonStyles.disagreeActivated.htmlClass
                                                           else "")
            case "neutral" =>
              VoteButtonStyles.neutral.htmlClass + " " + (if (self.state.isSelected)
                                                            VoteButtonStyles.neutralActivated.htmlClass
                                                          else "")
            case _ =>
              VoteButtonStyles.neutral.htmlClass + " " + (if (self.state.isSelected)
                                                            VoteButtonStyles.neutralActivated.htmlClass
                                                          else "")
          }
        ).mkString(" ")
      <.div()(
        <.button(^.className := buttonClasses, ^.onClick := vote())(
          <.span(^.className := VoteButtonStyles.label)(
            <.span(^.className := TextStyles.smallerText)(
              unescape(I18n.t(s"content.proposal.${self.props.wrapped.vote.key}"))
            )
          )
        ),
        <.QualificateVoteComponent(^.wrapped := QualificateVoteProps(vote = self.props.wrapped.vote))(),
        <.style()(VoteButtonStyles.render[String])
      )
    }
  )
}

object VoteButtonStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val button: StyleA = style(
    position.relative,
    display.block,
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
    unsafeChild("[class$=\"label\"]")(opacity(0)),
    (&.hover)(
      color(ThemeStyles.TextColor.white),
      backgroundColor(ThemeStyles.TextColor.base),
      boxShadow := s"0 ${2.pxToEm().value} ${5.pxToEm().value} 0 rgba(0, 0, 0, .5)"
    )
  )

  val buttonNotActivated: StyleA = style(
    unsafeChild("[class$=\"label\"]")(transition := "opacity .2s ease-in-out"),
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
    boxShadow := s"0 ${2.pxToEm().value} ${5.pxToEm().value} 0 rgba(0, 0, 0, .5)"
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
    boxShadow := s"0 ${2.pxToEm().value} ${5.pxToEm().value} 0 rgba(0, 0, 0, .5)"
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
    boxShadow := s"0 ${2.pxToEm().value} ${5.pxToEm().value} 0 rgba(0, 0, 0, .5)"
  )
}
