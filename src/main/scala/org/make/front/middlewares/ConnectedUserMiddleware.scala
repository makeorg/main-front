package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions._
import org.make.front.components.AppState
import org.make.services.user.UserServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global

class ConnectedUserMiddleware(override val apiBaseUrl: String) extends UserServiceComponent {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (store: Store[AppState]) => { (dispatch: Dispatch) =>
    {
      case LogoutAction =>
        userService.logout()
        // toDo: add a dispatch(ResetStore)
        dispatch(LogoutAction)
      case action: LoggedInAction =>
        dispatch(action)
      case ReloadUserAction =>
        userService.getCurrentUser().map(user => dispatch(LoggedInAction(user)))
      case action => dispatch(action)
    }
  }

}
