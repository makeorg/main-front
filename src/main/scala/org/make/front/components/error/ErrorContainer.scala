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

package org.make.front.components.error

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState

import scala.util.Random

object ErrorContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Error.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Error.ErrorProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      def redirectToRandomTheme: () => Unit = { () =>
        if (state.configuration.isEmpty) {
          dispatch(LoadConfiguration)
        }

        val randomThemeSlug = Random.shuffle(state.themes.toSeq).head.slug
        props.history.push(s"/theme/$randomThemeSlug")
      }

      Error.ErrorProps(redirectToRandomTheme = redirectToRandomTheme)

    }
}
