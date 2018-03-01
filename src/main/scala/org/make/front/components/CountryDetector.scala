package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.SetCountry
import io.github.shogowada.scalajs.reactjs.router.RouterProps._

object CountryDetector {
  def apply(reactClass: ReactClass): ReactClass = {
    ReactRedux.connectAdvanced(selectorFactory)(reactClass)
  }

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Unit =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {

        val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase
        if (appState.country != countryCode) {
          dispatch(SetCountry(countryCode))
        }
      }
    }
}
