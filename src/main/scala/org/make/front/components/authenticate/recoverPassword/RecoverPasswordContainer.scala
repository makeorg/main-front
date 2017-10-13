package org.make.front.components.authenticate.recoverPassword

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyInfo
import org.make.front.components.AppState
import org.make.front.components.authenticate.recoverPassword.RecoverPassword.RecoverPasswordProps
import org.make.front.facades.I18n
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object RecoverPasswordContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(RecoverPassword.reactClass)

  case class RecoverPasswordContainerProps(onRecoverPasswordSuccess: () => Unit = () => {})

  def selectorFactory: (Dispatch) => (AppState, Props[RecoverPasswordContainerProps]) => RecoverPasswordProps =
    (dispatch: Dispatch) => { (_: AppState, props) =>
      def handleSubmit(email: String): Future[_] = {
        val future = UserService.resetPasswordRequest(email)
        future.onComplete {
          case Success(_) =>
            dispatch(NotifyInfo(message = I18n.t("form.recoverPassword.notification.message")))
            props.wrapped.onRecoverPasswordSuccess()
          case Failure(_) =>
        }
        future
      }

      RecoverPassword.RecoverPasswordProps(handleSubmit)
    }
}
