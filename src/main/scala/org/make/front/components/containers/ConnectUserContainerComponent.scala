package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.client.{BadRequestHttpException, UnauthorizedHttpException}
import org.make.front.actions._
import org.make.front.components.presentationals.ConnectUserComponent
import org.make.front.components.presentationals.ConnectUserComponent.{ConnectUserProps, ConnectUserState}
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

        def handleForgotPasswordLinkClick() = {
          // Close login modal
          dispatch(DismissLoginRequired)
          // Open PAssword recovery modal
          dispatch(OpenPasswordRecoveryModalAction)
        }

        def signInGoogle(response: Response, child: Self[ConnectUserProps, ConnectUserState]): Unit = {
          handleFutureApiSignInResponse(
            userService.loginGoogle(response.asInstanceOf[GoogleAuthResponse].tokenId),
            child
          )
        }

        def signInFacebook(response: Response, child: Self[ConnectUserProps, ConnectUserState]): Unit = {
          handleFutureApiSignInResponse(
            userService.loginFacebook(response.asInstanceOf[FacebookAuthResponse].accessToken),
            child
          )
        }

        def signIn(email: String, password: String, child: Self[ConnectUserProps, ConnectUserState]): Unit = {
          handleFutureApiSignInResponse(userService.login(email, password), child)
        }

        def register(child: Self[ConnectUserProps, ConnectUserState]): Unit = {
          handleFutureApiRegisterResponse(
            userService
              .registerUser(
                email = child.state.email,
                password = child.state.password,
                firstName = child.state.firstName,
                profession = child.state.profession,
                postalCode = child.state.postalCode,
                age = child.state.age.map(_.toInt)
              )
              .flatMap { _ =>
                userService.login(child.state.email, child.state.password)
              },
            child
          )
        }

        // @toDo: manage specific errors
        def handleFutureApiSignInResponse(futureUser: Future[User],
                                          child: Self[ConnectUserProps, ConnectUserState]): Unit = {
          futureUser.onComplete {
            case Success(user) =>
              dispatch(LoggedInAction(user))
              clearDataState(child)
            case Failure(e) =>
              e match {
                case UnauthorizedHttpException =>
                  child.setState(child.state.copy(errorMessage = Seq("form.login.errorAuthenticationFailed")))
                case _ => {
                  child.setState(child.state.copy(errorMessage = Seq("form.login.errorSignInFailed")))
                }
              }
          }
        }

        // @toDo: manage make api authentication to register an access token
        // @toDo: update error handling when API return a key code instead of text
        def handleFutureApiRegisterResponse(futureUser: Future[User],
                                            child: Self[ConnectUserProps, ConnectUserState]): Unit = {
          futureUser.onComplete {
            case Success(user) =>
              dispatch(LoggedInAction(user))
              clearDataState(child)
            case Failure(e) =>
              e match {
                case exception: BadRequestHttpException if exception.getMessage.contains("already exist") =>
                  child.setState(child.state.copy(errorMessage = Seq("form.register.errorAlreadyExist")))
                case _ => {
                  child.setState(child.state.copy(errorMessage = Seq("form.register.errorRegistrationFailed")))
                }
              }
          }
        }

        def clearDataState(child: Self[ConnectUserProps, ConnectUserState]): Unit = {
          child.setState(
            child.state
              .copy(email = "", password = "", firstName = "", age = None, postalCode = None, profession = None)
          )
        }

        ConnectUserComponent.ConnectUserProps(
          isConnected = appState.connectedUser.isDefined,
          signInGoogle = signInGoogle,
          signInFacebook = signInFacebook,
          signIn = signIn,
          register = register,
          closeModal = closeModal,
          handleForgotPasswordLinkClick = handleForgotPasswordLinkClick,
          isOpen = appState.technicalState.showLoginModal,
          googleAppId = googleAppId,
          facebookAppId = facebookAppId,
          isRegistering = true,
          isProposalFlow = appState.technicalState.useProposalLoginView
        )
    }
}
