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

package org.make.front.components.cookieAlert

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.OnDismissCookieAlert
import org.make.front.components.AppState
import org.make.front.facades.{CookieOpts, Cookies}

object CookieAlertContainer {

  final case class CookieAlertContainerProps()

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(CookieAlert.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[CookieAlertContainerProps]) => CookieAlert.CookieAlertProps =
    (dispatch: Dispatch) => { (_: AppState, _: Props[CookieAlertContainerProps]) =>
      val isAlertOpened: Boolean = Cookies.get("cookieconsent_status").isEmpty

      def dismissCookieAlert: () => Unit = { () =>
        Cookies.set("cookieconsent_status", "dismiss", opts = CookieOpts(expires = 365))
        dispatch(OnDismissCookieAlert)
      }

      CookieAlert.CookieAlertProps(isAlertOpened = isAlertOpened, closeCallback = dismissCookieAlert)
    }
}
