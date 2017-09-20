package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyInfo
import org.make.front.components.AppState
import org.make.front.components.recoverPassword.PasswordRecovery
import org.make.front.components.recoverPassword.PasswordRecovery.PasswordRecoveryProps
import org.make.front.facades.{Configuration, I18n}
import org.make.services.user.UserServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object RecoverPasswordContainer extends UserServiceComponent {

  override val apiBaseUrl: String = Configuration.apiUrl

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(PasswordRecovery.reactClass)

  case class RecoverPasswordContainerProps(onRecoverPasswordSuccess: () => Unit = () => {})

  def selectorFactory: (Dispatch) => (AppState, Props[RecoverPasswordContainerProps]) => PasswordRecoveryProps =
    (dispatch: Dispatch) => { (_: AppState, props) =>
      def handleSubmit(email: String): Future[_] = {
        val future = userService.resetPasswordRequest(email)
        future.onComplete {
          case Success(_) =>
            dispatch(NotifyInfo(message = I18n.t("form.passwordRecovery.notification.message"), title = None))
            props.wrapped.onRecoverPasswordSuccess()
          case Failure(_) =>
        }
        future
      }

      PasswordRecovery.PasswordRecoveryProps(handleSubmit)
    }
}
