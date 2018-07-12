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

package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{OperationExpanded => OperationModel, TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.ui.CTAStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object ConfirmationOfProposalSubmission {

  case class ConfirmationOfProposalSubmissionProps(trackingParameters: Map[String, String],
                                                   trackingInternalOnlyParameters: Map[String, String],
                                                   maybeTheme: Option[TranslatedThemeModel],
                                                   maybeOperation: Option[OperationModel],
                                                   onBack: ()                  => _,
                                                   onSubmitAnotherProposal: () => _)

  val reactClass: ReactClass =
    React
      .createClass[ConfirmationOfProposalSubmissionProps, Unit](
        displayName = "ConfirmationOfProposalSubmission",
        componentDidMount = { self =>
          TrackingService
            .track(
              eventName = "display-proposal-submit-validation",
              trackingContext = TrackingContext(TrackingLocation.submitProposalPage, self.props.wrapped.maybeOperation.map(_.slug)),
              parameters = self.props.wrapped.trackingParameters,
              internalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters
            )
        },
        render = { self =>
          def handleClickOnButton() = () => {
            TrackingService.track(
              eventName = "click-proposal-submit-form-open",
              trackingContext = TrackingContext(TrackingLocation.endProposalPage, self.props.wrapped.maybeOperation.map(_.slug)),
              parameters = self.props.wrapped.trackingParameters,
              internalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters
            )
            self.props.wrapped.onSubmitAnotherProposal()
          }

          def handleClickOnBackButton() = () => {
            TrackingService.track(
              eventName = "click-back-button-after-proposal-submit",
              trackingContext = TrackingContext(TrackingLocation.endProposalPage, self.props.wrapped.maybeOperation.map(_.slug)),
              parameters = self.props.wrapped.trackingParameters,
              internalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters
            )
            self.props.wrapped.onBack()
          }

          <.article(^.className := ConfirmationOfProposalSubmissionStyles.wrapper)(
            <.p(^.className := js.Array(TextStyles.bigTitle, ConfirmationOfProposalSubmissionStyles.title))(
              <.i(^.className := FontAwesomeStyles.handPeaceO)(),
              unescape("&nbsp;"),
              <.span(^.dangerouslySetInnerHTML := I18n.t("submit-proposal.confirmation.title"))()
            ),
            <.p(
              ^.className := js.Array(TextStyles.mediumText, ConfirmationOfProposalSubmissionStyles.message),
              ^.dangerouslySetInnerHTML := I18n.t("submit-proposal.confirmation.info")
            )(),
            <.button(
              ^.className := js
                .Array(ConfirmationOfProposalSubmissionStyles.cta, CTAStyles.basic, CTAStyles.basicOnButton),
              ^.onClick := handleClickOnBackButton
            )(<.i(^.className := FontAwesomeStyles.handOLeft)(), unescape("&nbsp;"), self.props.wrapped.maybeTheme.map {
              theme =>
                <.span(
                  ^.dangerouslySetInnerHTML := I18n
                    .t("submit-proposal.confirmation.back-to-theme-cta", Replacements(("theme", theme.title)))
                )()
            }.getOrElse {
              <.span(^.dangerouslySetInnerHTML := I18n.t("submit-proposal.confirmation.back-cta"))()
            }),
            <.br()(),
            <.button(
              ^.className := js
                .Array(ConfirmationOfProposalSubmissionStyles.cta, CTAStyles.basic, CTAStyles.basicOnButton),
              ^.onClick := handleClickOnButton
            )(
              <.i(^.className := FontAwesomeStyles.lightbulbTransparent)(),
              unescape("&nbsp;"),
              <.Translate(^.value := "submit-proposal.confirmation.new-proposal-cta", ^.dangerousHtml := true)()
            ),
            <.style()(ConfirmationOfProposalSubmissionStyles.render[String])
          )
        }
      )
}

object ConfirmationOfProposalSubmissionStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(textAlign.center)

  val title: StyleA =
    style(
      display.inlineBlock,
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(20)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(34))),
      ThemeStyles.MediaQueries.beyondMedium(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(46)))
    )

  val message: StyleA =
    style(
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.small.pxToEm(18)))
    )

  val cta: StyleA =
    style(
      marginTop(ThemeStyles.SpacingValue.small.pxToEm(13)),
      ThemeStyles.MediaQueries.beyondSmall(marginTop(ThemeStyles.SpacingValue.medium.pxToEm(16))),
      &.firstChild(marginTop(`0`))
    )
}
