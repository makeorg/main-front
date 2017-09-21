package org.make.front.components.notification

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.DismissNotification
import org.make.front.components.AppState

object NotificationContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(NotificationList(_))

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => NotificationList.WrappedProps =
    (dispatch: Dispatch) => {
      val onClose = (identifier: Int) => {
        val action = DismissNotification(identifier)
        dispatch(action)
      }

      (state: AppState, _) =>
        NotificationList.WrappedProps(state.notifications, onClose = onClose)
    }
}
