package org.make.front.components

import org.make.front.models._

final case class AppState(configuration: Option[BusinessConfiguration],
                          politicalActions: Seq[PoliticalAction],
                          connectedUser: Option[User],
                          notifications: Seq[Notification],
                          locale: String = "fr") {

  def themes: Seq[Theme] = configuration.map(_.themesForLocale(locale)).getOrElse(Seq.empty)
  def findTheme(slug: String): Option[Theme] = themes.find(_.slug == slug)
}
