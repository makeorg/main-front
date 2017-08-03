package org.make.front.models

final case class AppState(notifications: Seq[Notification],
                          themes: Seq[Theme],
                          politicalActions: Seq[PoliticalAction],
                          connectedUser: Option[User])
