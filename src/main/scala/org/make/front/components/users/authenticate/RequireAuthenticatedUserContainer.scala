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

package org.make.front.components.users.authenticate

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.components.users.authenticate.RequireAuthenticatedUser.RequireAuthenticatedUserProps
import org.make.front.models.{OperationId, QuestionId}
import org.make.services.tracking.TrackingService.TrackingContext

object RequireAuthenticatedUserContainer {

  case class RequireAuthenticatedUserContainerProps(maybeOperationId: Option[OperationId],
                                                    trackingContext: TrackingContext,
                                                    trackingParameters: Map[String, String],
                                                    trackingInternalOnlyParameters: Map[String, String],
                                                    intro: ReactElement,
                                                    registerView: String,
                                                    defaultView: String = "register",
                                                    onceConnected: () => Unit,
                                                    maybeQuestionId: Option[QuestionId])

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _: Dispatch => (state: AppState, props: Props[RequireAuthenticatedUserContainerProps]) =>
      val registerTitle: Option[String] = props.wrapped.maybeOperationId.flatMap { operationId =>
        state.operations
          .findById(operationId)
          .flatMap(_.getOperationExpanded(country = state.country))
          .flatMap(
            _.wordings
              .find(_.language == state.language)
              .flatMap(_.registerTitle)
          )
      }
      RequireAuthenticatedUserProps(
        operationId = props.wrapped.maybeOperationId,
        registerTitle = registerTitle,
        trackingContext = props.wrapped.trackingContext,
        trackingParameters = props.wrapped.trackingParameters,
        trackingInternalOnlyParameters = props.wrapped.trackingInternalOnlyParameters,
        intro = props.wrapped.intro,
        registerView = props.wrapped.registerView,
        defaultView = props.wrapped.defaultView,
        onceConnected = props.wrapped.onceConnected,
        isConnected = state.connectedUser.isDefined,
        questionId = props.wrapped.maybeQuestionId
      )

  }(RequireAuthenticatedUser.reactClass)
}
