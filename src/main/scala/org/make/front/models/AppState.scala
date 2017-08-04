package org.make.front.models

final case class AppState(notifications: Seq[Notification], themes: Seq[Theme], connectedUser: Option[User])
