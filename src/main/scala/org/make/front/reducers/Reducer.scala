package org.make.front.reducers

import org.make.front.models.AppState

object Reducer {
  def reduce(maybeState: Option[AppState], action: Any): AppState = {
    AppState(
      notifications = NotificationReducer.reduce(maybeState.map(_.notifications), action),
      themes = ThemeReducer.reduce(maybeState.map(_.themes), action),
      politicalActions = PoliticalActionReducer.reduce(maybeState.map(_.politicalActions), action),
      connectedUser = ConnectedUserReducer.reduce(maybeState.flatMap(_.connectedUser), action)
    )
  }
}
