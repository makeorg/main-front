package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.{NotifyError, NotifySuccess}
import org.make.front.components.presentationals.ActivateAccountComponent
import org.make.front.components.presentationals.ActivateAccountComponent.ActivateAccountProps
import org.make.front.facades.{Configuration, I18n}
import org.make.front.models.AppState
import org.make.services.user.UserServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ActivateAccountContainerComponent extends UserServiceComponent {

  override val apiBaseUrl: String = Configuration.apiUrl

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ActivateAccountComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ActivateAccountProps =
    (dispatch: Dispatch) => {

      (state: AppState, props: Props[Unit]) =>
        val userId = props.`match`.params("userId")
        val verificationToken = props.`match`.params("verificationToken")

        def handleValidateAccount(child: Self[ActivateAccountProps, Unit]): Unit = {
          userService.validateAccount(userId, verificationToken).onComplete {
            case Success(_) => {
              dispatch(NotifySuccess(
                message = I18n.t("content.account.validationSuccess"),
                title = Some(I18n.t("content.account.validationTitle"))
              ))
              child.props.history.push("/")
            }
            case Failure(e) => {
              dispatch(NotifyError(
                message = I18n.t("content.account.validationError"),
                title = Some(I18n.t("content.account.validationTitle"))
              ))
              child.props.history.push("/")
            }
          }
        }

        ActivateAccountComponent.ActivateAccountProps(handleValidateAccount)
    }

}
