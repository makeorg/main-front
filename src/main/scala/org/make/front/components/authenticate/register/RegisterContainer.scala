package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.{ContainerComponentFactory, ReactRedux}
import org.make.front.actions.LoggedInAction
import org.make.front.components.authenticate.register.RegisterWithEmail.{RegisterProps, RegisterState}
import org.make.front.facades.Configuration
import org.make.front.models.{User => UserModel}
import org.make.services.user.UserServiceComponent

import scala.concurrent.Future

object RegisterContainer extends UserServiceComponent {

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

    (_, _) =>
      RegisterProps(onSubmit = register())
  }

  val registerWithEmailReactClass: ReactClass = selector(RegisterWithEmail.reactClass)
}
