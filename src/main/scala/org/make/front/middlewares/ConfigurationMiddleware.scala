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

package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.{LoadConfiguration, NotifyError, SetConfiguration}
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.services.ConfigurationService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object ConfigurationMiddleware {

  var lastCall: Option[js.Date] = None

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (appStore: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case LoadConfiguration =>
        if ((lastCall.isEmpty || lastCall.exists { time =>
              (new js.Date().getTime() - time.getTime()) > 2000
            })
            && appStore.getState.configuration.isEmpty) {
          lastCall = Some(new js.Date())
          ConfigurationService.fetchConfiguration().onComplete {
            case Success(configuration) => dispatch(SetConfiguration(configuration))
            case Failure(e)             => dispatch(NotifyError(I18n.t("error-message.main"), None))
          }
        }
      case action => dispatch(action)
  }

}
