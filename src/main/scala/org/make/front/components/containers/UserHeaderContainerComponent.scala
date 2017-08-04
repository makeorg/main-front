package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.front.components.presentationals.UserHeaderComponent
import org.make.front.models.AppState

object UserHeaderContainerComponent extends RouterProps {

  lazy val reactClass = ReactRedux.connectAdvanced { _: Dispatch => (state: AppState, _: Props[Unit]) =>
    UserHeaderComponent.WrappedProps(
      isConnected = state.connectedUser.isDefined,
      userFirstName = state.connectedUser.map(_.firstname),
      avatarUrl = state.connectedUser.flatMap(_.avatarUrl)
    )
  }(UserHeaderComponent.reactClass)
}
