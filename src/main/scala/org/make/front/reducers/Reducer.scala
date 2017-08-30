package org.make.front.reducers

import org.make.front.models.AppState

object Reducer {
  def reduce(maybeState: Option[AppState], action: Any): AppState = {
    AppState(
      themes = ThemeReducer.reduce(maybeState.map(_.themes), action),
      politicalActions = PoliticalActionReducer.reduce(maybeState.map(_.politicalActions), action),
      connectedUser = ConnectedUserReducer.reduce(maybeState.flatMap(_.connectedUser), action),
      passwordRecoveryModalIsOpen = PasswordRecoveryReducer.reduce(maybeState.map(_.passwordRecoveryModalIsOpen), action),
      technicalState = TechnicalStateReducer.reduce(maybeState.map(_.technicalState), action)
    )
  }
}
