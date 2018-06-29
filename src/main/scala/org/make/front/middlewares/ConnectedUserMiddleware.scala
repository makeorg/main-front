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
import org.make.front.actions._
import org.make.front.components.AppState
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global

class ConnectedUserMiddleware {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (store: Store[AppState]) => { (dispatch: Dispatch) =>
    {
      case LogoutAction =>
        UserService.logout()
        // toDo: add a dispatch(ResetStore)
        dispatch(LogoutAction)
      case action: LoggedInAction =>
        dispatch(action)
      case ReloadUserAction =>
        UserService.getCurrentUser().map(user => dispatch(LoggedInAction(user)))
      case action => dispatch(action)
    }
  }

}
