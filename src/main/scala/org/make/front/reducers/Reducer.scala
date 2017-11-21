package org.make.front.reducers

import org.make.front.components.AppState

object Reducer {
  def reduce(maybeState: Option[AppState], action: Any): AppState = {
    AppState(
      configuration = ConfigurationReducer.reduce(maybeState.flatMap(_.configuration), action),
      politicalActions = PoliticalActionReducer.reduce(maybeState.map(_.politicalActions), action),
      connectedUser = ConnectedUserReducer.reduce(maybeState.flatMap(_.connectedUser), action),
      operations = OperationsReducer.reduce(maybeState.map(_.operations), action)
    )
  }
}
