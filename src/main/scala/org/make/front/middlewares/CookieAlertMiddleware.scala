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
import org.make.front.components.AppState
import org.make.front.actions._

import scala.util.Try

object CookieAlertMiddleware {

  private var listeners: Map[String, CookieAlertListener] = Map.empty

  final case class CookieAlertListener(onDismissCookieAlert: () => Unit)

  def addCookieAlertListener(id: String, listener: CookieAlertListener): Unit = {
    listeners += id -> listener
  }

  def removeCookieAlertListener(id: String): Unit = {
    listeners -= id
  }

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (_: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case OnDismissCookieAlert =>
        listeners.values.foreach(listener => Try(listener.onDismissCookieAlert()))
      case action => dispatch(action)
  }
}
