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
import org.make.front.actions.VoteAction
import org.make.front.components.AppState
import org.make.front.models.{Location, OperationId, ThemeId}
import org.make.front.models.Location.Sequence

import scala.util.Try

object TriggerSignUpMiddleware {

  private var listeners: Map[String, TriggerSignUpListener] = Map.empty

  def addTriggerSignUpListener(id: String, listener: TriggerSignUpListener): Unit = {
    listeners += id -> listener
  }

  def removeTriggerSignUpListener(id: String): Unit = {
    listeners -= id
  }

  final case class TriggerSignUpListener(onTriggerSignUp: (Location, Option[OperationId], Option[ThemeId]) => Unit)

  val handle: Store[AppState] => Dispatch => Any => Any = (store: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case VoteAction(Sequence(_), _, _) =>
      case VoteAction(location, maybeOperationId, maybeThemeId) =>
        if (store.getState.connectedUser.isEmpty) {
          listeners.values.foreach { listener =>
            Try(listener.onTriggerSignUp(location, maybeOperationId, maybeThemeId))
          }
        }
      case action => dispatch(action)
  }
}
