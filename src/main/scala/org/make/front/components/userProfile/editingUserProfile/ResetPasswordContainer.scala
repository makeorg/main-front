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

package org.make.front.components.userProfile.editingUserProfile

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.ReloadUserAction
import org.make.front.components.AppState
import org.make.front.components.userProfile.editingUserProfile.ResetPassword.ResetPasswordProps
import org.make.services.user.UserService

import scala.concurrent.Future

object ResetPasswordContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ResetPassword.reactClass)

  def selectorFactory: Dispatch => (AppState, Props[Unit]) => ResetPasswordProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      def changePassword(oldPassword: Option[String], newPassword: String): Future[Unit] = {
        state.connectedUser match {
          case Some(user) => UserService.changePassword(user.userId.value, oldPassword, newPassword)
          case None       =>
            // should never happen
            dispatch(ReloadUserAction)
            Future.successful({})
        }
      }
      ResetPasswordProps(hasPassword = state.connectedUser.exists(_.hasPassword), changePassword = changePassword)
    }
}
