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

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.components.AppState
import org.make.front.components.submitProposal.SubmitProposalAndAuthenticate.SubmitProposalAndAuthenticateProps
import org.make.front.models.{
  RegisterProposal,
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.services.proposal.ProposalService
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom

import scala.concurrent.Future

object SubmitProposalAndAuthenticateContainer {

  case class SubmitProposalAndAuthenticateContainerProps(intro: (ReactElement) => ReactElement = identity,
                                                         trackingContext: TrackingContext,
                                                         trackingParameters: Map[String, String],
                                                         trackingInternalOnlyParameters: Map[String, String],
                                                         onProposalProposed: () => Unit,
                                                         maybeTheme: Option[TranslatedThemeModel],
                                                         maybeOperation: Option[OperationModel],
                                                         maybeSequence: Option[SequenceId],
                                                         maybeLocation: Option[LocationModel])

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _ => (appState: AppState, props: Props[SubmitProposalAndAuthenticateContainerProps]) =>
      def propose(content: String): Future[RegisterProposal] = {
        val location = LocationModel.firstByPrecedence(
          location = props.wrapped.maybeLocation,
          sequence = props.wrapped.maybeSequence.map(sequenceId => LocationModel.Sequence(sequenceId)),
          themePage = props.wrapped.maybeTheme.map(theme        => LocationModel.ThemePage(theme.id)),
          operationPage =
            props.wrapped.maybeOperation.map(operation => LocationModel.OperationPage(operation.operationId)),
          fallback = LocationModel.UnknownLocation(dom.window.location.href)
        )
        ProposalService.createProposal(
          content = content,
          location = location,
          themeId = props.wrapped.maybeTheme.map(_.id.value),
          operation = props.wrapped.maybeOperation,
          country = appState.country,
          language = appState.language
        )
      }

      SubmitProposalAndAuthenticateProps(
        trackingContext = props.wrapped.trackingContext,
        trackingParameters = props.wrapped.trackingParameters,
        trackingInternalOnlyParameters = props.wrapped.trackingInternalOnlyParameters,
        intro = props.wrapped.intro,
        maybeTheme = props.wrapped.maybeTheme,
        maybeOperation = props.wrapped.maybeOperation,
        onProposalProposed = props.wrapped.onProposalProposed,
        propose = propose
      )
  }(SubmitProposalAndAuthenticate.reactClass)

}
