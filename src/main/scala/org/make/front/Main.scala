package org.make.front

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.redux.{Redux, Store}
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.actions.{LoadConfiguration, ReloadUserAction}
import org.make.front.components.AppState
import org.make.front.components.Components.RichVirtualDOMElements
import org.make.front.facades.{I18n, NativeReactModal}
import org.make.front.middlewares.{
  ConfigurationMiddleware,
  ConnectedUserMiddleware,
  CookieAlertMiddleware,
  NotificationMiddleware,
  PoliticalActionMiddleware
}
import org.make.front.reducers.Reducer
import org.scalajs.dom

import scalacss.defaults.Exports
import scalacss.internal.mutable.Settings

object Main {

  val CssSettings: Exports with Settings = scalacss.devOrProdDefaults

  def main(args: Array[String]): Unit = {
    Translations.loadTranslations()
    I18n.setLocale("fr")

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

    val connectedUserMiddleware = new ConnectedUserMiddleware()

    val store: Store[AppState] = Redux.createStore(
      Reducer.reduce,
      ReduxDevTools.composeWithDevTools(
        Redux.applyMiddleware(
          ConfigurationMiddleware.handle,
          PoliticalActionMiddleware.handle,
          connectedUserMiddleware.handle,
          CookieAlertMiddleware.handle,
          NotificationMiddleware.handle
        )
      )
    )
    initStore(store)

    ReactDOM.render(
      <.Provider(^.store := store)(<.HashRouter()(<.AppComponent.empty)),
      dom.document.getElementById("make-app")
    )
  }

  private def initStore(store: Store[AppState]): Unit = {
    store.dispatch(LoadConfiguration)
    store.dispatch(ReloadUserAction)
  }

}
