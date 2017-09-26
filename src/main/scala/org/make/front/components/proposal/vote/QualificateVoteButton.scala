package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.models.{Qualification => QualificationModel, Vote => VoteModel}
import org.make.front.styles.{TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

import org.make.front.helpers.NumberFormat.formatToKilo

object QualificateVoteButton {

  case class QualificateVoteButtonProps(vote: VoteModel,
                                        qualification: QualificationModel,
                                        handleQualificateVote: (QualificationModel) => Unit)

  case class QualificateVoteButtonState(isSelected: Boolean)

  lazy val reactClass: ReactClass = React.createClass[QualificateVoteButtonProps, QualificateVoteButtonState](
    getInitialState = (_) => QualificateVoteButtonState(isSelected = false),
    render = (self) => {

      def QualificateVote() = (e: SyntheticEvent) => {
        e.preventDefault()
        self.setState(_.copy(isSelected = !self.state.isSelected))
        self.props.wrapped.handleQualificateVote(self.props.wrapped.qualification)
      }

      val buttonClasses =
        Seq(QualificateVoteButtonStyles.button.htmlClass, self.props.wrapped.vote.key match {
          case "agree" =>
            QualificateVoteButtonStyles.agree.htmlClass + " " + (if (self.state.isSelected)
                                                                   QualificateVoteButtonStyles.agreeActivated.htmlClass
                                                                 else "")
          case "disagree" =>
            QualificateVoteButtonStyles.disagree.htmlClass + " " + (if (self.state.isSelected)
                                                                      QualificateVoteButtonStyles.disagreeActivated.htmlClass
                                                                    else "")
          case "neutral" =>
            QualificateVoteButtonStyles.neutral.htmlClass + " " + (if (self.state.isSelected)
                                                                     QualificateVoteButtonStyles.neutralActivated.htmlClass
                                                                   else "")
          case _ =>
            QualificateVoteButtonStyles.neutral.htmlClass + " " + (if (self.state.isSelected)
                                                                     QualificateVoteButtonStyles.neutralActivated.htmlClass
                                                                   else "")
        }).mkString(" ")

      <.button(^.className := buttonClasses, ^.onClick := QualificateVote())(
        <.span(
          ^.className := Seq(TextStyles.smallerText, TextStyles.boldText, QualificateVoteButtonStyles.label),
          ^.dangerouslySetInnerHTML := (I18n.t(s"content.proposal.${self.props.wrapped.qualification.key}"))
        )(),
        if (!self.state.isSelected)
          <.span(
            ^.className := Seq(QualificateVoteButtonStyles.votesCounter, TextStyles.mediumText, TextStyles.boldText)
          )(I18n.t("content.proposal.plusOne"))
        else
          <.span(
            ^.className := Seq(
              QualificateVoteButtonStyles.votesCounter,
              QualificateVoteButtonStyles.selectedQualificationVotesCounter,
              TextStyles.mediumText,
              TextStyles.boldText
            )
          )(formatToKilo(self.props.wrapped.qualification.count)),
        <.style()(QualificateVoteButtonStyles.render[String])
      )

    }
  )

}

object QualificateVoteButtonStyles extends StyleSheet.Inline {

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
    height(30.pxToEm()),
    width(100.%%),
    boxSizing.borderBox,
    padding :=! s"0 ${ThemeStyles.SpacingValue.smaller.pxToEm().value}",
    border :=! s"1px solid ${ThemeStyles.TextColor.base.value}",
    borderRadius(15.pxToEm()),
    backgroundColor.transparent,
    transition := "color .2s ease-in-out, background-color .2s ease-in-out",
    (&.hover)(color(ThemeStyles.TextColor.white), backgroundColor(ThemeStyles.TextColor.base))
  )

  val agree: StyleA = style(
    color(ThemeStyles.ThemeColor.positive),
    borderColor(ThemeStyles.ThemeColor.positive),
    (&.hover)(backgroundColor(ThemeStyles.ThemeColor.positive), color(ThemeStyles.TextColor.white))
  )

  val agreeActivated: StyleA =
    style(color(ThemeStyles.TextColor.white), backgroundColor(ThemeStyles.ThemeColor.positive))

  val disagree: StyleA = style(
    color(ThemeStyles.ThemeColor.negative),
    borderColor(ThemeStyles.ThemeColor.negative),
    (&.hover)(backgroundColor(ThemeStyles.ThemeColor.negative), color(ThemeStyles.TextColor.white))
  )

  val disagreeActivated: StyleA =
    style(color(ThemeStyles.TextColor.white), backgroundColor(ThemeStyles.ThemeColor.negative))

  val neutral: StyleA = style(
    color(ThemeStyles.TextColor.grey),
    borderColor(ThemeStyles.TextColor.grey),
    (&.hover)(backgroundColor(ThemeStyles.TextColor.grey), color(ThemeStyles.TextColor.white))
  )

  val neutralActivated: StyleA = style(color(ThemeStyles.TextColor.white), backgroundColor(ThemeStyles.TextColor.grey))

  val label: StyleA =
    style(lineHeight(1), whiteSpace.nowrap, unsafeChild(".fa")(display.inline, color(ThemeStyles.ThemeColor.assertive)))

  val votesCounter: StyleA =
    style(
      float.right,
      paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm()),
      opacity(1),
      transition := "opacity .2s ease-in-out",
      lineHeight(1)
    )

  val selectedQualificationVotesCounter: StyleA = style(opacity(0.5))
}
