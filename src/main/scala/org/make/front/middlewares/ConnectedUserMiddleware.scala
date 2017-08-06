package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions._
import org.make.front.models.AppState
import org.make.services.user.UserServiceComponent

object ConnectedUserMiddleware extends UserServiceComponent {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (store: Store[AppState]) => { (dispatch: Dispatch) =>
    {
      case LogoutAction =>
        userService.logout()
        dispatch(LogoutAction)

      case action => dispatch(action)
    }
  }
}
