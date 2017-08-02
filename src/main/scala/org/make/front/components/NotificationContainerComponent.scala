package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.DismissNotification
import org.make.front.models.AppState

object NotificationContainerComponent {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced(selectorFactory)(NotificationListComponent(_))

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => NotificationListComponent.WrappedProps =
    (dispatch: Dispatch) => {
      val onClose = (identifier: Int) => {
        val action = DismissNotification(identifier)
        dispatch(action)
      }

      (state: AppState, _) =>
        NotificationListComponent.WrappedProps(state.notifications, onClose = onClose)
    }
}
