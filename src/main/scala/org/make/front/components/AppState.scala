package org.make.front.components

import org.make.front.models.{
  BusinessConfiguration => BusinessConfigurationModel,
  OperationExpanded     => OperationModel,
  PoliticalAction       => PoliticalActionModel,
  TranslatedTheme       => TranslatedThemeModel,
  User                  => UserModel
}

final case class AppState(configuration: Option[BusinessConfigurationModel],
                          politicalActions: Seq[PoliticalActionModel],
                          operations: Seq[OperationModel],
                          bait: String = "Il faut ",
                          connectedUser: Option[UserModel],
                          country: String = "FR",
                          language: String = "fr") {

  def themes: Seq[TranslatedThemeModel] = configuration.map(_.themesForLocale(country, language)).getOrElse(Seq.empty)
  def findTheme(slug: String): Option[TranslatedThemeModel] = themes.find(_.slug == slug)

  def findOperation(slug: String): Option[OperationModel] = operations.find(_.slug == slug)

}
