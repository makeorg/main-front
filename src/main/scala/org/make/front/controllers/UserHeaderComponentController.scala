package org.make.front.controllers

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.front.components.UserHeaderComponent
import org.make.front.models.AppState

object UserHeaderComponentController extends RouterProps {

  private lazy val reactClass = ReactRedux.connectAdvanced { _: Dispatch => (state: AppState, _: Props[Unit]) =>
    UserHeaderComponent.WrappedProps(
      isConnected = state.connectedUser.isDefined,
      userFirstName = state.connectedUser.map(_.firstname),
      avatarUrl = state.connectedUser.flatMap(_.avatarUrl)
    )
  }(UserHeaderComponent())

  def apply(): ReactClass = reactClass
}
