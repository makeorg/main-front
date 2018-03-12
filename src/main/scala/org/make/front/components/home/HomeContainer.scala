package org.make.front.components.home

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadConfiguration

object HomeContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Home.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Home.HomeProps =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        appState.configuration match {
          case Some(config) =>
            if (!config.coreIsAvailableForCountry(appState.country)) {
              props.history.push(s"/${appState.country}/soon")
            }
          case None => dispatch(LoadConfiguration)
        }

        Home.HomeProps(appState.country)
      }
    }
}
