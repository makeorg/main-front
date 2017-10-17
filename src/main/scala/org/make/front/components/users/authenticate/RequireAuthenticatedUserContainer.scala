package org.make.front.components.users.authenticate

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.components.users.authenticate.RequireAuthenticatedUser.RequireAuthenticatedUserProps
import org.make.front.models.OperationId

object RequireAuthenticatedUserContainer {

  case class RequireAuthenticatedUserContainerProps(operation: Option[OperationId],
                                                    intro: ReactElement,
                                                    registerView: String,
                                                    defaultView: String = "register",
                                                    onceConnected: () => Unit)

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _: Dispatch => (state: AppState, props: Props[RequireAuthenticatedUserContainerProps]) =>
      RequireAuthenticatedUserProps(
        operation = props.wrapped.operation,
        intro = props.wrapped.intro,
        registerView = props.wrapped.registerView,
        defaultView = props.wrapped.defaultView,
        onceConnected = props.wrapped.onceConnected,
        isConnected = state.connectedUser.isDefined
      )

  }(RequireAuthenticatedUser.reactClass)
}
