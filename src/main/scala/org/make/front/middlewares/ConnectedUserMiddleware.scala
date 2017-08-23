package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions._
import org.make.front.facades.I18n
import org.make.front.models.AppState
import org.make.services.user.UserServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

object ConnectedUserMiddleware extends UserServiceComponent {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (store: Store[AppState]) => { (dispatch: Dispatch) =>
    {
      case RegisterAction(username, password, firstName) =>
        userService.registerByUsernameAndPasswordAndFirstName(username, password, firstName).onComplete {
          case Success(_) =>
            userService.login(username, password).onComplete {
              case Success(user) => store.dispatch(LoggedInAction(user))
              case Failure(_)    => store.dispatch(NotifyError(I18n.t("errors.main"), Some(I18n.t("errors.loginFailed"))))
            }
          case Failure(_) => store.dispatch(NotifyError(I18n.t("errors.main"), Some("Register failed")))
        }

      case LogoutAction =>
        userService.logout()
        dispatch(LogoutAction)

      case action => dispatch(action)
    }
  }
}
