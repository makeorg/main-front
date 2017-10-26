package org.make.front.components.userProfile

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LogoutAction
import org.make.front.components.AppState

object UserProfileContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(UserProfile.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => UserProfile.UserProfileProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      def logout: () => Unit = { () =>
        dispatch(LogoutAction)
        props.history.push("/")
      }

      if (state.connectedUser.isDefined) {
        UserProfile.UserProfileProps(user = state.connectedUser, logout = logout)
      } else {
        props.history.push("/")
        UserProfile.UserProfileProps(user = None, logout = () => {})
      }
    }
}
