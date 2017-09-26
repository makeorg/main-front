package org.make.front.components.notifications

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState

object NotificationsContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Notifications.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Notifications.NotificationsProps =
    (dispatch: Dispatch) => { (state: AppState, _) =>
      Notifications.NotificationsProps(state.notifications)
    }
}
