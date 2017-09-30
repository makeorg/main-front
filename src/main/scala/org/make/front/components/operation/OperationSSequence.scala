package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.sequence.SequenceContainer.SequenceContainerProps
import org.make.front.components.submitProposal.SubmitProposal.SubmitProposalProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  GradientColor => GradientColorModel,
  Operation     => OperationModel,
  Sequence      => SequenceModel
}
import org.make.front.styles._
import org.make.front.styles.ui.CTAStyles

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object OperationSSequence {
  final case class OperationSSequenceProps(operation: OperationModel, sequence: SequenceModel)

  final case class OperationSSequenceState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[OperationSSequenceProps, OperationSSequenceState](
      displayName = "OperationSSequence",
      getInitialState = { _ =>
        OperationSSequenceState(isProposalModalOpened = false)
      },
      render = { self =>
        val gradient: GradientColorModel =
          self.props.wrapped.operation.gradient.getOrElse(GradientColorModel("#FFF", "#FFF"))

        val closeProposalModal: () => Unit = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
          event.preventDefault()
          self.setState(state => state.copy(isProposalModalOpened = true))
        }

        <.section(^.className := OperationSSequenceStyles.wrapper)(
          <.header(
            ^.className := Seq(
              OperationSSequenceStyles.headerWrapper,
              OperationSSequenceStyles.gradientBackground(gradient.from, gradient.to)
            )
          )(
            <.div(^.className := OperationSSequenceStyles.headerInnerWrapper)(
              <.div(^.className := LayoutRulesStyles.centeredRow)(
                <.div(^.className := LayoutRulesStyles.col)(
                  <.div(^.className := OperationSSequenceStyles.headerInnerSubWrapper)(
                    <.p(^.className := Seq(OperationSSequenceStyles.backLinkWrapper))(
                      <.Link(
                        ^.className := OperationSSequenceStyles.backLink,
                        ^.to := s"/operation/${self.props.wrapped.operation.slug}"
                      )(
                        <.i(
                          ^.className := Seq(
                            OperationSSequenceStyles.backLinkArrow,
                            FontAwesomeStyles.angleLeft,
                            FontAwesomeStyles.fa
                          )
                        )(),
                        <.span(
                          ^.className := Seq(TextStyles.smallText, TextStyles.title),
                          ^.dangerouslySetInnerHTML := "Accéder à<br>l'opération"
                        )()
                      )
                    ),
                    <.div(^.className := OperationSSequenceStyles.titleWrapper)(
                      <.h1(^.className := Seq(OperationSSequenceStyles.title, TextStyles.smallTitle))(
                        unescape(self.props.wrapped.sequence.title)
                      ),
                      <.p(
                        ^.className := Seq(
                          OperationSSequenceStyles.totalOfPropositions,
                          TextStyles.smallText,
                          TextStyles.boldText
                        )
                      )("10 propositions")
                    ),
                    <.div(^.className := OperationSSequenceStyles.openProposalModalButtonWrapper)(
                      <.button(
                        ^.className := Seq(
                          CTAStyles.basic,
                          CTAStyles.basicOnButton,
                          OperationSSequenceStyles.openProposalModalButton
                        ),
                        ^.onClick := openProposalModal
                      )(
                        <.i(^.className := Seq(FontAwesomeStyles.pencil, FontAwesomeStyles.fa))(),
                        unescape("&nbsp;" + I18n.t("content.search.propose"))
                      ),
                      <.FullscreenModalComponent(
                        ^.wrapped := FullscreenModalProps(self.state.isProposalModalOpened, closeProposalModal)
                      )(<.SubmitProposalComponent(^.wrapped := SubmitProposalProps(onProposalProposed = () => {
                        self.setState(_.copy(isProposalModalOpened = false))
                      }))())
                    )
                  )
                )
              )
            )
          ),
          <.div(^.className := OperationSSequenceStyles.contentWrapper)(
            <.div(^.className := OperationSSequenceStyles.contentInnerWrapper)(
              <.SequenceContainerComponent(^.wrapped := SequenceContainerProps(self.props.wrapped.sequence))()
            )
          ),
          <.style()(OperationSSequenceStyles.render[String])
        )
      }
    )
}
object OperationSSequenceStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(display.table, height(100.%%), width(100.%%))

  val contentWrapper: StyleA =
    style(display.tableRow, height(100.%%))

  val contentInnerWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val headerWrapper: StyleA =
    style(display.tableRow)

  val headerInnerWrapper: StyleA =
    style(
      display.tableCell,
      verticalAlign.middle,
      paddingTop(50.pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
      ThemeStyles.MediaQueries
        .beyondSmall(paddingTop(80.pxToEm()))
    )

  val headerInnerSubWrapper: StyleA =
    style(display.table, height(100.pxToEm()), width(100.%%))

  val backLinkWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle, width(150.pxToEm()), color(ThemeStyles.TextColor.white))

  val backLink: StyleA =
    style(position.relative, display.inlineBlock, paddingLeft(20.pxToEm()), color(ThemeStyles.TextColor.white))

  val backLinkArrow: StyleA =
    style(position.absolute, top(50.%%), left(`0`), transform := "translateY(-50%)", fontSize(28.pxToEm()))

  val titleWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle)

  val title: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.white))

  val totalOfPropositions: StyleA =
    style(textAlign.center, color(ThemeStyles.TextColor.white))

  val openProposalModalButtonWrapper: StyleA =
    style(display.tableCell, verticalAlign.middle, width(150.pxToEm()), textAlign.right)

  val openProposalModalButton: StyleA =
    style()

  def gradientBackground(from: String, to: String): StyleA =
    style(background := s"linear-gradient(130deg, $from, $to)")
}
