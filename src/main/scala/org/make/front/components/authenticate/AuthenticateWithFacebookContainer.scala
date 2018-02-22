package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.actions.LoggedInAction
import org.make.front.components.AppState
import org.make.front.components.authenticate.AuthenticateWithFacebookButton.AuthenticateWithFacebookButtonProps
import org.make.front.facades.Configuration
import org.make.front.facades.ReactFacebookLogin.FacebookAuthResponse
import org.make.front.models.{OperationId, User => UserModel}
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.user.UserService
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object AuthenticateWithFacebookContainer {

  case class AuthenticateWithFacebookContainerProps(trackingContext: TrackingContext,
                                                    trackingParameters: Map[String, String],
                                                    onSuccessfulLogin: () => Unit = () => {},
                                                    isLookingLikeALink: Boolean = false,
                                                    operationId: Option[OperationId])

  val reactClass: ReactClass = ReactRedux
    .connectAdvanced[AppState, AuthenticateWithFacebookContainerProps, AuthenticateWithFacebookButtonProps] {
      dispatch => (state, props) =>
        def signIn(response: Response): Future[UserModel] = {
          handleFutureApiSignInResponse(
            UserService.loginFacebook(
              token = response.asInstanceOf[FacebookAuthResponse].accessToken,
              operationId = props.wrapped.operationId,
              country = state.country,
              language = state.language
            )
          )
        }

        def handleFutureApiSignInResponse(futureUser: Future[UserModel]): Future[UserModel] = {
          futureUser.onComplete {
            case Success(user) =>
              dispatch(LoggedInAction(user))
              props.wrapped.onSuccessfulLogin()
            case Failure(e) => throw e
          }
          futureUser
        }

        AuthenticateWithFacebookButtonProps(
          trackingContext = props.wrapped.trackingContext,
          trackingParameters = props.wrapped.trackingParameters,
          isConnected = state.connectedUser.isDefined,
          facebookAppId = Configuration.facebookAppId,
          errorMessages = Seq.empty,
          signIn = signIn,
          isLookingLikeALink = props.wrapped.isLookingLikeALink
        )
    }(AuthenticateWithFacebookButton.reactClass)
}
