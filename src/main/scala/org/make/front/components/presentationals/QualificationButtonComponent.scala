package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.facades.I18n
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.helpers.NumberFormat.formatToKilo
import org.make.front.models.{VoteStats, VoteType}
import org.make.front.styles.MakeStyles

import scalacss.DevDefaults._
import scalacss.internal.ValueT

//todo get qualification status through props
object QualificationButtonComponent {

  def onClickQualif(self: Self[QualificationButtonProps, QualificationButtonState], qualifPosition: String)(): Unit = {
    self.setState(
      _.copy(
        isToggledTop = if (qualifPosition == "top") !self.state.isToggledTop else self.state.isToggledTop,
        isToggledMiddle = if (qualifPosition == "middle") !self.state.isToggledMiddle else self.state.isToggledMiddle,
        isToggledBottom = if (qualifPosition == "bottom") !self.state.isToggledBottom else self.state.isToggledBottom
      )
    )
  }

  final case class QualificationButtonProps(color: ValueT[ValueT.Color],
                                            qualificationType: VoteType,
                                            qualifStats: VoteStats)

  final case class QualificationButtonState(isToggledTop: Boolean, isToggledMiddle: Boolean, isToggledBottom: Boolean)

  lazy val reactClass: ReactClass = React.createClass[QualificationButtonProps, QualificationButtonState](
    getInitialState =
      (_) => QualificationButtonState(isToggledTop = false, isToggledMiddle = false, isToggledBottom = false),
    render = (self) => {
      val keyQualifType = s"content.proposal.${self.props.wrapped.qualificationType.translationKey}"
      val qualifStats = self.props.wrapped.qualifStats
      <.div()(
        renderQualification(self, s"$keyQualifType.top", qualifStats.nbQualifTop, self.state.isToggledTop, "top"),
        renderQualification(
          self,
          s"$keyQualifType.middle",
          qualifStats.nbQualifMiddle,
          self.state.isToggledMiddle,
          "middle"
        ),
        renderQualification(
          self,
          s"$keyQualifType.bottom",
          qualifStats.nbQualifBottom,
          self.state.isToggledBottom,
          "bottom"
        ),
        <.style()(QualificationButtonStyle.render[String])
      )
    }
  )

  def renderQualification(self: Self[QualificationButtonProps, QualificationButtonState],
                          text: String,
                          totalQualif: Int,
                          isToggled: Boolean,
                          qualifPosition: String): ReactElement = {
    val (divStyle, spanText, spanStyle) =
      if (!isToggled)
        (
          QualificationButtonStyle.qualificationButton(self.props.wrapped.color),
          I18n.t("content.proposal.plusOne"),
          QualificationButtonStyle.plusOne
        )
      else
        (
          QualificationButtonStyle.qualificationButtonSelected(self.props.wrapped.color),
          formatToKilo(totalQualif),
          QualificationButtonStyle.statQualif
        )

    <.div(^.className := divStyle, ^.onClick := onClickQualif(self, qualifPosition) _)(
      <.div(^.className := QualificationButtonStyle.textQualif)(
        <.Translate(^.value := text, ^.dangerousHtml := true, ^.className := QualificationButtonStyle.qualifWrapper)()
      ),
      <.div(^.className := spanStyle)(<.span()(spanText))
    )
  }

}

object QualificationButtonStyle extends StyleSheet.Inline {
  import dsl._

  def qualificationButton(buttonColor: ValueT[ValueT.Color]): StyleA = style(
    display.flex,
    position.relative,
    width(100.%%),
    border :=! s"0.2rem solid ${buttonColor.value}",
    color :=! buttonColor,
    borderRadius(3.rem),
    padding :=! "0 1rem",
    marginBottom(0.5.rem),
    justifyContent.spaceBetween,
    alignItems.center,
    backgroundColor :=! MakeStyles.Color.white,
    cursor.pointer,
    (&.hover)(color :=! MakeStyles.Color.white, backgroundColor :=! buttonColor)
  )

  def qualificationButtonSelected(buttonColor: ValueT[ValueT.Color]): StyleA = style(
    display.flex,
    position.relative,
    width(100.%%),
    border :=! s"0.2rem solid ${buttonColor.value}",
    color :=! MakeStyles.Color.white,
    borderRadius(3.rem),
    padding :=! "0 1rem",
    marginBottom(0.5.rem),
    justifyContent.spaceBetween,
    alignItems.center,
    backgroundColor :=! buttonColor,
    cursor.pointer
  )

  val textQualif: StyleA = style(display.block, fontSize(1.3.rem), MakeStyles.Font.circularStdBook)

  val plusOne: StyleA = style(fontSize(1.8.rem), fontWeight :=! "900")

  val statQualif: StyleA = style(fontSize(1.8.rem), color :=! "hsla(0, 0%, 100%, .5)")

  val qualifWrapper: StyleA = style(unsafeChild(".spanHeart")(color(MakeStyles.Color.pink), marginLeft(0.2.rem)))
}
