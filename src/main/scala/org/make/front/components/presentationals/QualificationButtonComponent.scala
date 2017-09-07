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
import org.make.front.styles.MakeStyles

import scalacss.DevDefaults._

//todo get qualification status through props
object QualificationButtonComponent {

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

  final case class QualificationButtonProps(voteType: VoteType, qualifStats: Vote)

  final case class QualificationButtonState(qualifications: Seq[Qualification])

  lazy val reactClass: ReactClass = React.createClass[QualificationButtonProps, QualificationButtonState](
    getInitialState = (self) => QualificationButtonState(qualifications = self.props.wrapped.qualifStats.qualifications),
    render = (self) => {
      <.div()(self.state.qualifications.map { qualification =>
        renderQualification(self, qualification, s"content.proposal.${qualification.key}")
      }, <.style()(QualificationButtonStyle.render[String]))
    }
  )

  def renderQualification(self: Self[QualificationButtonProps, QualificationButtonState],
                          qualification: Qualification,
                          text: String): ReactElement = {

    val QualificationStyle = self.props.wrapped.voteType match {
      case VoteAgree =>
        Seq(QualificationButtonStyle.qualificationButton, QualificationButtonStyle.qualificationButtonAgree)
      case VoteDisagree =>
        Seq(QualificationButtonStyle.qualificationButton, QualificationButtonStyle.qualificationButtonDisagree)
      case VoteNeutral =>
        Seq(QualificationButtonStyle.qualificationButton, QualificationButtonStyle.qualificationButtonNeutral)
    }

    val QualificationSelectedStyle = self.props.wrapped.voteType match {
      case VoteAgree =>
        Seq(QualificationButtonStyle.qualificationButton, QualificationButtonStyle.qualificationButtonSelectedAgree)
      case VoteDisagree =>
        Seq(QualificationButtonStyle.qualificationButton, QualificationButtonStyle.qualificationButtonSelectedDisagree)
      case VoteNeutral =>
        Seq(QualificationButtonStyle.qualificationButton, QualificationButtonStyle.qualificationButtonSelectedNeutral)
    }

    val (divStyle, spanText, spanStyle) =
      if (!qualification.selected)
        (QualificationStyle, I18n.t("content.proposal.plusOne"), QualificationButtonStyle.plusOne)
      else
        (QualificationSelectedStyle, formatToKilo(qualification.count), QualificationButtonStyle.statQualif)

    <.div(^.className := divStyle, ^.onClick := onClickQualif(self, qualification) _)(
      <.div(^.className := QualificationButtonStyle.textQualif)(
        <.Translate(^.value := text, ^.dangerousHtml := true, ^.className := QualificationButtonStyle.qualifWrapper)()
      ),
      <.div(^.className := spanStyle)(<.span()(spanText))
    )
  }

}

object QualificationButtonStyle extends StyleSheet.Inline {
  import dsl._

  val qualificationButtonAgree: StyleA = style(
    border :=! s"0.2rem solid ${MakeStyles.Color.green.value}",
    color :=! MakeStyles.Color.green,
    (&.hover)(color :=! MakeStyles.Color.white, backgroundColor :=! MakeStyles.Color.green)
  )

  val qualificationButtonDisagree: StyleA = style(
    border :=! s"0.2rem solid ${MakeStyles.Color.red.value}",
    color :=! MakeStyles.Color.red,
    (&.hover)(color :=! MakeStyles.Color.white, backgroundColor :=! MakeStyles.Color.red)
  )

  val qualificationButtonNeutral: StyleA = style(
    border :=! s"0.2rem solid ${MakeStyles.Color.greyVote.value}",
    color :=! MakeStyles.Color.greyVote,
    (&.hover)(color :=! MakeStyles.Color.white, backgroundColor :=! MakeStyles.Color.greyVote)
  )

  val qualificationButton: StyleA = style(
    display.flex,
    position.relative,
    width(100.%%),
    borderRadius(3.rem),
    padding :=! "0 1rem",
    marginBottom(0.5.rem),
    justifyContent.spaceBetween,
    alignItems.center,
    backgroundColor :=! MakeStyles.Color.white,
    cursor.pointer
  )

  val qualificationButtonSelectedAgree: StyleA =
    style(border :=! s"0.2rem solid ${MakeStyles.Color.green.value}", backgroundColor :=! MakeStyles.Color.green)

  val qualificationButtonSelectedDisagree: StyleA =
    style(border :=! s"0.2rem solid ${MakeStyles.Color.red.value}", backgroundColor :=! MakeStyles.Color.red)

  val qualificationButtonSelectedNeutral: StyleA =
    style(border :=! s"0.2rem solid ${MakeStyles.Color.greyVote.value}", backgroundColor :=! MakeStyles.Color.greyVote)

  val qualificationButtonSelected: StyleA = style(
    display.flex,
    position.relative,
    width(100.%%),
    color :=! MakeStyles.Color.white,
    borderRadius(3.rem),
    padding :=! "0 1rem",
    marginBottom(0.5.rem),
    justifyContent.spaceBetween,
    alignItems.center,
    cursor.pointer
  )

  val textQualif: StyleA = style(display.block, fontSize(1.3.rem), MakeStyles.Font.circularStdBook)

  val plusOne: StyleA = style(fontSize(1.8.rem), fontWeight :=! "900")

  val statQualif: StyleA = style(fontSize(1.8.rem), color :=! "hsla(0, 0%, 100%, .5)")

  val qualifWrapper: StyleA = style(unsafeChild(".spanHeart")(color(MakeStyles.Color.pink), marginLeft(0.2.rem)))
}
