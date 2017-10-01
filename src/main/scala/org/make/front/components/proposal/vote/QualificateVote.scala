package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.QualificateVoteButton.QualificateVoteButtonProps
import org.make.front.models.{Qualification => QualificationModel, Vote => VoteModel}
import org.make.front.styles._
import org.make.front.styles.utils._

import scala.scalajs.js.Dynamic.{global => g}
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object QualificateVote {

  final case class QualificateVoteProps(vote: VoteModel)

  final case class QualificateVoteState(qualifications: Seq[QualificationModel])

  lazy val reactClass: ReactClass =
    React
      .createClass[QualificateVoteProps, QualificateVoteState](
        displayName = "QualificateVote",
        getInitialState = (self) => QualificateVoteState(qualifications = self.props.wrapped.vote.qualifications),
        render = { (self) =>
          def saveVoteQualification(qualification: QualificationModel): Unit = {
            g.console.log(qualification.toString)
          }

          <.ul()(self.props.wrapped.vote.qualifications.map { qualification =>
            <.li(^.className := QualificateVoteStyles.buttonItem)(
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

  val buttonItem: StyleA =
    style(marginTop({ ThemeStyles.SpacingValue.smaller / 2 }.pxToEm()), &.firstChild(marginTop(`0`)))

}
