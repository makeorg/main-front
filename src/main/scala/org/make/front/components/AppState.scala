package org.make.front.components

import org.make.front.models.{Notification, PoliticalAction, Theme, User}

final case class AppState(themes: Seq[Theme],
                          politicalActions: Seq[PoliticalAction],
                          connectedUser: Option[User],
                          notifications: Seq[Notification])
