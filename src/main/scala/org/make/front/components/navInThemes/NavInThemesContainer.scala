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

package org.make.front.components.navInThemes

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState

object NavInThemesContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(NavInThemes.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => NavInThemes.NavInThemesProps =
    (dispatch: Dispatch) => { (appState: AppState, _: Props[Unit]) =>
      if (appState.configuration.isEmpty) {
        dispatch(LoadConfiguration)
      }

      val shouldDisplayTheme =
        appState.configuration
          .flatMap(_.supportedCountries.find(_.countryCode == appState.country).map(_.coreIsAvailable))
          .getOrElse(false)

      NavInThemes.NavInThemesProps(appState.themes, shouldDisplayTheme, appState.country)
    }
}
