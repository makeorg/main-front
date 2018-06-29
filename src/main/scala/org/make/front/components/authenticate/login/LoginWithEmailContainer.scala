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

package org.make.front.components.authenticate.login

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.actions.LoggedInAction
import org.make.front.components.AppState
import org.make.front.components.authenticate.login.LoginWithEmail.LoginWithEmailProps
import org.make.services.tracking.TrackingService
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object LoginWithEmailContainer {

  case class LoginWithEmailContainerProps(note: String,
                                          trackingContext: TrackingContext,
                                          trackingParameters: Map[String, String],
                                          onSuccessfulLogin: () => Unit = () => {})

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    dispatch => (_: AppState, props: Props[LoginWithEmailContainerProps]) =>
      def signIn(email: String, password: String): Future[_] = {
        val result = UserService.login(email, password)
        result.onComplete {
          case Success(user) =>
            TrackingService.track(
              "signin-email-success",
              props.wrapped.trackingContext,
              props.wrapped.trackingParameters
            )
            dispatch(LoggedInAction(user))
            props.wrapped.onSuccessfulLogin()
          case Failure(_) =>
            TrackingService.track(
              "signin-email-failure",
              props.wrapped.trackingContext,
              props.wrapped.trackingParameters
            )
        }
        result
      }
      LoginWithEmailProps(
        note = props.wrapped.note,
        trackingContext = props.wrapped.trackingContext,
        trackingParameters = props.wrapped.trackingParameters,
        connectUser = signIn
      )
  }(LoginWithEmail.reactClass)

}
