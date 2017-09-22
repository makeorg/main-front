package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.actions.LoggedInAction
import org.make.front.components.AppState
import org.make.front.components.authenticate.AuthenticateWithSocialNetworks.AuthenticateWithSocialNetworksProps
import org.make.front.facades.Configuration
import org.make.front.facades.ReactFacebookLogin.FacebookAuthResponse
import org.make.front.facades.ReactGoogleLogin.GoogleAuthResponse
import org.make.front.models.{User => UserModel}
import org.make.services.user.UserServiceComponent
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object AuthenticateWithSocialNetworksContainer extends UserServiceComponent {

  override def apiBaseUrl: String = Configuration.apiUrl

  case class AuthenticateWithSocialNetworksContainerProps(note: String, onSuccessfulLogin: () => Unit = () => {})

  val reactClass: ReactClass = ReactRedux
    .connectAdvanced[AppState, AuthenticateWithSocialNetworksContainerProps, AuthenticateWithSocialNetworksProps] {
      dispatch => (state, props) =>
        def signInGoogle(response: Response): Future[UserModel] = {
          handleFutureApiSignInResponse(userService.loginGoogle(response.asInstanceOf[GoogleAuthResponse].tokenId))
        }

        def signInFacebook(response: Response): Future[UserModel] = {
          handleFutureApiSignInResponse(
            userService.loginFacebook(response.asInstanceOf[FacebookAuthResponse].accessToken)
          )
        }

        def handleFutureApiSignInResponse(futureUser: Future[UserModel]): Future[UserModel] = {
          futureUser.onComplete {
            case Success(user) =>
              dispatch(LoggedInAction(user))
              props.wrapped.onSuccessfulLogin()
            case Failure(_) =>
          }
          futureUser
        }

        AuthenticateWithSocialNetworksProps(
          note = props.wrapped.note,
          isConnected = state.connectedUser.isDefined,
          googleAppId = Configuration.googleAppId,
          facebookAppId = Configuration.facebookAppId,
          errorMessages = Seq.empty,
          signInFacebook = signInFacebook,
          signInGoogle = signInGoogle
        )
    }(AuthenticateWithSocialNetworks.reactClass)
}
