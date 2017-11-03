package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.components.proposal.vote.QualificateVoteButton.QualificateVoteButtonProps
import org.make.front.models.Qualification
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.TooltipStyles
import org.make.front.styles.utils._

import scala.concurrent.Future
import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object QualificateVote {

  final case class QualificateVoteProps(updateState: Boolean,
                                        voteKey: String,
                                        qualifications: Seq[Qualification],
                                        qualify: (String)             => Future[Qualification],
                                        removeQualification: (String) => Future[Qualification],
                                        guide: Option[String] = None)

  final case class QualificateVoteState(qualifications: Map[String, Qualification])

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
          <.div(^.className := QualificateVoteStyles.wrapper)(<.ul()(self.props.wrapped.qualifications.map {
            qualification =>
              <.li(^.className := QualificateVoteStyles.buttonItem)(
                <.QualificateVoteButtonComponent(
                  ^.wrapped := QualificateVoteButtonProps(
                    updateState = self.props.wrapped.updateState,
                    voteKey = self.props.wrapped.voteKey,
                    qualification = qualification,
                    qualifyVote = self.props.wrapped.qualify,
                    removeVoteQualification = self.props.wrapped.removeQualification
                  )
                )(),
                <.style()(QualificateVoteStyles.render[String])
              )
          }), if (self.props.wrapped.guide.getOrElse("") != "") {
            <.p(^.className := QualificateVoteStyles.guide)(
              <.span(
                ^.className := TextStyles.smallerText,
                ^.dangerouslySetInnerHTML := self.props.wrapped.guide.getOrElse("")
              )()
            )
          })
        }
      )
}

object QualificateVoteStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(position.relative)

  val buttonItem: StyleA =
    style(marginTop((ThemeStyles.SpacingValue.smaller / 2).pxToEm()), &.firstChild(marginTop(`0`)))

  val guide: StyleA =
    style(
      TooltipStyles.topPositioned,
      ThemeStyles.MediaQueries.beyondMedium(
        top(50.%%),
        transform := "translateY(-50%)",
        left(100.%%),
        marginBottom(`0`),
        marginLeft(ThemeStyles.SpacingValue.smaller.pxToEm()),
        bottom.inherit,
        right.auto,
        (&.after)(
          top(50.%%),
          transform := "translateY(-50%)",
          right(100.%%),
          bottom.auto,
          borderTop :=! s"${5.pxToEm().value} solid transparent",
          borderBottom :=! s"${5.pxToEm().value} solid transparent",
          borderRight :=! s"${5.pxToEm().value} solid ${ThemeStyles.BackgroundColor.darkGrey.value}"
        )
      )
    )
}
