package org.make.front.components.proposals.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposals.vote.QualificateVoteButton.QualificateVoteButtonProps
import org.make.front.models.{Qualification => QualificationModel, Vote => VoteModel}
import org.make.front.styles.ThemeStyles

import scala.scalajs.js.Dynamic.{global => g}
import scalacss.DevDefaults._
import scalacss.internal.{Length, StyleA}
import scalacss.internal.mutable.StyleSheet

object QualificateVote {

  final case class QualificateVoteProps(vote: VoteModel)

  final case class QualificateVoteState(qualifications: Seq[QualificationModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[QualificateVoteProps, QualificateVoteState](
        getInitialState = (self) => QualificateVoteState(qualifications = self.props.wrapped.vote.qualifications),
        render = { (self) =>
          def saveVoteQualification(qualification: QualificationModel): Unit = {
            g.console.log(qualification.toString)
          }

          <.ul()(self.props.wrapped.vote.qualifications.map { qualification =>
            <.li(^.className := QualificateVoteStyles.qualificateVoteButtonItem)(
              <.QualificateVoteButtonComponent(
                ^.wrapped := QualificateVoteButtonProps(
                  vote = self.props.wrapped.vote,
                  qualification = qualification,
                  handleQualificateVote = saveVoteQualification
                )
              )(),
              <.style()(QualificateVoteStyles.render[String])
            )
          })
        }
      )
}

object QualificateVoteStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val qualificateVoteButtonItem: StyleA =
    style(marginTop({ ThemeStyles.SpacingValue.smaller / 2 }.pxToEm()), &.firstChild(marginTop(0.%%)))

}
