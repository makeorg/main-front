package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.front.actions.{DismissLoginRequired, LoggedInAction, NotifyError}
import org.make.front.components.presentationals.ConnectUserComponent
import org.make.front.components.presentationals.ConnectUserComponent.{ConnectUserProps, State}
import org.make.front.facades.{Configuration, I18n}
import org.make.front.facades.ReactFacebookLogin.FacebookAuthResponse
import org.make.front.facades.ReactGoogleLogin.GoogleAuthResponse
import org.make.front.models.{AppState, User}
import org.make.services.user.UserServiceComponent
import org.scalajs.dom.experimental.Response

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ConnectUserContainerComponent extends RouterProps with UserServiceComponent {

  //TODO: have things injected rather than using static stuff
  val googleAppId: String = Configuration.googleAppId
  val facebookAppId: String = Configuration.facebookAppId
  override val apiBaseUrl: String = Configuration.apiUrl

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ConnectUserComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ConnectUserComponent.ConnectUserProps =
    (dispatch: Dispatch) =>
      (appState: AppState, _: Props[Unit]) => {

        def closeModal() = {
          dispatch(DismissLoginRequired)
        }

        def signInGoogle(response: Response, child: Self[ConnectUserProps, State]): Unit = {
          handleFutureApiSignInResponse(
            userService.loginGoogle(response.asInstanceOf[GoogleAuthResponse].tokenId),
            child
          )
        }

        def signInFacebook(response: Response, child: Self[ConnectUserProps, State]): Unit = {
          handleFutureApiSignInResponse(
            userService.loginFacebook(response.asInstanceOf[FacebookAuthResponse].accessToken),
            child
          )
        }

        def signIn(username: String, password: String, child: Self[ConnectUserProps, State]): Unit = {
          handleFutureApiSignInResponse(userService.login(username, password), child)
        }

        def register(username: String,
                     password: String,
                     firstName: String,
                     child: Self[ConnectUserProps, State]): Unit = {
          handleFutureApiRegisterResponse(userService.registerUser(username, password, firstName).flatMap { _ =>
            userService.login(username, password)
          }, child)
        }

        // @toDo: manage specific errors (like username already exist)
        def handleFutureApiSignInResponse(futureUser: Future[User], child: Self[ConnectUserProps, State]): Unit = {
          futureUser.onComplete {
            case Success(user) =>
              dispatch(LoggedInAction(user))
            case Failure(_) =>
              dispatch(NotifyError(I18n.t("errors.tryAgain"), Some(I18n.t("errors.unexpectedBehaviour"))))
              child.setState(child.state.copy(errorMessage = I18n.t("form.login.errorAuthenticationFailed")))
          }
        }

        // @toDo: manage specific errors (like username already exist)
        // @toDo: manage make api authentication to register an access token
        def handleFutureApiRegisterResponse(futureUser: Future[User], child: Self[ConnectUserProps, State]): Unit = {
          futureUser.onComplete {
            case Success(user) =>
              dispatch(LoggedInAction(user))
            case Failure(_) =>
              dispatch(NotifyError(I18n.t("errors.tryAgain"), Some(I18n.t("errors.unexpectedBehaviour"))))
              child.setState(child.state.copy(errorMessage = I18n.t("form.register.errorRegistrationFailed")))
          }
        }

        ConnectUserComponent.ConnectUserProps(
          isConnected = appState.connectedUser.isDefined,
          signInGoogle = signInGoogle,
          signInFacebook = signInFacebook,
          signIn = signIn,
          register = register,
          closeModal = closeModal,
          isOpen = appState.technicalState.showLoginModal,
          googleAppId = googleAppId,
          facebookAppId = facebookAppId,
          isRegistering = true,
          isProposalFlow = appState.technicalState.useProposalLoginView
        )
    }
}
