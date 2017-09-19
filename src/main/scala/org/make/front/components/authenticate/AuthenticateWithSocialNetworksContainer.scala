package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.client.UnauthorizedHttpException
import org.make.front.actions.LoggedInAction
import org.make.front.components.AppState
import org.make.front.components.authenticate.AuthenticateWithSocialNetworks.{
  AuthenticateWithSocialNetworksProps,
  AuthenticateWithSocialNetworksState
}
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

  case class AuthenticateWithSocialNetworksContainerProps(intro: String, note: String)

  val reactClass: ReactClass = ReactRedux
    .connectAdvanced[AppState, AuthenticateWithSocialNetworksContainerProps, AuthenticateWithSocialNetworksProps] {
      dispatch =>
        def signInGoogle(
          response: Response,
          child: Self[AuthenticateWithSocialNetworksProps, AuthenticateWithSocialNetworksState]
        ): Unit = {
          org.scalajs.dom.console.log("Just before doing the call...")
          handleFutureApiSignInResponse(
            userService.loginGoogle(response.asInstanceOf[GoogleAuthResponse].tokenId),
            child
          )
        }

        def signInFacebook(
          response: Response,
          child: Self[AuthenticateWithSocialNetworksProps, AuthenticateWithSocialNetworksState]
        ): Unit = {
          handleFutureApiSignInResponse(
            userService.loginFacebook(response.asInstanceOf[FacebookAuthResponse].accessToken),
            child
          )
        }

        // @toDo: manage specific errors
        def handleFutureApiSignInResponse(
          futureUser: Future[UserModel],
          child: Self[AuthenticateWithSocialNetworksProps, AuthenticateWithSocialNetworksState]
        ): Unit = {
          futureUser.onComplete {
            case Success(user) =>
              org.scalajs.dom.console.log("Successfully retrieves user")
              dispatch(LoggedInAction(user))
              clearDataState(child)
            case Failure(e) =>
              org.scalajs.dom.console.log("error while retrieving user", e.getMessage)
              e match {
                case UnauthorizedHttpException =>
                  child.setState(child.state.copy(errorMessages = Seq("form.login.errorAuthenticationFailed")))
                case _ =>
                  child.setState(child.state.copy(errorMessages = Seq("form.login.errorSignInFailed")))
              }
          }
        }

        def clearDataState(
          child: Self[AuthenticateWithSocialNetworksProps, AuthenticateWithSocialNetworksState]
        ): Unit = {
          child.setState(_.copy(errorMessages = Seq.empty))
        }

        (state, props) =>
          AuthenticateWithSocialNetworksProps(
            intro = props.wrapped.intro,
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
