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

package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationWording, Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.base._
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

object ConsultationProposal {

  case class ConsultationProposalProps(operation: OperationModel,
                                       maybeLocation: Option[LocationModel],
                                       language: String)

  case class ConsultationProposalState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[ConsultationProposalProps, ConsultationProposalState](
      displayName = "ConsultationProposal",
      getInitialState = { _ =>
        ConsultationProposalState(isProposalModalOpened = false)
      },
      render = self => {

        val consultation: OperationModel = self.props.wrapped.operation
        val wording: OperationWording = consultation.getWordingByLanguageOrError(self.props.wrapped.language)
        var proposalInput: Option[HTMLElement] = None

        val closeProposalModal: () => Unit = () => {
          self.setState(state => state.copy(isProposalModalOpened = false))
        }

        object DynamicConsultationProposalStyles extends StyleSheet.Inline {
          import dsl._

          val TitleColor = style(color :=! consultation.color)

        }

        def openProposalModalFromInput() = () => {
          self.setState(state => state.copy(isProposalModalOpened = true))
          TrackingService
            .track(
              "click-proposal-submit-form-open",
              TrackingContext(TrackingLocation.operationPage, operationSlug = Some(consultation.slug))
            )
          proposalInput.foreach(_.blur())
        }

        <.div(^.className := js.Array(ConsultationProposalStyles.wrapper))(
          <.p(^.className := ConsultationProposalStyles.preTitle)(
            <.i(^.className := js.Array(ConsultationProposalStyles.icon, FontAwesomeStyles.lightbulbTransparent))(),
            //Todo check translations with product team
            unescape(I18n.t("operation.consultation-proposal.title"))
          ),
          <.h1(
            ^.className := js
              .Array(ConsultationProposalStyles.proposalTitle, DynamicConsultationProposalStyles.TitleColor)
          )(unescape(wording.question)),
          <.div(^.className := js.Array(InputStyles.wrapper, ConsultationProposalStyles.proposalInputWrapper))(
            <.span(^.className := TableLayoutStyles.wrapper)(
              <.span(^.className := TableLayoutStyles.cell)(
                <.input(
                  ^.`type`.text,
                  ^.value := I18n.t("common.bait"),
                  ^.ref := ((input: HTMLElement) => proposalInput = Some(input)),
                  ^.onFocus := openProposalModalFromInput(),
                  ^.readOnly := true
                )()
              ),
              <.span(
                ^.className := js
                  .Array(TableLayoutStyles.cellVerticalAlignMiddle, ConsultationProposalStyles.charsLimit)
              )(
                <.span(^.className := TextStyles.smallText)(
                  (unescape(I18n.t("operation.proposal-form-in-header.limit-of-chars-info")))
                )
              )
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
                onProposalProposed = closeProposalModal,
                maybeSequence = None,
                maybeLocation = self.props.wrapped.maybeLocation,
                language = self.props.wrapped.language
              )
            )()
          ),
          <.style()(ConsultationProposalStyles.render[String], DynamicConsultationProposalStyles.render[String])
        )
      }
    )
}

object ConsultationProposalStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.white),
      padding(ThemeStyles.SpacingValue.small.pxToEm()),
      boxShadow := s"0 1px 1px 0 rgba(0, 0, 0, .5)",
      ThemeStyles.MediaQueries.beyondLargeMedium(
        padding(20.pxToEm())
      )
    )

  val preTitle: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries.beyondSmall(
        fontSize(18.pxToEm())
      )
    )

  val icon: StyleA =
    style(marginRight(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val proposalTitle: StyleA =
    style(
      ThemeStyles.Font.circularStdBold,
      fontSize(15.pxToEm()),
      lineHeight(1),
      ThemeStyles.MediaQueries.beyondSmall(fontSize(18.pxToEm()))
    )

  val proposalInputWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.smaller.pxToEm()),
      padding(`0`, ThemeStyles.SpacingValue.smaller.pxToEm()),
      unsafeChild("input")(ThemeStyles.Font.circularStdBold, cursor.text, borderColor(ThemeStyles.BorderColor.lighter))
    )

  val charsLimit: StyleA =
    style(textAlign.right, color(ThemeStyles.TextColor.lighter))
}
