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
import org.make.front.actions.ReloadUserAction
import org.make.front.components.AppState
import org.make.front.components.userProfile.editingUserProfile.OptinNewsletter.{
  OptinNewsletterProps,
  OptinNewsletterState
}
import org.make.front.facades.I18n
import org.make.services.user.UserService
import org.scalajs.dom.raw.HTMLInputElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object OptinNewsletterContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(OptinNewsletter.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => OptinNewsletterProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      def handleOnSubmit(
        self: Self[OptinNewsletterProps, OptinNewsletterState]
      ): FormSyntheticEvent[HTMLInputElement] => Unit = { event =>
        event.preventDefault()
        UserService.updateUser(optInNewsletter = Some(self.state.isChecked)).onComplete {
          case Success(_) =>
            self.setState(self.state.copy(message = I18n.t("user-profile.optin-newsletter.confirmation")))
            dispatch(ReloadUserAction)
          case Failure(e) =>
            self.setState(self.state.copy(message = I18n.t("user-profile.optin-newsletter.error")))
        }
      }

      OptinNewsletterProps(handleOnSubmit = handleOnSubmit, optInNewsletter = state.connectedUser.flatMap { user =>
        user.profile.map(_.optInNewsletter)
      }.getOrElse(false))
    }
}
