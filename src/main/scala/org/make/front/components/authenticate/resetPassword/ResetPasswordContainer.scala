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

package org.make.front.components.authenticate.resetPassword

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.components.AppState
import org.make.front.components.authenticate.resetPassword.PasswordReset.{PasswordResetProps, PasswordResetState}
import org.make.front.facades.I18n
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ResetPasswordContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(PasswordReset.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => PasswordResetProps =
    (_: Dispatch) => { (_: AppState, props: Props[Unit]) =>
      val userId = props.`match`.params("userId")
      val resetToken = props.`match`.params("resetToken")

      def checkResetToken(child: Self[PasswordResetProps, PasswordResetState]): Unit = {
        UserService.resetPasswordCheck(userId, resetToken).onComplete {
          case Success(isValidResetToken) => child.setState(child.state.copy(isValidResetToken = isValidResetToken))
          case Failure(e)                 => throw e
        }
      }

      def handleSubmit(self: Self[PasswordResetProps, PasswordResetState]): Unit = {
        UserService.resetPasswordChange(userId, resetToken, self.state.password).onComplete {
          case Success(_) =>
            self.setState(self.state.copy(success = true))
          case Failure(e) =>
            self.setState(
              self.state.copy(errorMessage = I18n.t("authenticate.reset-password.notifications.error-message"))
            )
        }
      }

      PasswordReset.PasswordResetProps(handleSubmit, checkResetToken)
    }
}
