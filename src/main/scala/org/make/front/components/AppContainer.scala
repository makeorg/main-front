package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.App.AppProps

object AppContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(App.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => AppProps =
    (_: Dispatch) => { (appState: AppState, _: Props[Unit]) =>
      {
        val nVotesTriggerConnexionDefault: Int = 5
        AppProps(
          language = appState.language,
          country = appState.country,
          nVotesTriggerConnexion =
            appState.configuration.map(_.nVotesTriggerConnexion).getOrElse(nVotesTriggerConnexionDefault)
        )
      }
    }
}
