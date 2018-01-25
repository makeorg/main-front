package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{SequenceId, Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object PromptingToProposeInRelationToOperation {

  final case class PromptingToProposeInRelationToOperationProps(operation: OperationModel,
                                                                clickOnButtonHandler: () => Unit,
                                                                proposeHandler: ()       => Unit,
                                                                sequenceId: SequenceId,
                                                                maybeLocation: Option[LocationModel])

  final case class PromptingToProposeInRelationToOperationState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToProposeInRelationToOperationProps, PromptingToProposeInRelationToOperationState](
        displayName = "PromptingToProposeInRelationToOperation",
        getInitialState = { _ =>
          PromptingToProposeInRelationToOperationState(isProposalModalOpened = false)
        },
        render = { self =>
          val closeProposalModal: () => Unit = () => {
            self.setState(state => state.copy(isProposalModalOpened = false))
          }

          val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
            TrackingService.track(
              "click-proposal-submit-form-open",
              TrackingContext(TrackingLocation.sequenceProposalPushCard, Some(self.props.wrapped.operation.slug)),
              Map("sequenceId" -> self.props.wrapped.sequenceId.value)
            )
          }

          val onNextProposal: () => Unit = { () =>
            self.props.wrapped.clickOnButtonHandler()
            TrackingService.track(
              "click-proposal-push-card-ignore",
              TrackingContext(TrackingLocation.sequencePage, Some(self.props.wrapped.operation.slug)),
              Map("sequenceId" -> self.props.wrapped.sequenceId.value)
            )
          }

          <.div(^.className := Seq(LayoutRulesStyles.row, PromptingToProposeInRelationToOperationStyles.wrapper))(
            <.div(^.className := Seq(PromptingToProposeInRelationToOperationStyles.titleWrapper))(
              <.p(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(
                unescape(I18n.t("sequence.prompting-to-propose.intro"))
              )
            ),
            <.div(^.className := Seq(PromptingToProposeInRelationToOperationStyles.ctaWrapper))(
              <.button(^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton), ^.onClick := openProposalModal)(
                <.i(^.className := Seq(FontAwesomeStyles.pencil))(),
                unescape("&nbsp;" + I18n.t("sequence.prompting-to-propose.propose-cta"))
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
                      closeProposalModal()
                      self.props.wrapped.proposeHandler()
                    },
                    maybeSequence = Some(self.props.wrapped.sequenceId),
                    maybeLocation = self.props.wrapped.maybeLocation
                  )
                )()
              )
            ),
            <.div(^.className := PromptingToProposeInRelationToOperationStyles.ctaWrapper)(
              <.button(
                ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton, CTAStyles.moreDiscreet),
                ^.onClick := onNextProposal
              )(
                <.i(^.className := Seq(FontAwesomeStyles.stepForward))(),
                unescape("&nbsp;" + I18n.t("sequence.prompting-to-propose.next-cta"))
              )
            ),
            <.style()(PromptingToProposeInRelationToOperationStyles.render[String])
          )
        }
      )

}

object PromptingToProposeInRelationToOperationStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(textAlign.center)

  val titleWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.small.pxToEm()))

  val ctaWrapper: StyleA =
    style(
      display.inlineBlock,
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      marginLeft(ThemeStyles.SpacingValue.small.pxToEm()),
      &.firstChild(marginLeft(`0`), marginRight(ThemeStyles.SpacingValue.small.pxToEm()))
    )
}
