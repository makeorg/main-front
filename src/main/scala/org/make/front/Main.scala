package org.make.front

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.components.AppComponent
import org.make.front.facades.I18n
import org.make.front.middlewares.NotificationMiddleware
import org.make.front.reducers.Reducer
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {

  val CssSettings = scalacss.devOrProdDefaults

  override def main(): Unit = {
    Translations.loadTranslations()
    I18n.setLocale("fr")
    val store = Redux.createStore(
      Reducer.reduce,
      ReduxDevTools.composeWithDevTools(Redux.applyMiddleware(NotificationMiddleware.handle))
    )

    ReactDOM.render(
      <.Provider(^.store := store)(<.BrowserRouter()(<(AppComponent()).empty)),
      dom.document.getElementById("make-app")
    )
  }
}
