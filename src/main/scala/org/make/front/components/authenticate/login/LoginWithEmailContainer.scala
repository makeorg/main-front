package org.make.front.components.authenticate.login

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.actions.LoggedInAction
import org.make.front.components.AppState
import org.make.front.components.authenticate.login.LoginWithEmail.LoginWithEmailProps

import scala.util.{Failure, Success}
import org.make.front.components.connectUser.ConnectUserContainer.userService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object LoginWithEmailContainer {

  case class LoginWithEmailContainerProps(intro: String, onSuccessfulLogin: () => Unit = () => {})

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    dispatch => (_: AppState, props: Props[LoginWithEmailContainerProps]) =>
      def signIn(email: String, password: String): Future[_] = {
        val result = userService.login(email, password)
        result.onComplete {
          case Success(user) =>
            dispatch(LoggedInAction(user))
            props.wrapped.onSuccessfulLogin()
          case Failure(_) =>
        }
        result
      }
      LoginWithEmailProps(intro = props.wrapped.intro, connectUser = signIn)
  }(LoginWithEmail.reactClass)

}
