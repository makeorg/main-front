package org.make.front.components.authenticate.login

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.actions.LoggedInAction
import org.make.front.components.AppState
import org.make.front.components.authenticate.login.LoginWithEmail.LoginWithEmailProps
import org.make.front.facades.FacebookPixel
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object LoginWithEmailContainer {

  case class LoginWithEmailContainerProps(note: String, onSuccessfulLogin: () => Unit = () => {})

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    dispatch => (_: AppState, props: Props[LoginWithEmailContainerProps]) =>
      def signIn(email: String, password: String): Future[_] = {
        val result = UserService.login(email, password)
        result.onComplete {
          case Success(user) =>
            FacebookPixel.fbq("trackCustom", "signin-email-success")
            dispatch(LoggedInAction(user))
            props.wrapped.onSuccessfulLogin()
          case Failure(_) =>
        }
        result
      }
      LoginWithEmailProps(note = props.wrapped.note, connectUser = signIn)
  }(LoginWithEmail.reactClass)

}
