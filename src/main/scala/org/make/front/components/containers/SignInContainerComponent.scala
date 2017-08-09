package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.front.actions.{DismissLoginRequired, LoggedInAction, NotifyError}
import org.make.front.components.presentationals.SignInComponent
import org.make.front.components.presentationals.SignInComponent.{SignInProps, SignInState}
import org.make.front.facades.I18n
import org.make.front.facades.ReactFacebookLogin.FacebookAuthResponse
import org.make.front.facades.ReactGoogleLogin.GoogleAuthResponse
import org.make.front.models.{AppState, User}
import org.make.services.user.UserServiceComponent
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object SignInContainerComponent extends RouterProps with UserServiceComponent {

  //TODO: load these from config
  val googleAppId = "810331964280-qtdupbrjusihad3b5da51i5p66qpmhmr.apps.googleusercontent.com"
  val facebookAppId = "317128238675603"

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SignInComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => SignInComponent.SignInProps =
    (dispatch: Dispatch) =>
      (appState: AppState, _: Props[Unit]) => {

        def closeModal() = {
          dispatch(DismissLoginRequired)
        }

        def signInGoogle(response: Response, child: Self[SignInProps, SignInState]): Unit = {
          handleFutureApiResponse(userService.loginGoogle(response.asInstanceOf[GoogleAuthResponse].tokenId), child)
        }

        def signInFacebook(response: Response, child: Self[SignInProps, SignInState]): Unit = {
          handleFutureApiResponse(
            userService.loginFacebook(response.asInstanceOf[FacebookAuthResponse].accessToken),
            child
          )
        }

        def signIn(username: String, password: String, child: Self[SignInProps, SignInState]): Unit = {
          handleFutureApiResponse(userService.login(username, password), child)
        }

        def handleFutureApiResponse(futureUser: Future[User], child: Self[SignInProps, SignInState]): Unit = {
          futureUser.onComplete {
            case Success(user) =>
              dispatch(LoggedInAction(user))
            case Failure(e) =>
              dispatch(NotifyError(I18n.t("errors.tryAgain"), Some(I18n.t("errors.unexpectedBehaviour"))))
              child.setState(child.state.copy(errorMessage = I18n.t("form.login.errorAuthenticationFailed")))
          }
        }

        SignInComponent.SignInProps(
          isConnected = appState.connectedUser.isDefined,
          signInGoogle = signInGoogle,
          signInFacebook = signInFacebook,
          signIn = signIn,
          closeModal = closeModal,
          isOpen = appState.technicalState.showLoginModal,
          googleAppId = googleAppId,
          facebookAppId = facebookAppId
        )
    }
}
