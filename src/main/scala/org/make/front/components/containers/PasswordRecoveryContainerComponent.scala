package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.{DismissNotification, NotifyInfo, PasswordRecoveryAction}
import org.make.front.components.presentationals.PasswordRecoveryComponent
import org.make.front.components.presentationals.PasswordRecoveryComponent._
import org.make.front.facades.I18n
import org.make.front.models.AppState

object PasswordRecoveryContainerComponent {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(PasswordRecoveryComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => PasswordRecoveryProps =
    (dispatch: Dispatch) => {
      def closeModal() = {
        dispatch(PasswordRecoveryAction(openModal = false))
      }

      def handleSubmitSuccess() = {
        dispatch(PasswordRecoveryAction(openModal = false))
        dispatch(NotifyInfo(
          message = I18n.t("form.passwordRecovery.notification.message"),
          title = Some(I18n.t("form.passwordRecovery.notification.title")),
          autoDismiss = None
        ))
      }

      (state: AppState, _) =>
        PasswordRecoveryComponent.PasswordRecoveryProps(state.passwordRecoveryModalIsOpen, closeModal, handleSubmitSuccess)
    }
}
