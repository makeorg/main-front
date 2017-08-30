package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.{LoginRequired, NotifyInfo, PasswordRecoveryAction}
import org.make.front.components.presentationals.PasswordRecoveryComponent
import org.make.front.components.presentationals.PasswordRecoveryComponent._
import org.make.front.facades.I18n
import org.make.front.models.AppState

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object PasswordRecoveryContainerComponent {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(PasswordRecoveryComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => PasswordRecoveryProps =
    (dispatch: Dispatch) => {
      def closeModal() = {
        dispatch(PasswordRecoveryAction(openModal = false))
      }

      def handleSubmit(self: Self[PasswordRecoveryProps, PasswordRecoveryState]): Unit = {
        userService.recoverPassword(self.state.email).onComplete {
          case Success(_) =>
            dispatch(PasswordRecoveryAction(openModal = false))
            dispatch(NotifyInfo(
              message = I18n.t("form.passwordRecovery.notification.message"),
              title = None
            ))
            self.setState(self.state.copy(errorMessage = "", email = ""))
          case Failure(e) =>
            println(e.getMessage)
            self.setState(self.state.copy(errorMessage = I18n.t("form.passwordRecovery.emailDoesNotExist")))
        }
      }

      def handleReturOnClick() = {
        dispatch(PasswordRecoveryAction(openModal = false))
        dispatch(LoginRequired)
      }

      (state: AppState, _) =>
        PasswordRecoveryComponent.PasswordRecoveryProps(
          state.passwordRecoveryModalIsOpen,
          closeModal,
          handleReturOnClick,
          handleSubmit
        )
    }
}
