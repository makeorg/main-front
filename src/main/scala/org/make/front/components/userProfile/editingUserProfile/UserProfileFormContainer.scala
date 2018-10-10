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
import org.make.core.validation.{Constraint, LengthConstraint, NotBlankConstraint}
import org.make.front.actions.ReloadUserAction
import org.make.front.components.AppState
import org.make.front.components.userProfile.editingUserProfile.UserProfileForm.{
  UserProfileFormProps,
  UserProfileFormState
}
import org.make.front.facades.{I18n, Replacements}
import org.make.services.user.UserService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object UserProfileFormContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(UserProfileForm.reactClass)

  def selectorFactory: Dispatch => (AppState, Props[Unit]) => UserProfileFormProps =
    (dispatch: Dispatch) => { (state: AppState, _: Props[Unit]) =>
      val fieldsValidation: js.Array[(String, Constraint, Map[String, String])] = {
        js.Array(
          (
            "firstName",
            NotBlankConstraint,
            Map("notBlank" -> I18n.t("user-profile.form.inputs.empty-field-error-message"))
          ),
          (
            "postalCode",
            new LengthConstraint(max = Some(7)),
            Map("maxMessage" -> I18n.t("user-profile.form.inputs.format-error-message"))
          ),
          (
            "description",
            new LengthConstraint(min = Some(15), max = Some(450)),
            Map(
              "minMessage" -> I18n
                .t("user-profile.form.inputs.min-error-message", replacements = Replacements(("min", "15"))),
              "maxMessage" -> I18n
                .t("user-profile.form.inputs.max-error-message", replacements = Replacements(("max", "450")))
            )
          )
        )
      }
      def handleOnSubmit(
        self: Self[UserProfileFormProps, UserProfileFormState]
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
          UserService
            .updateUser(
              firstName = self.state.fields.get("firstName"),
              age = self.state.fields.get("age"),
              profession = self.state.fields.get("profession"),
              postalCode = self.state.fields.get("postalCode"),
              description = self.state.fields.get("description")
            )
            .onComplete {
              case Success(_) =>
                self.setState(_.copy(message = I18n.t("user-profile.update.confirmation")))
                dispatch(ReloadUserAction)
              case Failure(_) =>
                self.setState(
                  state => state.copy(errors = state.errors + ("global" -> I18n.t("user-profile.update.error")))
                )
            }
        }
      }

      UserProfileFormProps(handleOnSubmit = handleOnSubmit, user = state.connectedUser.get)
    }
}
