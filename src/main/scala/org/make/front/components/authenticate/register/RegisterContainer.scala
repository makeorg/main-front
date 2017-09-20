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

object RegisterContainer extends UserServiceComponent {

  case class RegisterUserProps(intro: String, note: String)

  override def apiBaseUrl: String = Configuration.apiUrl

  def selector: ContainerComponentFactory[RegisterProps] = ReactRedux.connectAdvanced { dispatch =>
    def register(): (RegisterState) => Future[UserModel] = { state =>
      userService
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
        .map { user =>
          dispatch(LoggedInAction(user))
          user
        }
    }

    (_: AppState, props: Props[RegisterUserProps]) =>
      RegisterProps(props.wrapped.intro, props.wrapped.note, register = register())
  }

  val registerWithEmailReactClass: ReactClass = selector(RegisterWithEmail.reactClass)
  val registerWithEmailExpandedReactClass: ReactClass = selector(RegisterWithEmailExpanded.reactClass)
}
