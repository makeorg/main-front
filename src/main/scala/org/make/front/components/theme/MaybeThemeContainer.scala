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

package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState
import org.make.front.helpers.QueryString

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object MaybeThemeContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(MaybeTheme.reactClass)

  def selectorFactory: Dispatch => (AppState, Props[Unit]) => MaybeTheme.MaybeThemeProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("themeSlug")

        val queryTags: js.Array[String] =
          QueryString.parse(props.location.search).get("tagIds").map(_.split(',').toJSArray).getOrElse(js.Array())

        state.configuration match {
          case Some(config) =>
            if (!config.coreIsAvailableForCountry(state.country)) {
              props.history.push(s"/${state.country}/soon")
            }
          case None => dispatch(LoadConfiguration)
        }

        MaybeTheme.MaybeThemeProps(
          maybeTheme = state.findTheme(slug = slug),
          maybeOperation = None,
          maybeLocation = None,
          queryTags = queryTags
        )

      }
    }
}
