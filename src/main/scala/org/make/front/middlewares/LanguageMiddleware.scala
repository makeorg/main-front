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

package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.client.MakeApiClient
import org.make.front.actions.SetLanguage
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.services.tracking.{GlobalTrackingParameter, TrackingService}

object LanguageMiddleware {
  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (appStore: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case SetLanguage(newLanguage) =>
        updateServicesLanguage(newLanguage)
        dispatch(SetLanguage(newLanguage))
      case action => dispatch(action)
  }

  def updateServicesLanguage(language: String): Unit = {
    I18n.setLocale(language)
    MakeApiClient.addHeaders(Map(MakeApiClient.languageHeader -> language))
    TrackingService.setGlobalTrackingParameter(GlobalTrackingParameter.language, language)
  }
}
