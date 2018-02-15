package org.make.front.reducers

import org.make.front.components.AppState

object Reducer {
  def reduce(maybeState: Option[AppState], action: Any): AppState = {

    AppState(
      bait = BaitReducer.reduce(maybeState.map(_.bait), action).getOrElse("Il faut "),
      country = CountryReducer.reduce(maybeState.map(_.country), action).getOrElse("FR"),
      language = LanguageReducer.reduce(maybeState.map(_.language), action).getOrElse("fr"),
      configuration = ConfigurationReducer.reduce(maybeState.flatMap(_.configuration), action),
      politicalActions = PoliticalActionReducer.reduce(maybeState.map(_.politicalActions), action),
      connectedUser = ConnectedUserReducer.reduce(maybeState.flatMap(_.connectedUser), action)
    )
  }
}
