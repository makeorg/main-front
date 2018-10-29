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

package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.{ContainerComponentFactory, ReactRedux}
import org.make.front.actions.{LoggedInAction, NotifyInfo}
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{Operation, OperationExpanded, OperationId, Tag, User => UserModel}
import org.make.services.operation.OperationService
import org.make.services.tag.TagService
import org.make.services.tracking.TrackingService
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object RegisterContainer {

  case class RegisterUserProps(note: String,
                               trackingContext: TrackingContext,
                               trackingParameters: Map[String, String],
                               trackingInternalOnlyParameters: Map[String, String],
                               onSuccessfulRegistration: () => Unit = () => {},
                               operationId: Option[OperationId])

  def selector: ContainerComponentFactory[RegisterProps] = ReactRedux.connectAdvanced {
    dispatch => (appState: AppState, props: Props[RegisterUserProps]) =>
      def getAdditionalFields: Seq[SignUpField] = props.wrapped.operationId match {
        case Some(operationId) =>
          appState.operations
            .findById(operationId)
            .flatMap(_.getOperationExpanded(country = appState.country).map(_.additionalFields)) match {
            case None         => Seq.empty
            case Some(values) => values
          }
        case _ => Seq.empty
      }

      def register(): (RegisterState) => Future[UserModel] = { state =>
        val future = UserService
          .registerUser(
            email = state.fields("email"),
            password = state.fields("password"),
            firstName = state.fields("firstName"),
            profession = state.fields.get("profession"),
            postalCode = state.fields.get("postalCode"),
            age = state.fields.get("age").map(_.toInt),
            operationId = props.wrapped.operationId,
            country = appState.country,
            language = appState.language,
            gender = state.fields.get("gender"),
            socioProfessionalCategory = state.fields.get("socioProfessionalCategory")
          )
          .flatMap { _ =>
            UserService.login(state.fields("email"), state.fields("password"))
          }

        future.onComplete {
          case Success(user) =>
            TrackingService
              .track(
                eventName = "signup-email-success",
                trackingContext = props.wrapped.trackingContext,
                parameters = props.wrapped.trackingParameters,
                internalOnlyParameters = props.wrapped.trackingInternalOnlyParameters
              )
            dispatch(LoggedInAction(user))
            dispatch(NotifyInfo(message = I18n.t("authenticate.register.notifications.success")))
            props.wrapped.onSuccessfulRegistration()
          case Failure(_) =>
            TrackingService
              .track(
                eventName = "signup-email-failure",
                trackingContext = props.wrapped.trackingContext,
                parameters = props.wrapped.trackingParameters,
                internalOnlyParameters = props.wrapped.trackingInternalOnlyParameters
              )
        }

        future
      }
      RegisterProps(
        props.wrapped.note,
        trackingContext = props.wrapped.trackingContext,
        trackingParameters = props.wrapped.trackingParameters,
        trackingInternalOnlyParameters = props.wrapped.trackingInternalOnlyParameters,
        register = register(),
        additionalFields = getAdditionalFields
      )
  }

  val registerWithEmailReactClass: ReactClass = selector(RegisterWithEmail.reactClass)
  val registerWithEmailExpandedReactClass: ReactClass = selector(RegisterWithEmailExpanded.reactClass)
}
