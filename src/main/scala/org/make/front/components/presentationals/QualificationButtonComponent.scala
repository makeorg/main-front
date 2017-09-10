package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.facades.I18n
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.helpers.NumberFormat.formatToKilo
import org.make.front.models.{Qualification, Vote}

import scalacss.DevDefaults._

//todo get qualification status through props
object QualificationButtonComponent {

  final case class QualificationButtonProps(voteType: VoteType, qualifStats: Vote)

  final case class QualificationButtonState(qualifications: Seq[Qualification])

  def onClickQualif(self: Self[QualificationButtonProps, QualificationButtonState],
                    qualification: Qualification)(): Unit = {
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

  lazy val reactClass: ReactClass = React.createClass[QualificationButtonProps, QualificationButtonState](
    getInitialState = (self) => QualificationButtonState(qualifications = self.props.wrapped.qualifStats.qualifications),
    render = (self) => {
      <.ul()(self.state.qualifications.map { qualification =>
        <.li()(renderQualification(self, qualification, s"content.proposal.${qualification.key}"))
      }, <.style()(QualificationButtonStyles.render[String]))
    }
  )

  def renderQualification(self: Self[QualificationButtonProps, QualificationButtonState],
                          qualification: Qualification,
                          text: String): ReactElement = {

    val QualificationStyle = self.props.wrapped.voteType match {
      case VoteAgree    => QualificationButtonStyles.agree
      case VoteDisagree => QualificationButtonStyles.disagree
      case VoteNeutral  => QualificationButtonStyles.neutral
    }

    val QualificationSelectedStyle = self.props.wrapped.voteType match {
      case VoteAgree    => QualificationButtonStyles.agreeSelected
      case VoteDisagree => QualificationButtonStyles.disagreeSelected
      case VoteNeutral  => QualificationButtonStyles.neutralSelected
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

object QualificationButtonStyles extends StyleSheet.Inline {

  import dsl._

  val agree: StyleA = style()

  val disagree: StyleA = style()

  val neutral: StyleA = style()

  val agreeSelected: StyleA = style()

  val disagreeSelected: StyleA = style()

  val neutralSelected: StyleA = style()

}
