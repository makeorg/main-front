package org.make.front.components

import org.make.front.models.{
  BusinessConfiguration => BusinessConfigurationModel,
  PoliticalAction       => PoliticalActionModel,
  TranslatedTheme       => TranslatedThemeModel,
  User                  => UserModel
}

import scala.scalajs.js

final case class AppState(configuration: Option[BusinessConfigurationModel],
                          politicalActions: js.Array[PoliticalActionModel],
                          bait: String = "Il faut ",
                          connectedUser: Option[UserModel],
                          country: String = "FR",
                          language: String = "fr") {

  def themes: js.Array[TranslatedThemeModel] =
    configuration.map(_.themesForLocale(country, language)).getOrElse(js.Array())
  def findTheme(slug: String): Option[TranslatedThemeModel] = themes.find(_.slug == slug)
}
