package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.QualificateVoteButton.QualificateVoteButtonProps
import org.make.front.models.{Qualification => QualificationModel}
import org.make.front.styles._
import org.make.front.styles.utils._
import org.make.services.proposal.ProposalResponses.QualificationResponse

import scala.concurrent.Future
import scala.scalajs.js.Dynamic.{global => g}
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object QualificateVote {

  final case class QualificateVoteProps(voteKey: String,
                                        qualifications: Seq[QualificationModel],
                                        qualify: (String)             => Future[QualificationResponse],
                                        removeQualification: (String) => Future[QualificationResponse])

  final case class QualificateVoteState(qualifications: Map[String, QualificationModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[QualificateVoteProps, QualificateVoteState](
        displayName = "QualificateVote",
        getInitialState = { self =>
          QualificateVoteState(qualifications = self.props.wrapped.qualifications.map { qualification =>
            qualification.key -> qualification
          }.toMap)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(QualificateVoteState(qualifications = props.wrapped.qualifications.map { qualification =>
            qualification.key -> qualification
          }.toMap))
        },
        render = { self =>
          def saveVoteQualification(qualification: QualificationModel): Unit = {
            g.console.log(qualification.toString)
          }

          <.ul()(self.props.wrapped.qualifications.map { qualification =>
            <.li(^.className := QualificateVoteStyles.buttonItem)(
              <.QualificateVoteButtonComponent(
                ^.wrapped := QualificateVoteButtonProps(
                  voteKey = self.props.wrapped.voteKey,
                  qualification = qualification,
                  qualifyVote = self.props.wrapped.qualify,
                  removeVoteQualification = self.props.wrapped.removeQualification
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

  val buttonItem: StyleA =
    style(marginTop({ ThemeStyles.SpacingValue.smaller / 2 }.pxToEm()), &.firstChild(marginTop(`0`)))

}
