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
            dispatch(SetCountryLanguage(country = newCountry, language = countryConfiguration.defaultLanguage))
            LanguageMiddleware.updateServicesLanguage(countryConfiguration.defaultLanguage)
          }
        }
      case action => dispatch(action)
  }

  private def updateServicesCountry(country: String): Unit = {
    MakeApiClient.addHeaders(Map(MakeApiClient.countryHeader -> country))
    TrackingService.setGlobalTrackingParameter(GlobalTrackingParameter.country, country)
  }

}
