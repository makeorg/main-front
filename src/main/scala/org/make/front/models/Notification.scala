package org.make.front.models

object NotificationLevel extends Enumeration {
  val Info, Alert, Error, Success = Value
}

case class Notification(identifier: Int,
                        notificationLevel: NotificationLevel.Value,
                        message: String,
                        title: Option[String],
                        autoDismiss: Option[Int] = None)
