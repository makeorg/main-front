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

package org.make.front.components.politicalActions

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadPoliticalAction
import org.make.front.components.AppState
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}

object PoliticalActionsContainer {

  case class PoliticalActionsContainerProps(currentTheme: Option[TranslatedThemeModel])

  lazy val reactClass: ReactClass =
    ReactRedux.connectAdvanced(selectorFactory)(PoliticalActionsList.reactClass)

  def selectorFactory: (
    Dispatch
  ) => (AppState, Props[PoliticalActionsContainerProps]) => PoliticalActionsList.PoliticalActionsListProps = {
    (dispatch: Dispatch) =>
      { (state: AppState, props: Props[PoliticalActionsContainerProps]) =>
        val politicalActionsList = state.politicalActions

        dispatch(LoadPoliticalAction)

        PoliticalActionsList.PoliticalActionsListProps(
          politicalActionsList.filter(_.themeSlug == props.wrapped.currentTheme.map(_.slug))
        )
      }
  }
}
