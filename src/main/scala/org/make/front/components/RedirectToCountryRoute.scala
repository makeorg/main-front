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

package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
object RedirectToCountryRoute {

  case class RedirectToCountryRouteProps(country: String)

  def apply(): ReactClass = {
    ReactRedux.connectAdvanced(selectorFactory)(
      React
        .createClass[RedirectToCountryRouteProps, Unit](componentWillMount = { self =>
          val location: String = self.props.location.pathname
          self.props.history.push(s"/${self.props.wrapped.country}$location${self.props.location.search}")
        }, render = (_) => {
          <.div()()
        })
    )
  }

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => RedirectToCountryRouteProps =
    (_: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        RedirectToCountryRouteProps(appState.country)
      }
    }
}
