package org.make.front.models

final case class AppState(themes: Seq[Theme],
                          politicalActions: Seq[PoliticalAction],
                          connectedUser: Option[User],
                          technicalState: TechnicalState)

final case class TechnicalState(notifications: Seq[Notification], showLoginModal: Boolean)
