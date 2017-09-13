package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action
import org.make.front.models.User

final case class LoggedInAction(user: User) extends Action
case object LogoutAction extends Action
case object OpenPasswordRecoveryModalAction extends Action
case object ClosePasswordRecoveryModalAction extends Action
case object ReloadUserAction extends Action
