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
