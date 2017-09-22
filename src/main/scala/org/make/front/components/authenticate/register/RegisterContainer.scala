package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.{ContainerComponentFactory, ReactRedux}
import org.make.front.actions.LoggedInAction
import org.make.front.components.AppState
import org.make.front.facades.Configuration
import org.make.front.models.{User => UserModel}
import org.make.services.user.UserServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object RegisterContainer extends UserServiceComponent {

  case class RegisterUserProps(note: String, onSuccessfulRegistration: () => Unit = () => {})

  override def apiBaseUrl: String = Configuration.apiUrl

  def selector: ContainerComponentFactory[RegisterProps] = ReactRedux.connectAdvanced {
    dispatch => (_: AppState, props: Props[RegisterUserProps]) =>
      def register(): (RegisterState) => Future[UserModel] = { state =>
        val future = userService
          .registerUser(
            email = state.fields("email"),
            password = state.fields("password"),
            firstName = state.fields("firstName"),
            profession = state.fields.get("profession"),
            postalCode = state.fields.get("postalCode"),
            age = state.fields.get("age").map(_.toInt)
          )
          .flatMap { _ =>
            userService.login(state.fields("email"), state.fields("password"))
          }

        future.onComplete {
          case Success(user) =>
            dispatch(LoggedInAction(user))
            props.wrapped.onSuccessfulRegistration()
          case Failure(_) =>
        }

        future
      }
      RegisterProps(props.wrapped.note, register = register())
  }

  val registerWithEmailReactClass: ReactClass = selector(RegisterWithEmail.reactClass)
  val registerWithEmailExpandedReactClass: ReactClass = selector(RegisterWithEmailExpanded.reactClass)
}
