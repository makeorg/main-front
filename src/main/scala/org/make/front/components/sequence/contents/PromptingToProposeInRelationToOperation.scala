/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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

import scala.scalajs.js

object PromptingToProposeInRelationToOperation {

  final case class PromptingToProposeInRelationToOperationProps(operation: OperationModel,
                                                                clickOnButtonHandler: ()   => Unit,
                                                                proposeHandler: ()         => Unit,
                                                                handleCanUpdate: (Boolean) => Unit,
                                                                sequenceId: SequenceId,
                                                                maybeLocation: Option[LocationModel],
                                                                language: String)

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
            self.props.wrapped.handleCanUpdate(true)
          }

          val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isProposalModalOpened = true))
            self.props.wrapped.handleCanUpdate(false)
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

          <.div(^.className := js.Array(LayoutRulesStyles.row, PromptingToProposeInRelationToOperationStyles.wrapper))(
            <.div(^.className := js.Array(PromptingToProposeInRelationToOperationStyles.titleWrapper))(
              <.p(^.className := js.Array(TextStyles.bigText, TextStyles.boldText))(
                unescape(I18n.t("sequence.prompting-to-propose.intro"))
              )
            ),
            <.div(^.className := js.Array(PromptingToProposeInRelationToOperationStyles.ctaWrapper))(
              <.button(
                ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton),
                ^.onClick := openProposalModal
              )(
                <.i(^.className := js.Array(FontAwesomeStyles.pencil))(),
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
                    maybeLocation = self.props.wrapped.maybeLocation,
                    language = self.props.wrapped.language
                  )
                )()
              )
            ),
            <.div(^.className := PromptingToProposeInRelationToOperationStyles.ctaWrapper)(
              <.button(
                ^.className := js.Array(CTAStyles.basic, CTAStyles.basicOnButton, CTAStyles.moreDiscreet),
                ^.onClick := onNextProposal
              )(
                <.i(^.className := js.Array(FontAwesomeStyles.stepForward))(),
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
