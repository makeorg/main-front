package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.App.AppProps

object AppContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(App.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => AppProps =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        AppProps(language = appState.language, country = appState.country)
      }
    }
}
