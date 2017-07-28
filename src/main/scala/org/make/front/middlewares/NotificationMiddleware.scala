package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.{DismissNotification, NotifyAction}
import org.make.front.models.AppState
import org.make.front.reducers.NotificationReducer

import scala.scalajs.js

object NotificationMiddleware {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (_: Store[AppState]) =>
    (next: Dispatch) => {
      case action: NotifyAction =>
        next(action)
        if (action.autoDismiss.nonEmpty) {
          js.timers.setTimeout(action.autoDismiss.get)(
            next(DismissNotification(NotificationReducer.generateIdentifier(action)))
          )
        }

      case action => next(action)
  }
}
