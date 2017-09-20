package org.make.front.components.proposals.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.presentationals._
import org.make.front.components.proposals.vote.QualificateVoteButton.QualificateVoteButtonProps
import org.make.front.models.{Qualification => QualificationModel, Vote => VoteModel}

import scala.scalajs.js.Dynamic.{global => g}

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
            <.li()(
              <.QualificateVoteButtonComponent(
                ^.wrapped := QualificateVoteButtonProps(
                  vote = self.props.wrapped.vote,
                  qualification = qualification,
                  handleQualificateVote = saveVoteQualification
                )
              )()
            )
          })
        }
      )
}
