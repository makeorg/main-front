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
