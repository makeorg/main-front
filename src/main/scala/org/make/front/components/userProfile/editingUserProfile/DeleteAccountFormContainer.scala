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

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.client.BadRequestHttpException
import org.make.core.validation.{Constraint, PasswordConstraint, RegexConstraint, SameConstraint}
import org.make.front.actions.{LogoutAction, NotifySuccess}
import org.make.front.components.AppState
import org.make.front.components.authenticate.register.getErrorsMessagesFromApiErrors
import org.make.front.components.userProfile.editingUserProfile.DeleteAccountForm.{
  DeleteAccountFormProps,
  DeleteAccountFormState
}
import org.make.front.facades.{I18n, Replacements}
import org.make.services.user.UserService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object DeleteAccountFormContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(DeleteAccountForm.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => DeleteAccountFormProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      val fieldsValidation: js.Array[(String, Constraint, Map[String, String])] = {
        js.Array(
          (
            "password",
            PasswordConstraint,
            Map(
              "minMessage" -> I18n.t(
                "authenticate.inputs.password.format-error-message",
                Replacements("min" -> PasswordConstraint.min.toString)
              )
            )
          ),
          (
            "email",
            new SameConstraint(state.connectedUser.map(_.email).getOrElse("")),
            Map("invalid" -> I18n.t("user-profile.delete-account.email.error"))
          )
        )
      }
      def handleOnSubmit(
        self: Self[DeleteAccountFormProps, DeleteAccountFormState]
      ): FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
        event.preventDefault()
        var errors: Map[String, String] = Map.empty

        fieldsValidation.foreach {
          case (fieldName, constraint, translation) =>
            val fieldErrors = constraint
              .validate(self.state.fields.get(fieldName), translation)
              .map(_.message)
            if (fieldErrors.nonEmpty) {
              errors += (fieldName -> fieldErrors.head)
            }
        }

        if (errors.nonEmpty) {
          self.setState(_.copy(errors = errors))
        } else {
          state.connectedUser.foreach { user =>
            val result: Future[Unit] =
              UserService.removeUser(userId = user.userId, password = self.state.fields.get("password"))

            result.onComplete {
              case Success(_) =>
                dispatch(LogoutAction)
                dispatch(NotifySuccess(message = I18n.t("user-profile.delete-account.confirmation")))
              case Failure(e) =>
                e match {
                  case exception: BadRequestHttpException =>
                    self.setState(_.copy(errors = getErrorsMessagesFromApiErrors(exception.errors).toMap))
                  case _ =>
                    self.setState(_.copy(errors = Map("global" -> I18n.t("error-message.unexpected-behaviour"))))
                }
            }
          }
        }
      }

      DeleteAccountFormProps(
        handleOnSubmit = handleOnSubmit,
        userHasPassword = state.connectedUser.forall(_.hasPassword)
      )
    }
}
