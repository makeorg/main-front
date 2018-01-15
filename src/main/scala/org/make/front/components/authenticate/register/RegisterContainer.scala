package org.make.front.components.authenticate.register

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.{ContainerComponentFactory, ReactRedux}
import org.make.front.actions.{LoggedInAction, NotifyInfo}
import org.make.front.components.AppState
import org.make.front.facades.{FacebookPixel, I18n}
import org.make.front.models.{OperationId, User => UserModel}
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object RegisterContainer {

  case class RegisterUserProps(note: String,
                               onSuccessfulRegistration: () => Unit = () => {},
                               operationId: Option[OperationId])

  def selector: ContainerComponentFactory[RegisterProps] = ReactRedux.connectAdvanced {
    dispatch => (_: AppState, props: Props[RegisterUserProps]) =>
      def register(): (RegisterState) => Future[UserModel] = { state =>
        val future = UserService
          .registerUser(
            email = state.fields("email"),
            password = state.fields("password"),
            firstName = state.fields("firstName"),
            profession = state.fields.get("profession"),
            postalCode = state.fields.get("postalCode"),
            age = state.fields.get("age").map(_.toInt),
            operationId = props.wrapped.operationId
          )
          .flatMap { _ =>
            UserService.login(state.fields("email"), state.fields("password"))
          }

        future.onComplete {
          case Success(user) =>
            FacebookPixel.fbq("trackCustom", "signup-email-success")
            dispatch(LoggedInAction(user))
            dispatch(NotifyInfo(message = I18n.t("authenticate.register.notifications.success")))
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
