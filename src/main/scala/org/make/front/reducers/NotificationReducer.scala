package org.make.front.reducers

import org.make.front.actions._
import org.make.front.models.{Notification => NotificationModel, NotificationLevel => NotificationLevelModel}

object NotificationReducer {

  def reduce(maybeNotifications: Option[Seq[NotificationModel]], action: Any): Seq[NotificationModel] = {
    val notifications = maybeNotifications.getOrElse(Seq.empty)
    action match {
      case action: NotifyAction =>
        addNotification(generateIdentifier(action), notifications, action match {
          case _: NotifyInfo    => NotificationLevelModel.Info
          case _: NotifyError   => NotificationLevelModel.Error
          case _: NotifyAlert   => NotificationLevelModel.Alert
          case _: NotifySuccess => NotificationLevelModel.Success
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
                              notifications: Seq[NotificationModel],
                              level: NotificationLevelModel.Value,
                              message: String,
                              title: Option[String],
                              autoDismiss: Option[Int]): Seq[NotificationModel] = {

    notifications :+ NotificationModel(
      identifier = identifier,
      notificationLevel = level,
      message = message,
      title = title
    )
  }

  private def dismissNotification(notifications: Seq[NotificationModel], identifier: Int): Seq[NotificationModel] = {
    notifications.filter(_.identifier != identifier)
  }
}
