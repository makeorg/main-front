package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action
import org.make.front.models.User

case object RegisterAction extends Action
final case class RegisteredAction(user: User) extends Action

final case class LoginAction(user: User) extends Action
final case class LoggedInAction(user: User) extends Action

case object LogoutAction extends Action
