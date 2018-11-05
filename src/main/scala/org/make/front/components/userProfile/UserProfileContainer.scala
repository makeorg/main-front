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

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LogoutAction
import org.make.front.components.AppState

object UserProfileContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(UserProfile.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => UserProfile.UserProfileProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      val tabs: Seq[String] = Seq("summary", "proposals", "actions", "settings")
      val activeTab: String = props.`match`.params.get("activeTab").getOrElse("proposals")

      if (!tabs.contains(activeTab)) {
        props.history.push("/404")
      }

      def logout: () => Unit = { () =>
        dispatch(LogoutAction)
        props.history.push(s"/${state.country}")
      }

      if (state.connectedUser.isDefined) {
        UserProfile.UserProfileProps(
          user = state.connectedUser,
          logout = logout,
          activeTab = activeTab,
          countryCode = state.country
        )
      } else {
        props.history.push(s"/${state.country}")
        UserProfile.UserProfileProps(user = None, logout = () => {}, activeTab = activeTab, countryCode = state.country)
      }
    }
}
