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

import java.util.UUID
import org.make.front.models.{NotificationLevel => NotificationLevelModel}
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions._
import org.make.front.components.AppState
import org.make.front.models.Notification

import scala.scalajs.js
import scala.util.Try

object NotificationMiddleware {

  private var listeners: Map[String, NotificationListener] = Map.empty

  def addNotificationListener(id: String, listener: NotificationListener): Unit = {
    listeners += id -> listener
  }

  def removeNotificationListener(id: String): Unit = {
    listeners -= id
  }

  final case class NotificationListener(onNewNotification: (Notification) => Unit,
                                        dismissNotification: (String)     => Unit)

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (_: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case action: NotifyAction =>
        val id: String = UUID.randomUUID().toString
        val level = action match {
          case _: NotifyInfo    => NotificationLevelModel.Info
          case _: NotifyError   => NotificationLevelModel.Error
          case _: NotifyAlert   => NotificationLevelModel.Alert
          case _: NotifySuccess => NotificationLevelModel.Success
        }

        listeners.values.foreach(
          listener =>
            Try(
              listener.onNewNotification(
                Notification(identifier = id, level = level, message = action.message, autoDismiss = action.autoDismiss)
              )
          )
        )
        if (action.autoDismiss.nonEmpty) {
          js.timers.setTimeout(action.autoDismiss.get)(
            listeners.values
              .foreach(listener => Try(listener.dismissNotification(id)))
          )
        }

      case action => dispatch(action)
  }
}
