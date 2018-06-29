/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
            dispatch(NotifyInfo(message = I18n.t("authenticate.recover-password.notifications.success")))
            props.wrapped.onRecoverPasswordSuccess()
          case Failure(_) =>
        }
        future
      }

      RecoverPassword.RecoverPasswordProps(handleSubmit)
    }
}
