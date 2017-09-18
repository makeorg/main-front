package org.make.front.components.userNav

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.front.actions.{LoginRequired, LogoutAction}
import org.make.front.components.AppState

object UserNavContainer extends RouterProps {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced {
    dispatch: Dispatch => (state: AppState, _: Props[Unit]) =>
      UserNav.WrappedProps(
        isConnected = state.connectedUser.isDefined,
        userFirstName = state.connectedUser.flatMap(_.firstName),
        avatarUrl = state.connectedUser.flatMap(_.profile).flatMap(_.avatarUrl),
        login = ()  => dispatch(LoginRequired()),
        logout = () => dispatch(LogoutAction)
      )
  }(UserNav.reactClass)
}
