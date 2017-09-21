package org.make.front.components.users.authenticate

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.components.users.authenticate.RequireAuthenticatedUser.RequireAuthenticatedUserProps

object RequireAuthenticatedUserContainer {

  case class RequireAuthenticatedUserContainerProps(defaultView: String = "login", onceConnected: () => Unit)

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _: Dispatch => (state: AppState, props: Props[RequireAuthenticatedUserContainerProps]) =>
      RequireAuthenticatedUserProps(
        defaultView = props.wrapped.defaultView,
        onceConnected = props.wrapped.onceConnected,
        isConnected = state.connectedUser.isDefined
      )

  }(RequireAuthenticatedUser.reactClass)
}
