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

package org.make.front.reducers

import org.make.front.components.AppState

import scala.scalajs.js

object Reducer {
  def reduce(maybeState: Option[AppState], action: Any): AppState = {

    AppState(
      bait = BaitReducer.reduce(maybeState.map(_.bait), action).getOrElse("Il faut "),
      country = CountryReducer.reduce(maybeState.map(_.country), action).getOrElse("FR"),
      language = LanguageReducer.reduce(maybeState.map(_.language), action).getOrElse("fr"),
      configuration = ConfigurationReducer.reduce(maybeState.flatMap(_.configuration), action),
      politicalActions = PoliticalActionReducer.reduce(maybeState.map(_.politicalActions), action),
      connectedUser = ConnectedUserReducer.reduce(maybeState.flatMap(_.connectedUser), action),
      sequenceDone = SequenceDoneReducer.reduce(maybeState.map(_.sequenceDone), action).getOrElse(js.Array())
    )
  }
}
