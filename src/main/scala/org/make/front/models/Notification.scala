package org.make.front.models

sealed trait NotificationLevel

object NotificationLevel {
  object Info extends NotificationLevel
  object Alert extends NotificationLevel
  object Error extends NotificationLevel
  object Success extends NotificationLevel
}

case class Notification(identifier: Int, level: NotificationLevel, message: String, autoDismiss: Option[Int] = None)
