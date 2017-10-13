package org.make.front.components.users.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.components.authenticate.LoginOrRegister.LoginOrRegisterProps

object RequireAuthenticatedUser {

  case class RequireAuthenticatedUserProps(intro: ReactElement,
                                           registerView: String,
                                           defaultView: String = "register",
                                           onceConnected: () => Unit,
                                           isConnected: Boolean)
  type RequireAuthenticatedUserState = Unit

  val reactClass: ReactClass =
    React.createClass[RequireAuthenticatedUserProps, RequireAuthenticatedUserState](
      displayName = "RequireAuthenticatedUser",
      componentWillMount = { self =>
        if (self.props.wrapped.isConnected) {
          self.props.wrapped.onceConnected()
        }
      },
      componentWillUpdate = { (_, props, _) =>
        if (props.wrapped.isConnected) {
          props.wrapped.onceConnected()
        }
      },
      render = { self =>
        val props = self.props.wrapped

        if (props.isConnected) {
          <.div()(<.SpinnerComponent.empty)
        } else {
          <.div()(
            self.props.wrapped.intro,
            <.LoginOrRegisterComponent(
              // There is no need to use callback here, since the component will be reloaded with different props
              // once the user is connected. if we map it here, the callback will be called twice
              ^.wrapped := LoginOrRegisterProps(
                registerView = props.registerView,
                displayView = props.defaultView,
                onSuccessfulLogin = () => {}
              )
            )()
          )
        }
      }
    )
}
