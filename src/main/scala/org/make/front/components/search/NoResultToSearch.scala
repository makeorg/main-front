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

package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.modals.FullscreenModal.FullscreenModalProps
import org.make.front.components.operation.SubmitProposalInRelationToOperation.SubmitProposalInRelationToOperationProps
import org.make.front.components.submitProposal.SubmitProposal.SubmitProposalProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object NoResultToSearch {

  final case class NoResultToSearchProps(searchValue: Option[String],
                                         maybeLocation: Option[LocationModel],
                                         maybeOperation: Option[OperationModel],
                                         language: String)

  final case class NoResultToSearchState(isProposalModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[NoResultToSearchProps, NoResultToSearchState](
      displayName = "NoResultToSearch",
      getInitialState = (_) => NoResultToSearchState(isProposalModalOpened = false),
      render = {
        self =>
          val closeProposalModal: () => Unit = () => {
            self.setState(_.copy(isProposalModalOpened = false))
          }

          val openProposalModal: (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            TrackingService.track(
              eventName = "click-proposal-submit-form-open",
              trackingContext = TrackingContext(TrackingLocation.searchResultsPage),
              parameters = Map.empty,
              internalOnlyParameters =
                self.props.wrapped.searchValue.map(query => Map("query" -> query)).getOrElse(Map.empty)
            )
            self.setState(_.copy(isProposalModalOpened = true))
          }
          <.article(^.className := js.Array(LayoutRulesStyles.centeredRow, NoResultToSearchStyles.wrapper))(
            <.p(^.className := NoResultToSearchStyles.sadSmiley)("😞"),
            <.h1(
              ^.className := js.Array(TextStyles.mediumText, NoResultToSearchStyles.searchedExpressionIntro),
              ^.dangerouslySetInnerHTML := I18n.t("search.no-results.intro")
            )(),
            <.h2(^.className := js.Array(TextStyles.mediumTitle, NoResultToSearchStyles.searchedExpression))(
              unescape("«&nbsp;" + self.props.wrapped.searchValue.getOrElse("") + "&nbsp;»")
            ),
            <.hr(^.className := NoResultToSearchStyles.messageSeparator)(),
            <.p(^.className := js.Array(TextStyles.mediumText, NoResultToSearchStyles.openProposalModalIntro))(
              unescape(I18n.t("search.no-results.prompting-to-propose"))
            ),
            <.button(
              ^.className := js
                .Array(NoResultToSearchStyles.openProposalModalButton, CTAStyles.basic, CTAStyles.basicOnButton),
              ^.onClick := openProposalModal
            )(
              <.i(^.className := js.Array(FontAwesomeStyles.pencil))(),
              unescape("&nbsp;" + I18n.t("search.no-results.propose-cta"))
            ),
            <.FullscreenModalComponent(
              ^.wrapped := FullscreenModalProps(
                isModalOpened = self.state.isProposalModalOpened,
                closeCallback = closeProposalModal
              )
            )(self.props.wrapped.maybeOperation.map { operation =>
              <.SubmitProposalInRelationToOperationComponent(
                ^.wrapped := SubmitProposalInRelationToOperationProps(
                  operation = operation,
                  onProposalProposed = () => {
                    self.setState(_.copy(isProposalModalOpened = false))
                  },
                  maybeSequence = None,
                  maybeLocation = self.props.wrapped.maybeLocation,
                  language = self.props.wrapped.language
                )
              )()
            }.getOrElse {
              <.SubmitProposalComponent(
                ^.wrapped := SubmitProposalProps(
                  trackingParameters = Map.empty,
                  trackingInternalOnlyParameters = Map.empty,
                  onProposalProposed = () => {
                    self.setState(_.copy(isProposalModalOpened = false))
                  },
                  maybeLocation = self.props.wrapped.maybeLocation,
                  maybeOperation = self.props.wrapped.maybeOperation
                )
              )()
            }),
            <.style()(NoResultToSearchStyles.render[String])
          )
      }
    )
}

object NoResultToSearchStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(paddingTop(ThemeStyles.SpacingValue.larger.pxToEm()), paddingBottom(ThemeStyles.SpacingValue.larger.pxToEm()))

  val sadSmiley: StyleA =
    style(
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(34)),
      fontSize(34.pxToEm()),
      lineHeight(1),
      textAlign.center,
      ThemeStyles.MediaQueries
        .beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm(48)), fontSize(48.pxToEm()))
    )

  val messageSeparator: StyleA = style(
    display.block,
    maxWidth(235.pxToEm()),
    margin(ThemeStyles.SpacingValue.medium.pxToEm(), auto),
    borderTop(1.px, solid, ThemeStyles.BorderColor.veryLight),
    ThemeStyles.MediaQueries
      .beyondSmall(maxWidth(470.pxToEm()), margin(ThemeStyles.SpacingValue.large.pxToEm(), auto))
  )

  val searchedExpressionIntro: StyleA =
    style(textAlign.center, unsafeChild("strong")(ThemeStyles.Font.circularStdBold))

  val searchedExpression: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm(20)),
      textAlign.center,
      ThemeStyles.MediaQueries
        .beyondSmall(marginTop(ThemeStyles.SpacingValue.small.pxToEm(34)))
    )

  val openProposalModalIntro: StyleA =
    style(textAlign.center)

  val openProposalModalButton: StyleA =
    style(display.block, margin(ThemeStyles.SpacingValue.small.pxToEm(), auto, `0`))
}
