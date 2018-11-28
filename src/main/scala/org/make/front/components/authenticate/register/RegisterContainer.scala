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
import org.make.front.models.{OperationExpanded, QuestionId, User => UserModel}
import org.make.services.tracking.TrackingService
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object RegisterContainer {

  case class RegisterUserProps(trackingContext: TrackingContext,
                               trackingParameters: Map[String, String],
                               trackingInternalOnlyParameters: Map[String, String],
                               onSuccessfulRegistration: () => Unit = () => {})

  def selector: ContainerComponentFactory[RegisterProps] = ReactRedux.connectAdvanced {
    dispatch => (appState: AppState, props: Props[RegisterUserProps]) =>
      def operationExpanded: Option[OperationExpanded] = appState.currentOperation.operation
      def questionId: Option[QuestionId] = operationExpanded.flatMap(_.questionId)

      def legalNote: String =
        operationExpanded
          .flatMap(_.wordings.find(_.language == appState.language).flatMap(_.legalNote))
          .getOrElse(I18n.t("authenticate.register.terms"))

      def register(): Map[String, String] => Future[UserModel] = { fields =>
        val future = UserService
          .registerUser(
            email = fields("email"),
            password = fields("password"),
            firstName = fields("firstName"),
            profession = fields.get("profession"),
            postalCode = fields.get("postalCode"),
            age = fields.get("age").map(_.toInt),
            operationId = operationExpanded.map(_.operationId),
            country = appState.country,
            language = appState.language,
            gender = fields.get("gender"),
            socioProfessionalCategory = fields.get("socioProfessionalCategory"),
            optInPartner = fields.get("optInPartner").map(_.nonEmpty),
            questionId = questionId,
            optIn = fields.get("optIn").map(_.nonEmpty)
          )
          .flatMap { _ =>
            UserService.login(fields("email"), fields("password"))
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
        note = legalNote,
        trackingContext = props.wrapped.trackingContext,
        trackingParameters = props.wrapped.trackingParameters,
        trackingInternalOnlyParameters = props.wrapped.trackingInternalOnlyParameters,
        register = register(),
        additionalFields = operationExpanded.map(_.additionalFields).getOrElse(Seq.empty),
        language = appState.language
      )
  }

  val registerWithEmailReactClass: ReactClass = selector(RegisterWithEmail.reactClass)
  val registerWithEmailExpandedReactClass: ReactClass = selector(RegisterWithEmailExpanded.reactClass)
}
