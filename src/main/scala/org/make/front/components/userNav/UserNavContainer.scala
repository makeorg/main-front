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

package org.make.front.components.userNav

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.front.actions.LogoutAction
import org.make.front.components.AppState

object UserNavContainer extends RouterProps {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced {
    dispatch: Dispatch => (state: AppState, _: Props[Unit]) =>
      UserNav.UserNavProps(
        isConnected = state.connectedUser.isDefined,
        userFirstName = state.connectedUser.flatMap(_.firstName).orElse(state.connectedUser.flatMap(_.organisationName)),
        avatarUrl = state.connectedUser.flatMap(_.profile).flatMap(_.avatarUrl),
        //login = ()  => dispatch(LoginRequired()),
        logout = () => dispatch(LogoutAction),
        country = state.country
      )
  }(UserNav.reactClass)
}
