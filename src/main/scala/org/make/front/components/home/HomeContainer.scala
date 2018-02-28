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

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Unit =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        state.configuration match {
          case Some(config) =>
            if (!config.coreIsAvailableForCountry(state.country)) {
              props.history.push(s"/${state.country}/soon")
            }
          case None => dispatch(LoadConfiguration)
        }
      }
    }
}
