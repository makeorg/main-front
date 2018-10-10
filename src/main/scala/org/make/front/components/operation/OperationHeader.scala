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

package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationWording, Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles._
import org.make.front.styles.base.{LayoutRulesStyles, TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

object OperationHeader {

  case class OperationHeaderProps(operation: OperationModel, maybeLocation: Option[LocationModel], language: String)

  case class OperationHeaderState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[OperationHeaderProps, OperationHeaderState](
      displayName = "OperationHeader",
      getInitialState = { _ =>
        OperationHeaderState(isProposalModalOpened = false)
      },
      render = { (self) =>
        val operation: OperationModel = self.props.wrapped.operation
        val wording: OperationWording = operation.getWordingByLanguageOrError(self.props.wrapped.language)
        var proposalInput: Option[HTMLElement] = None

        val closeProposalModal: () => Unit = () => {
          self.setState(_.copy(isProposalModalOpened = false))
        }

        def openProposalModalFromInput() = () => {
          self.setState(_.copy(isProposalModalOpened = true))
          TrackingService
            .track(
              eventName = "click-proposal-submit-form-open",
              trackingContext = TrackingContext(TrackingLocation.operationPage, operationSlug = Some(operation.slug))
            )
          proposalInput.foreach(_.blur())
        }

        object DynamicOperationHeaderStyles extends StyleSheet.Inline {
          import dsl._

          val proposalInputIntro = style(color :=! operation.color)
        }

        <.header(^.className := OperationHeaderStyles.wrapper)(
          <.div(^.className := LayoutRulesStyles.centeredRow)(
            <.h1(^.className := js.Array(TextStyles.smallTitle, OperationHeaderStyles.title))(
              unescape(I18n.t("operation.proposal-form-in-header.intro"))
            ),
            <.p(
              ^.className := js.Array(
                TextStyles.bigText,
                TextStyles.intro,
                OperationHeaderStyles.proposalInputIntro,
                DynamicOperationHeaderStyles.proposalInputIntro
              )
            )(unescape(wording.question)),
            <.p(
              ^.className := js.Array(
                InputStyles.wrapper,
                InputStyles.withIcon,
                InputStyles.biggerWithIcon,
                OperationHeaderStyles.proposalInputWithIconWrapper
              )
            )(
              <.span(^.className := TableLayoutStyles.wrapper)(
                <.span(^.className := js.Array(TableLayoutStyles.cell, OperationHeaderStyles.inputWrapper))(
                  <.input(
                    ^.`type`.text,
                    ^.value := I18n.t("common.bait"),
                    ^.ref := ((input: HTMLElement) => proposalInput = Some(input)),
                    ^.onFocus := openProposalModalFromInput(),
                    ^.readOnly := true
                  )()
                ),
                <.span(^.className := TableLayoutStyles.cellVerticalAlignMiddle)(
                  <.span(^.className := js.Array(TextStyles.smallText, OperationHeaderStyles.textLimitInfo))(
                    I18n.t("operation.proposal-form-in-header.limit-of-chars-info")
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
                  operation = operation,
                  onProposalProposed = closeProposalModal,
                  maybeSequence = None,
                  maybeLocation = self.props.wrapped.maybeLocation,
                  language = self.props.wrapped.language
                )
              )()
            )
          ),
          <.style()(OperationHeaderStyles.render[String], DynamicOperationHeaderStyles.render[String])
        )
      }
    )

}

object OperationHeaderStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      display.block,
      padding(ThemeStyles.SpacingValue.medium.pxToEm(), `0`),
      ThemeStyles.MediaQueries.beyondSmall(padding(ThemeStyles.SpacingValue.larger.pxToEm(), `0`)),
      backgroundColor(ThemeStyles.BackgroundColor.blackMoreTransparent)
    )

  val title: StyleA =
    style(textAlign.center)

  val proposalInputIntro: StyleA =
    style(textAlign.center)

  val proposalInputWithIconWrapper: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm()),
      boxShadow := "0 2px 5px 0 rgba(0,0,0,0.50)",
      &.before(content := "'\\F0EB'"),
      unsafeChild("input")(ThemeStyles.Font.circularStdBold, cursor.text)
    )

  val inputWrapper: StyleA =
    style(width(100.%%))

  val textLimitInfo: StyleA =
    style(padding(1.em), lineHeight.initial, color(ThemeStyles.TextColor.lighter), whiteSpace.nowrap)
}
