package org.make.front.components.containers


import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.components.presentationals.PasswordResetComponent
import org.make.front.components.presentationals.PasswordResetComponent.{PasswordResetProps, PasswordResetState}
import org.make.front.facades.{Configuration, I18n}
import org.make.front.models.AppState
import org.make.services.user.UserServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object PasswordResetContainerComponent extends UserServiceComponent {

  override val apiBaseUrl: String = Configuration.apiUrl

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(PasswordResetComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => PasswordResetProps =
    (_: Dispatch) => {

      (state: AppState, props: Props[Unit]) =>
        val userId = props.`match`.params("userId")

        val resetToken = props.`match`.params("resetToken")

        def checkResetToken(child: Self[PasswordResetProps, PasswordResetState]): Unit = {
          userService.resetPasswordCheck(userId, resetToken).onComplete {
            case Success(isValidResetToken) => child.setState(child.state.copy(isValidResetToken = isValidResetToken))
            case Failure(e) => throw e
          }
        }

        def handleSubmit(self: Self[PasswordResetProps, PasswordResetState]): Unit = {
          userService.resetPasswordChange(userId, resetToken, self.state.password).onComplete {
            case Success(_) =>
              self.setState(self.state.copy(password = "", showPassword = false, errorMessage = "", isValidResetToken = false))
            case Failure(e) =>
              println(e.getMessage)
              self.setState(self.state.copy(errorMessage = I18n.t("form.passwordReset.changeFailed")))
          }
        }

        PasswordResetComponent.PasswordResetProps(handleSubmit, checkResetToken)
    }

}
