package org.make.front.components.Vote

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.presentationals._
import org.make.front.facades.I18n
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.helpers.NumberFormat.formatToKilo
import org.make.front.models.{Qualification, Vote}

import scalacss.DevDefaults._

//todo get qualification status through props
object QualificateVoteComponent {

  final case class QualificateVoteProps(voteType: VoteType, qualifStats: Vote)

  final case class QualificateVoteState(qualifications: Seq[Qualification])

  def onClickQualif(self: Self[QualificateVoteProps, QualificateVoteState], qualification: Qualification)(): Unit = {
    val index = self.state.qualifications.indexOf(self.state.qualifications.find(_ == qualification).get)
    self.setState(
      _.copy(
        qualifications = self.state.qualifications.updated(
          index,
          Qualification(key = qualification.key, count = qualification.count, selected = !qualification.selected)
        )
      )
    )
  }

  lazy val reactClass: ReactClass = React.createClass[QualificateVoteProps, QualificateVoteState](
    getInitialState = (self) => QualificateVoteState(qualifications = self.props.wrapped.qualifStats.qualifications),
    render = (self) => {
      <.ul()(self.state.qualifications.map { qualification =>
        <.li()(renderQualification(self, qualification, s"content.proposal.${qualification.key}"))
      }, <.style()(QualificateVoteStyles.render[String]))
    }
  )

  def renderQualification(self: Self[QualificateVoteProps, QualificateVoteState],
                          qualification: Qualification,
                          text: String): ReactElement = {

    val QualificationStyle = self.props.wrapped.voteType match {
      case VoteAgree    => QualificateVoteStyles.agree
      case VoteDisagree => QualificateVoteStyles.disagree
      case VoteNeutral  => QualificateVoteStyles.neutral
    }

    val QualificationSelectedStyle = self.props.wrapped.voteType match {
      case VoteAgree    => QualificateVoteStyles.agreeSelected
      case VoteDisagree => QualificateVoteStyles.disagreeSelected
      case VoteNeutral  => QualificateVoteStyles.neutralSelected
    }

    val (divStyle, spanText) =
      if (!qualification.selected)
        (QualificationStyle, I18n.t("content.proposal.plusOne"))
      else
        (QualificationSelectedStyle, formatToKilo(qualification.count))

    <.button(^.className := divStyle, ^.onClick := onClickQualif(self, qualification) _)(
      <.Translate(^.value := text, ^.dangerousHtml := true)(),
      spanText
    )
  }
}

object QualificateVoteStyles extends StyleSheet.Inline {

  val agree: StyleA = style()

  val disagree: StyleA = style()

  val neutral: StyleA = style()

  val agreeSelected: StyleA = style()

  val disagreeSelected: StyleA = style()

  val neutralSelected: StyleA = style()

}
