package org.make.front

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.{Redux, Store}
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.actions.LoadThemes
import org.make.front.components.AppComponent
import org.make.front.facades.I18n
import org.make.front.middlewares.{NotificationMiddleware, ThemeMiddleware}
import org.make.front.models.AppState
import org.make.front.reducers.Reducer
import org.scalajs.dom

import scala.scalajs.js.JSApp
import scalacss.defaults.Exports
import scalacss.internal.mutable.Settings

object Main extends JSApp {

  val CssSettings: Exports with Settings = scalacss.devOrProdDefaults

  override def main(): Unit = {
    Translations.loadTranslations()
    I18n.setLocale("fr")
    val store: Store[AppState] = Redux.createStore(
      Reducer.reduce,
      ReduxDevTools.composeWithDevTools(Redux.applyMiddleware(NotificationMiddleware.handle, ThemeMiddleware.handle))
    )
    initStore(store)

    ReactDOM.render(
      <.Provider(^.store := store)(<.HashRouter()(<(AppComponent()).empty)),
      dom.document.getElementById("make-app")
    )
  }

  private def initStore(store: Store[AppState]): Unit = {
    store.dispatch(LoadThemes)
  }
}
