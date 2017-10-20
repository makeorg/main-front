package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.facades.{I18n}
import org.make.front.facades.Unescape.unescape
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.front.models.{Operation => OperationModel}

import scalacss.DevDefaults.{StyleA, _}
import scalacss.internal.mutable.StyleSheet

object PromptingToProposeInsideOperationSequence {

  final case class PromptingToProposeInsideOperationSequenceProps(operation: OperationModel,
                                                                  clickOnButtonHandler: () => Unit)

  final case class PromptingToProposeInsideOperationSequenceState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToProposeInsideOperationSequenceProps, PromptingToProposeInsideOperationSequenceState](
        displayName = "IntroOfOperationSequence",
        getInitialState = { _ =>
          PromptingToProposeInsideOperationSequenceState(isProposalModalOpened = false)
        },
        render = { self =>
          val closeProposalModal: () => Unit = () => {
            self.setState(state => state.copy(isProposalModalOpened = false))
          }

          val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
          }

          <.div(^.className := Seq(RowRulesStyles.row, PromptingToProposeInsideOperationSequenceStyles.wrapper))(
            <.div(^.className := Seq(ColRulesStyles.col, PromptingToProposeInsideOperationSequenceStyles.titleWrapper))(
              <.p(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(
                unescape(I18n.t("operation.sequence.prompting-to-propose.intro"))
              )
            ),
            <.div(^.className := Seq(PromptingToProposeInsideOperationSequenceStyles.ctaWrapper))(
              <.button(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton), ^.onClick := openProposalModal)(
                <.i(^.className := Seq(FontAwesomeStyles.paperPlaneTransparent))(),
                unescape("&nbsp;" + I18n.t("operation.sequence.prompting-to-propose.propose-cta"))
              )
            ),
            <.FullscreenModalComponent(
              ^.wrapped := FullscreenModalProps(
                isModalOpened = self.state.isProposalModalOpened,
                closeCallback = closeProposalModal
              )
            )(
              <.SubmitProposalInRelationToOperationComponent(
                ^.wrapped := SubmitProposalInRelationToOperationProps(
                  operation = self.props.wrapped.operation,
                  onProposalProposed = () => {
                    self.setState(_.copy(isProposalModalOpened = false))
                  }
                )
              )()
            ),
            <.div(^.className := PromptingToProposeInsideOperationSequenceStyles.ctaWrapper)(
              <.button(
                ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton, CTAStyles.moreDiscreet),
                ^.onClick := self.props.wrapped.clickOnButtonHandler
              )(
                <.i(^.className := Seq(FontAwesomeStyles.stepForward))(),
                unescape("&nbsp;" + I18n.t("operation.sequence.prompting-to-propose.next-cta"))
              )
            ),
            <.style()(PromptingToProposeInsideOperationSequenceStyles.render[String])
          )
        }
      )

}

object PromptingToProposeInsideOperationSequenceStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(textAlign.center)

  val titleWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val ctaWrapper: StyleA =
    style(
      display.inlineBlock,
      margin := s"${ThemeStyles.SpacingValue.small.pxToEm().value} ${ThemeStyles.SpacingValue.small.pxToEm().value} 0"
    )

}
