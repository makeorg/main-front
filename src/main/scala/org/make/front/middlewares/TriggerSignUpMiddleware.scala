package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.VoteAction
import org.make.front.components.AppState
import org.make.front.models.Location.Sequence

import scala.util.Try

object TriggerSignUpMiddleware {

  private var listeners: Map[String, TriggerSignUpListener] = Map.empty

  def addTriggerSignUpListener(id: String, listener: TriggerSignUpListener): Unit = {
    listeners += id -> listener
  }

  def removeTriggerSignUpListener(id: String): Unit = {
    listeners -= id
  }

  final case class TriggerSignUpListener(onTriggerSignUp: () => Unit)

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (store: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case VoteAction(Sequence(_)) =>
      case VoteAction(_) =>
        if (store.getState.connectedUser.isEmpty) {
          listeners.values.foreach { listener =>
            Try(listener.onTriggerSignUp())
          }
        }
      case action => dispatch(action)
  }
}
