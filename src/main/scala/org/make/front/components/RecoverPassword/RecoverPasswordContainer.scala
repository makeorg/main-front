package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.{ClosePasswordRecoveryModalAction, LoginRequired, NotifyInfo}
import org.make.front.components.RecoverPassword.PasswordRecoveryComponent
import org.make.front.components.RecoverPassword.PasswordRecoveryComponent.{PasswordRecoveryProps, _}
import org.make.front.facades.{Configuration, I18n}
import org.make.front.models.AppState
import org.make.services.user.UserServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object PasswordRecoveryContainerComponent extends UserServiceComponent {

  override val apiBaseUrl: String = Configuration.apiUrl

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(PasswordRecoveryComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => PasswordRecoveryProps =
    (dispatch: Dispatch) => {
      def closeModal() = {
        dispatch(ClosePasswordRecoveryModalAction)
      }

      def handleSubmit(self: Self[PasswordRecoveryProps, PasswordRecoveryState]): Unit = {
        userService.resetPasswordRequest(self.state.email).onComplete {
          case Success(_) =>
            dispatch(ClosePasswordRecoveryModalAction)
            dispatch(NotifyInfo(message = I18n.t("form.passwordRecovery.notification.message"), title = None))
            self.setState(self.state.copy(errorMessage = "", email = ""))
          case Failure(e) =>
            println(e.getMessage)
            self.setState(self.state.copy(errorMessage = I18n.t("form.passwordRecovery.emailDoesNotExist")))
        }
      }

      def handleReturnLinkClick() = {
        dispatch(ClosePasswordRecoveryModalAction)
        dispatch(LoginRequired)
      }

      (state: AppState, _) =>
        PasswordRecoveryComponent.PasswordRecoveryProps(
          state.technicalState.passwordRecoveryModalIsOpen,
          closeModal,
          handleReturnLinkClick,
          handleSubmit
        )
    }
}
