package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Operation => OperationModel, Location => LocationModel, Sequence => SequenceModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

object PromptingToProposeInRelationToOperation {

  final case class PromptingToProposeProps(operation: OperationModel,
                                           clickOnButtonHandler: () => Unit,
                                           proposeHandler: ()       => Unit,
                                           maybeSequence: Option[SequenceModel],
                                           maybeLocation: Option[LocationModel])

  final case class PromptingToProposeState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[PromptingToProposeProps, PromptingToProposeState](
        displayName = "PromptingToPropose",
        getInitialState = { _ =>
          PromptingToProposeState(isProposalModalOpened = false)
        },
        render = { self =>
          val closeProposalModal: () => Unit = () => {
            self.setState(state => state.copy(isProposalModalOpened = false))
          }

          val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
          }

          val onNextProposal: () => Unit = { () =>
            self.props.wrapped.clickOnButtonHandler()
            FacebookPixel.fbq("trackCustom", "click-proposal-push-card-ignore")
          }

          <.div(^.className := Seq(LayoutRulesStyles.row, PromptingToProposeStyles.wrapper))(
            <.div(^.className := Seq(PromptingToProposeStyles.titleWrapper))(
              <.p(^.className := Seq(TextStyles.bigText, TextStyles.boldText))(
                unescape(I18n.t("sequence.prompting-to-propose.intro"))
              )
            ),
            <.div(^.className := Seq(PromptingToProposeStyles.ctaWrapper))(
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
                    maybeSequence = self.props.wrapped.maybeSequence,
                    maybeLocation = self.props.wrapped.maybeLocation
                  )
                )()
              )
            ),
            <.div(^.className := PromptingToProposeStyles.ctaWrapper)(
              <.button(
                ^.className := Seq(CTAStyles.basic, CTAStyles.basicOnButton, CTAStyles.moreDiscreet),
                ^.onClick := onNextProposal
              )(
                <.i(^.className := Seq(FontAwesomeStyles.stepForward))(),
                unescape("&nbsp;" + I18n.t("sequence.prompting-to-propose.next-cta"))
              )
            ),
            <.style()(PromptingToProposeStyles.render[String])
          )
        }
      )

}

object PromptingToProposeStyles extends StyleSheet.Inline {

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
