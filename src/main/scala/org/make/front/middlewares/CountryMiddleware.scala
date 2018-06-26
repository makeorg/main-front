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
import org.make.front.actions._
import org.make.front.components.AppState
import org.make.front.models.CountryConfiguration
import org.make.services.tracking.{GlobalTrackingParameter, TrackingService}

object CountryMiddleware {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (appStore: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case SetCountry(newCountry) =>
        updateServicesCountry(newCountry)

        val countryConfiguration: Option[CountryConfiguration] = appStore.getState.configuration.flatMap {
          _.supportedCountries.find(_.countryCode == newCountry)
        }

        countryConfiguration.map { countryConfiguration =>
          if (countryConfiguration.supportedLanguages.contains(appStore.getState.language)) {
            dispatch(SetCountry(country = newCountry))
          } else {
            LanguageMiddleware.updateServicesLanguage(countryConfiguration.defaultLanguage)
            dispatch(SetCountryLanguage(country = newCountry, language = countryConfiguration.defaultLanguage))
          }
        }
      case action => dispatch(action)
  }

  def updateServicesCountry(country: String): Unit = {
    MakeApiClient.addHeaders(Map(MakeApiClient.countryHeader -> country))
    TrackingService.setGlobalTrackingParameter(GlobalTrackingParameter.country, country)
  }

}
