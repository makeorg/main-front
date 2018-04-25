package org.make.front.components.users.authenticate

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.authenticate.LoginOrRegister.LoginOrRegisterProps
import org.make.front.components.Components._
import org.make.front.components.modals.Modal.ModalProps
import org.make.front.middlewares.TriggerSignUpMiddleware
import org.make.front.middlewares.TriggerSignUpMiddleware.TriggerSignUpListener
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext

object TriggerSignUp {

  final case class TriggerSignUpProps(nVotesTriggerConnexion: Int)
  final case class TriggerSignUpState(id: String, voteCount: Int, isAuthenticateModalOpened: Boolean)

  lazy val reactClass: ReactClass =
    React.createClass[TriggerSignUpProps, TriggerSignUpState](
      displayName = "TriggerSignUp",
      getInitialState = { _ =>
        TriggerSignUpState(id = UUID.randomUUID().toString, voteCount = 0, isAuthenticateModalOpened = false)
      },
      componentDidMount = { self =>
        val onTriggerSignUp: () => Unit = () => {
          self.setState(state => state.copy(voteCount = state.voteCount + 1))
          if (self.state.voteCount != 0 && self.state.voteCount % self.props.wrapped.nVotesTriggerConnexion == 0)
            self.setState(_.copy(isAuthenticateModalOpened = true))
        }
        TriggerSignUpMiddleware
          .addTriggerSignUpListener(self.state.id, TriggerSignUpListener(onTriggerSignUp))
      },
      componentWillUnmount = { self =>
        TriggerSignUpMiddleware.removeTriggerSignUpListener(self.state.id)
      },
      render = { self =>
        <.ModalComponent(
          ^.wrapped := ModalProps(isModalOpened = self.state.isAuthenticateModalOpened, closeCallback = () => {
            self.setState(_.copy(isAuthenticateModalOpened = false))
          })
        )(
          <.LoginOrRegisterComponent(
            ^.wrapped := LoginOrRegisterProps(
              trackingContext = TrackingContext(TrackingLocation.triggerFromVote),
              trackingParameters = Map.empty,
              displayView = "register",
              onSuccessfulLogin = () => {
                self.setState(_.copy(isAuthenticateModalOpened = false))
              }
            )
          )()
        )
      }
    )
}
