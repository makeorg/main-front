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
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.submitProposal.ConfirmationOfProposalSubmission.ConfirmationOfProposalSubmissionProps
import org.make.front.components.submitProposal.SubmitProposalFormContainer.SubmitProposalFormContainerProps
import org.make.front.components.users.authenticate.RequireAuthenticatedUserContainer.RequireAuthenticatedUserContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationExpanded => OperationModel, TranslatedTheme => TranslatedThemeModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{LayoutRulesStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object SubmitProposalAndAuthenticate {

  case class SubmitProposalAndAuthenticateState(proposal: String,
                                                displayedComponent: String = "submit-proposal",
                                                errorMessage: Option[String])

  object SubmitProposalAndAuthenticateState {
    val empty: SubmitProposalAndAuthenticateState =
      SubmitProposalAndAuthenticateState(proposal = "", errorMessage = None)
  }

  case class SubmitProposalAndAuthenticateProps(intro: (ReactElement) => ReactElement,
                                                trackingContext: TrackingContext,
                                                trackingParameters: Map[String, String],
                                                trackingInternalOnlyParameters: Map[String, String],
                                                maybeTheme: Option[TranslatedThemeModel],
                                                maybeOperation: Option[OperationModel],
                                                onProposalProposed: () => Unit,
                                                propose: (String)      => Future[_])

  val reactClass: ReactClass =
    React.createClass[SubmitProposalAndAuthenticateProps, SubmitProposalAndAuthenticateState](
      displayName = "SubmitProposalAndAuthenticate",
      getInitialState = { _ =>
        SubmitProposalAndAuthenticateState.empty
      },
      componentWillReceiveProps = { (self, props) =>
        if (self.state.displayedComponent == "submit-proposal") {
          TrackingService
            .track(
              eventName = "display-proposal-submit-journey",
              trackingContext =
                TrackingContext(TrackingLocation.submitProposalPage, props.wrapped.trackingContext.operationSlug),
              parameters = props.wrapped.trackingParameters,
              internalOnlyParameters = props.wrapped.trackingInternalOnlyParameters
            )
        }
      },
      render = { self =>
        val props = self.props.wrapped

        val onConnectionOk: () => Unit = { () =>
          props.propose(self.state.proposal).onComplete {
            case Success(_) => self.setState(_.copy(displayedComponent = "result"))
            case Failure(_) =>
              self.setState(
                _.copy(
                  displayedComponent = "submit-proposal",
                  errorMessage = Some(unescape(I18n.t("submit-proposal.error-message")))
                )
              )
          }
        }

        val handleSubmitProposal: String => Unit = { proposal =>
          self.setState(_.copy(displayedComponent = "connect", proposal = proposal))
        }

        if (self.state.displayedComponent == "submit-proposal") {
          <.div(^.className := LayoutRulesStyles.centeredRow)(
            self.props.wrapped.intro(
              <.SubmitProposalFormContainerComponent(
                ^.wrapped := SubmitProposalFormContainerProps(
                  trackingContext = self.props.wrapped.trackingContext,
                  trackingParameters = self.props.wrapped.trackingParameters,
                  trackingInternalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters,
                  maybeTheme = props.maybeTheme,
                  errorMessage = None,
                  handleSubmitProposalForm = handleSubmitProposal
                )
              )()
            )
          )
        } else if (self.state.displayedComponent == "connect") {

          val intro: ReactElement =
            <.div(^.className := SubmitProposalAndAuthenticateStyles.header)(
              <.div(^.className := LayoutRulesStyles.narrowerCenteredRow)(
                <.p(^.className := SubmitProposalAndAuthenticateStyles.title)(
                  <.span(^.className := SubmitProposalAndAuthenticateStyles.intro)(
                    <.span(^.className := js.Array(TextStyles.mediumText, TextStyles.intro))(
                      unescape(I18n.t("submit-proposal.authenticate.intro"))
                    )
                  ),
                  <.br()(),
                  <.strong(^.className := TextStyles.bigTitle)(unescape(I18n.t("submit-proposal.authenticate.title")))
                )
              )
            )

          <.div()(
            <.RequireAuthenticatedUserComponent(
              ^.wrapped := RequireAuthenticatedUserContainerProps(
                maybeOperationId = self.props.wrapped.maybeOperation.map(_.operationId),
                trackingContext = TrackingContext(
                  TrackingLocation.submitProposalPage,
                  self.props.wrapped.trackingContext.operationSlug
                ),
                trackingParameters = self.props.wrapped.trackingParameters,
                trackingInternalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters,
                intro = intro,
                onceConnected = onConnectionOk,
                defaultView = "register-expanded",
                registerView = "register-expanded",
                maybeQuestionId = self.props.wrapped.maybeOperation.flatMap(_.questionId)
              )
            )(),
            <.style()(SubmitProposalAndAuthenticateStyles.render[String])
          )

        } else if (self.state.displayedComponent == "result") {
          <.div(^.className := LayoutRulesStyles.centeredRow)(
            <.ConfirmationOfProposalSubmissionComponent(
              ^.wrapped := ConfirmationOfProposalSubmissionProps(
                trackingParameters = self.props.wrapped.trackingParameters,
                trackingInternalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters,
                maybeTheme = self.props.wrapped.maybeTheme,
                maybeOperation = self.props.wrapped.maybeOperation,
                onBack = self.props.wrapped.onProposalProposed,
                onSubmitAnotherProposal = () => {
                  self.setState(_.copy(proposal = "", displayedComponent = "submit-proposal", errorMessage = None))
                }
              )
            )()
          )
        } else {
          <.div.empty
        }
      }
    )
}

object SubmitProposalAndAuthenticateStyles extends StyleSheet.Inline {

  import dsl._

  val header: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.larger.pxToEm()))

  val title: StyleA =
    style(textAlign.center)

  val intro: StyleA =
    style(display.inlineBlock, marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()))
}
