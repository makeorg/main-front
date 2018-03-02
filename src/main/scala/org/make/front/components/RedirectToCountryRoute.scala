package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
object RedirectToCountryRoute {

  case class RedirectToCountryRouteProps(country: String)

  def apply(): ReactClass = {
    ReactRedux.connectAdvanced(selectorFactory)(
      React
        .createClass[RedirectToCountryRouteProps, Unit](componentWillMount = { self =>
          val location: String = self.props.location.pathname
          self.props.history.push(s"/${self.props.wrapped.country}$location${self.props.location.search}")
        }, render = (_) => {
          <.div()()
        })
    )
  }

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => RedirectToCountryRouteProps =
    (_: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        RedirectToCountryRouteProps(appState.country)
      }
    }
}
