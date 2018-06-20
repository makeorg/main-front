package org.make.front.components.users.authenticate

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.authenticate.LoginOrRegister.LoginOrRegisterProps
import org.make.front.components.Components._
import org.make.front.components.modals.Modal.ModalProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.middlewares.TriggerSignUpMiddleware
import org.make.front.middlewares.TriggerSignUpMiddleware.TriggerSignUpListener
import org.make.front.models.{Location, OperationId}
import org.make.front.models.Location.OperationPage
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext

object TriggerSignUp {

  final case class TriggerSignUpProps(nVotesTriggerConnexion: Int)
  final case class TriggerSignUpState(id: String,
                                      voteCount: Int,
                                      isAuthenticateModalOpened: Boolean,
                                      voteLocation: Option[Location])

  lazy val reactClass: ReactClass =
    React.createClass[TriggerSignUpProps, TriggerSignUpState](
      displayName = "TriggerSignUp",
      getInitialState = { _ =>
        TriggerSignUpState(
          id = UUID.randomUUID().toString,
          voteCount = 0,
          isAuthenticateModalOpened = false,
          voteLocation = None
        )
      },
      componentDidMount = { self =>
        val onTriggerSignUp: Location => Unit = { location =>
          self.setState(state => state.copy(voteCount = state.voteCount + 1))
          if (self.state.voteCount != 0 && self.state.voteCount % self.props.wrapped.nVotesTriggerConnexion == 0)
            self.setState(_.copy(isAuthenticateModalOpened = true, voteLocation = Some(location)))
        }
        TriggerSignUpMiddleware
          .addTriggerSignUpListener(self.state.id, TriggerSignUpListener(onTriggerSignUp))
      },
      componentWillUnmount = { self =>
        TriggerSignUpMiddleware.removeTriggerSignUpListener(self.state.id)
      },
      render = { self =>
        val maybeOperationId = self.state.voteLocation.flatMap {
          case OperationPage(operationId) => Some(operationId)
          case _                          => None
        }
        <.ModalComponent(
          ^.wrapped := ModalProps(isModalOpened = self.state.isAuthenticateModalOpened, closeCallback = () => {
            self.setState(_.copy(isAuthenticateModalOpened = false, voteLocation = None))
          })
        )(
          <.LoginOrRegisterComponent(
            ^.wrapped := LoginOrRegisterProps(
              operationId = maybeOperationId,
              trackingContext = TrackingContext(TrackingLocation.triggerFromVote),
              trackingParameters = Map.empty,
              displayView = "register",
              onSuccessfulLogin = () => {
                self.setState(_.copy(isAuthenticateModalOpened = false, voteLocation = None))
              },
              registerTitle = Some(unescape(I18n.t("authenticate.register.with-email-intro-trigger")))
            )
          )()
        )
      }
    )
}
