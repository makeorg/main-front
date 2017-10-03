package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.helpers.NumberFormat.formatToKilo
import org.make.front.models.{Qualification => QualificationModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.QualificationResponse

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.DevDefaults._
import scalacss.internal.mutable.StyleSheet

import scala.concurrent.ExecutionContext.Implicits.global

object QualificateVoteButton {

  case class QualificateVoteButtonProps(voteKey: String,
                                        qualification: QualificationModel,
                                        qualifyVote: (String)             => Future[QualificationResponse],
                                        removeVoteQualification: (String) => Future[QualificationResponse])

  case class QualificateVoteButtonState(isSelected: Boolean, count: Int)

  lazy val reactClass: ReactClass = React.createClass[QualificateVoteButtonProps, QualificateVoteButtonState](
    displayName = "QualificateVoteButton",
    getInitialState = { self =>
      QualificateVoteButtonState(
        isSelected = self.props.wrapped.qualification.selected.getOrElse(false),
        self.props.wrapped.qualification.count
      )
    },
    componentWillReceiveProps = { (self, props) =>
      self.setState(
        QualificateVoteButtonState(
          isSelected = props.wrapped.qualification.selected.getOrElse(false),
          count = props.wrapped.qualification.count
        )
      )
    },
    render = { (self) =>
      def qualifyVote(key: String): (SyntheticEvent) => Unit = {
        e =>
          e.preventDefault()

          if (self.state.isSelected) {
            self.props.wrapped.removeVoteQualification(key).onComplete {
              case Failure(_)      =>
              case Success(result) => self.setState(_.copy(isSelected = result.hasQualified, count = result.count))
            }
          } else {
            self.props.wrapped.qualifyVote(key).onComplete {
              case Failure(_)      =>
              case Success(result) => self.setState(_.copy(isSelected = result.hasQualified, count = result.count))
            }
          }
      }

      val buttonClasses =
        Seq(QualificateVoteButtonStyles.button.htmlClass, self.props.wrapped.voteKey match {
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

      <.button(^.className := buttonClasses, ^.onClick := qualifyVote(self.props.wrapped.qualification.key))(
        <.span(^.className := QualificateVoteButtonStyles.innerWrapper)(
          <.span(
            ^.className := Seq(TextStyles.smallerText, TextStyles.boldText, QualificateVoteButtonStyles.label),
            ^.dangerouslySetInnerHTML := I18n.t(s"content.proposal.${self.props.wrapped.qualification.key}")
          )(),
          <.span(
            ^.className := Seq(QualificateVoteButtonStyles.votesCounter, TextStyles.mediumText, TextStyles.boldText)
          )(if (!self.state.isSelected) {
            I18n.t("content.proposal.plusOne")
          } else {
            <.span(^.className := QualificateVoteButtonStyles.selectedQualificationVotesCounter)(
              formatToKilo(self.state.count)
            )
          })
        ),
        <.style()(QualificateVoteButtonStyles.render[String])
      )
    }
  )

}

object QualificateVoteButtonStyles extends StyleSheet.Inline {

  import dsl._

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
    whiteSpace.nowrap,
    overflow.hidden,
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

  val innerWrapper: StyleA =
    style(display.table, width(100.%%), height(100.%%))

  val label: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      lineHeight(1),
      unsafeChild(".fa")(display.inline, color(ThemeStyles.ThemeColor.assertive))
    )

  val votesCounter: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      paddingLeft(0.5.em),
      textAlign.right,
      lineHeight(1),
      opacity(1),
      transition := "opacity .2s ease-in-out"
    )

  val selectedQualificationVotesCounter: StyleA = style(opacity(0.5))
}
