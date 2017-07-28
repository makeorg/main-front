package org.make.front.reducers

import org.make.front.models.AppState

object Reducer {
  def reduce(maybeState: Option[AppState], action: Any): AppState = {
    AppState(notifications = NotificationReducer.reduce(maybeState.map(_.notifications), action))
  }
}
