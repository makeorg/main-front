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

package org.make.front.components.userProfile

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.core.validation.{Constraint, NotBlankConstraint}
import org.make.front.components.AppState
import org.make.front.components.userProfile.UserProfileForm.{UserProfileFormProps, UserProfileFormState}
import org.make.front.facades.I18n
import org.make.services.user.UserService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object UserProfileFormContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(UserProfileForm.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => UserProfileFormProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      val fieldsValidation: js.Array[(String, Constraint, Map[String, String])] = {
        js.Array(
          (
            "firstName",
            NotBlankConstraint,
            Map("notBlank" -> I18n.t("user-profile.form.inputs.empty-field-error-message"))
          )
        )
      }
      def handleOnSubmit(self: Self[UserProfileFormProps, UserProfileFormState]): FormSyntheticEvent[HTMLInputElement] => Unit =  {
        event =>
          event.preventDefault()
          var errors: Map[String, String] = Map.empty

          fieldsValidation.foreach {
            case (fieldName, constraint, translation) => {
              val fieldErrors = constraint
                .validate(self.state.fields.get(fieldName), translation)
                .map(_.message)
              if (fieldErrors.nonEmpty) {
                errors += (fieldName -> fieldErrors.head)
              }
            }
          }

          if (errors.nonEmpty) {
            self.setState(_.copy(errors = errors))
          } else {
            UserService.updateUser(
              firstName = self.state.fields.get("lastName"),
              lastName = self.state.fields.get("lastName"),
              age = self.state.fields.get("age").map(_.toInt),
              profession = self.state.fields.get("profession"),
              postalCode = self.state.fields.get("postalCode")
            ).onComplete {
              case Success(_) =>
                self.setState(
                  self.state.copy(message = I18n.t("user-profile.update.confirmation"))
                )
              case Failure(e) =>
                self.setState(
                  self.state.copy(message = I18n.t("user-profile.update.error"))
                )
            }
          }
      }

      UserProfileFormProps(handleOnSubmit = handleOnSubmit, user = state.connectedUser.get)
    }
}