/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components

import org.make.front.models.{
  Operation,
  OperationList,
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
                          language: String = "fr",
                          sequenceDone: js.Array[String] = js.Array(),
                          operations: OperationList = OperationList.empty) {

  def themes: js.Array[TranslatedThemeModel] =
    configuration.map(_.themesForLocale(country, language)).getOrElse(js.Array())
  def findTheme(slug: String): Option[TranslatedThemeModel] = themes.find(_.slug == slug)

  def isSequenceDone(sequenceId: String): Boolean = sequenceDone.contains(sequenceId)
}
