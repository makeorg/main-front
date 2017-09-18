package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.{DismissNotification, NotifyAction}
import org.make.front.components.AppState
import org.make.front.reducers.NotificationReducer

import scala.scalajs.js

object NotificationMiddleware {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (_: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case action: NotifyAction =>
        dispatch(action)
        if (action.autoDismiss.nonEmpty) {
          js.timers.setTimeout(action.autoDismiss.get)(
            dispatch(DismissNotification(NotificationReducer.generateIdentifier(action)))
          )
        }

      case action => dispatch(action)
  }
}
