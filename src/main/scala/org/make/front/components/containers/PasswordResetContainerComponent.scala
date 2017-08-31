package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.presentationals.PasswordResetComponent
import org.make.front.components.presentationals.PasswordResetComponent.PasswordResetProps
import org.make.front.facades.Configuration
import org.make.front.models.AppState
import org.make.services.user.UserServiceComponent

object PasswordResetContainerComponent extends UserServiceComponent {

  override val apiBaseUrl: String = Configuration.apiUrl

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(PasswordResetComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => PasswordResetProps =
    (dispatch: Dispatch) => {

      (state: AppState, _) =>
        PasswordResetComponent.PasswordResetProps()
    }
}
