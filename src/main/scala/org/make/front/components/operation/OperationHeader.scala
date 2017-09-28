package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.facades._
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{GradientColor => GradientColorModel, Operation => OperationModel}
import org.make.front.styles.{InputStyles, LayoutRulesStyles, TextStyles, ThemeStyles}
import org.scalajs.dom.raw.HTMLElement

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object OperationHeader {

  case class OperationHeaderProps(operation: OperationModel)

  case class OperationHeaderState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[OperationHeaderProps, OperationHeaderState](
      displayName = getClass.toString,
      getInitialState = { _ =>
        OperationHeaderState(isProposalModalOpened = false)
      },
      render = (self) => {

        var proposalInput: Option[HTMLElement] = None

        def toggleProposalModal() = () => {
          self.setState(state => state.copy(isProposalModalOpened = !self.state.isProposalModalOpened))

        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          proposalInput.foreach(_.blur())
        }

        val operation: OperationModel =
          self.props.wrapped.operation
        val gradient: GradientColorModel = operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        <.header()(
          <.h1(^.className := Seq(TextStyles.veryBigTitle, OperationHeaderStyles.title))(unescape(operation.title)),
          <.p()("Partagez vos propositions"),
          <.p(
            ^.className := Seq(
              InputStyles.wrapper,
              InputStyles.withIcon,
              InputStyles.biggerWithIcon,
              OperationHeaderStyles.proposalInputWithIconWrapper
            )
          )(
            <.span(^.className := OperationHeaderStyles.inputInnerWrapper)(
              <.span(^.className := OperationHeaderStyles.inputSubInnerWrapper)(
                <.input(
                  ^.`type`.text,
                  ^.value := "Il faut ",
                  ^.ref := ((input: HTMLElement) => proposalInput = Some(input)),
                  ^.onFocus := openProposalModalFromInput()
                )()
              ),
              <.span(^.className := OperationHeaderStyles.textLimitInfoWapper)(
                <.span(^.className := Seq(TextStyles.smallText, OperationHeaderStyles.textLimitInfo))("8/140")
              )
            )
          ),
          <.FullscreenModalComponent(
            ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, toggleProposalModal())
          )(
            <.SubmitProposalInRelationToOperationComponent(
              ^.wrapped := SubmitProposalInRelationToOperationProps(operation = operation, onProposalProposed = () => {
                self.setState(_.copy(isProposalModalOpened = false))
              })
            )()
          ),
          <.style()(OperationHeaderStyles.render[String])
        )
      }
    )

}

object OperationHeaderStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  def gradientBackground(from: String, to: String): StyleA =
    style(background := s"linear-gradient(130deg, $from, $to)")

  val title: StyleA =
    style(
      display.inlineBlock,
      marginBottom(15.pxToEm(30)),
      lineHeight(41.pxToEm(30)),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(10.pxToEm(60)), lineHeight(83.pxToEm(60))),
      color(ThemeStyles.TextColor.white),
      textShadow := s"1px 1px 1px rgb(0, 0, 0)"
    )

  val proposalInputWithIconWrapper: StyleA =
    style(
      boxShadow := "0 2px 5px 0 rgba(0,0,0,0.50)",
      (&.before)(content := "'\\F0EB'"),
      unsafeChild("input")(ThemeStyles.Font.circularStdBold)
    )

  val inputInnerWrapper: StyleA = style(display.table, width(100.%%))

  val inputSubInnerWrapper: StyleA =
    style(display.tableCell, width(100.%%))

  val textLimitInfoWapper: StyleA = style(display.tableCell, verticalAlign.middle)

  val textLimitInfo: StyleA =
    style(padding(1.em), lineHeight.initial, color(ThemeStyles.TextColor.lighter), whiteSpace.nowrap)

}
