package org.make.front.reducers

import org.make.front.actions._
import org.make.front.models.{Notification, NotificationLevel}

object NotificationReducer {

  def reduce(maybeNotifications: Option[Seq[Notification]], action: Any): Seq[Notification] = {
    val notifications = maybeNotifications.getOrElse(Seq.empty)
    action match {
      case action: NotifyAction =>
        addNotification(generateIdentifier(action), notifications, action match {
          case _: NotifyInfo    => NotificationLevel.Info
          case _: NotifyError   => NotificationLevel.Error
          case _: NotifyAlert   => NotificationLevel.Alert
          case _: NotifySuccess => NotificationLevel.Success
        }, action.message, action.title, action.autoDismiss)
      case action: DismissNotification =>
        dismissNotification(notifications, action.identifier)
      case _ => notifications
    }
  }

  def generateIdentifier(notifyAction: NotifyAction): Int = {
    (notifyAction.title.getOrElse("") + notifyAction.message).hashCode()
  }

  private def addNotification(identifier: Int,
                              notifications: Seq[Notification],
                              level: NotificationLevel.Value,
                              message: String,
                              title: Option[String],
                              autoDismiss: Option[Int]): Seq[Notification] = {

    notifications :+ Notification(identifier = identifier, notificationLevel = level, message = message, title = title)
  }

  private def dismissNotification(notifications: Seq[Notification], identifier: Int): Seq[Notification] = {
    notifications.filter(_.identifier != identifier)
  }
}
