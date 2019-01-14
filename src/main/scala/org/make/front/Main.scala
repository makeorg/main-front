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

package org.make.front

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.redux.{Redux, Store}
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.client.MakeApiClient
import org.make.front.actions._
import org.make.front.components.AppState
import org.make.front.components.Components.RichVirtualDOMElements
import org.make.front.facades.{I18n, NativeReactModal}
import org.make.front.helpers.{DetectedCountry, WeEuropeansRedirect}
import org.make.front.middlewares._
import org.make.front.models.{BusinessConfiguration, Operation, OperationList, User}
import org.make.front.reducers.Reducer
import org.make.services.ConfigurationService
import org.make.services.operation.OperationService
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.user.UserService
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.defaults.Exports
import scalacss.internal.mutable.Settings

object Main {

  val CssSettings: Exports with Settings = scalacss.devOrProdDefaults

  def main(args: Array[String]): Unit = {

    WeEuropeansRedirect.weEuropeanRedirect(DetectedCountry.getDetectedCountry)

    Translations.loadTranslations()
    I18n.setLocale("fr", true)

    NativeReactModal.defaultStyles.overlay.update("zIndex", "9")
    NativeReactModal.defaultStyles.overlay.update("backgroundColor", "rgba(0, 0, 0, 0.7)")
    NativeReactModal.defaultStyles.content.update("position", "fixed")
    NativeReactModal.defaultStyles.content.update("top", "0")
    NativeReactModal.defaultStyles.content.update("right", "0")
    NativeReactModal.defaultStyles.content.update("bottom", "0")
    NativeReactModal.defaultStyles.content.update("left", "0")
    NativeReactModal.defaultStyles.content.update("width", "100%")
    NativeReactModal.defaultStyles.content.update("height", "100%")
    NativeReactModal.defaultStyles.content.update("padding", "0")
    NativeReactModal.defaultStyles.content.update("border", "none")
    NativeReactModal.defaultStyles.content.update("borderRadius", "0")
    NativeReactModal.defaultStyles.content.update("background", "transparent")
    NativeReactModal.defaultStyles.content.update("overflow", "auto")

    TrackingService.track(
      eventName = "load-application",
      trackingContext = TrackingContext(TrackingLocation.homepage),
      parameters = Map("href" -> dom.window.location.href)
    )

    val configurationFuture = ConfigurationService.fetchConfiguration()
    val connectedUserFuture =
      UserService.getCurrentUser().map(Some(_)).recoverWith { case _ => Future.successful(None) }
    val operationsFuture = OperationService.listOperations()

    val configurationAndUser = for {
      configuration <- configurationFuture
      maybeUser     <- connectedUserFuture
      operations    <- operationsFuture
    } yield (configuration, maybeUser, operations)

    configurationAndUser.onComplete {
      case Failure(_) => main(args)
      case Success((configuration, maybeUser, operations)) =>
        initApplication(configuration, maybeUser, operations)
    }
  }

  private def initApplication(configuration: BusinessConfiguration,
                              maybeUser: Option[User],
                              operations: OperationList) = {
    val connectedUserMiddleware = new ConnectedUserMiddleware()

    val store: Store[AppState] = Redux.createStore(
      Reducer.reduce,
      ReduxDevTools.composeWithDevTools(
        Redux.applyMiddleware(
          ConfigurationMiddleware.handle,
          OperationsMiddleware.handle,
          PoliticalActionMiddleware.handle,
          connectedUserMiddleware.handle,
          CookieAlertMiddleware.handle,
          NotificationMiddleware.handle,
          TriggerSignUpMiddleware.handle,
          LanguageMiddleware.handle,
          CountryMiddleware.handle
        )
      )
    )

    store.dispatch(SetConfiguration(configuration))
    store.dispatch(SetOperations(operations))
    maybeUser.foreach { user =>
      store.dispatch(LoggedInAction(user))
    }

    // init services depending language and country
    LanguageMiddleware.updateServicesLanguage(store.getState.language)
    CountryMiddleware.updateServicesCountry(store.getState.country)
    store.dispatch(SetCountryLanguage(country = store.getState.country, language = store.getState.language))

    // adding get parameters headers
    MakeApiClient.addHeaders(Map("x-get-parameters" -> dom.window.location.search.drop(1)).filter {
      case (_, value) => value.nonEmpty
    })

    // adding hostname header
    MakeApiClient.addHeaders(Map("x-hostname" -> dom.window.location.hostname))

    startAppWhenReady(maybeUser, store)
  }
  private def startAppWhenReady(maybeUser: Option[User], store: Store[AppState]): Unit = {
    if (store.getState.configuration.isEmpty || store.getState.connectedUser.isDefined != maybeUser.isDefined) {
      org.scalajs.dom.window.setTimeout(() => startAppWhenReady(maybeUser, store), 20d)
    } else {

      ReactDOM.render(
        <.Provider(^.store := store)(<.HashRouter()(<.AppContainerComponent.empty)),
        dom.document.getElementById("make-app")
      )
    }
  }
}
