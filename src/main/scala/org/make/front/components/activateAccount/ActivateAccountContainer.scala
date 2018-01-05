package org.make.front.components.activateAccount

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.{NotifyError, NotifySuccess}
import org.make.front.components.AppState
import org.make.front.components.activateAccount.ActivateAccount.ActivateAccountProps
import org.make.front.facades.I18n
import org.make.front.models.{OperationExpanded, OperationId}
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ActivateAccountContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ActivateAccount.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ActivateAccountProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      val userId = props.`match`.params("userId")
      val verificationToken = props.`match`.params("verificationToken")
      val operationId: Option[OperationId] = {
        val search = if (props.location.search.startsWith("?")) {
          props.location.search.substring(1)
        } else {
          props.location.search
        }
        search
          .split("&")
          .flatMap { parameter =>
            val operationParameterString = "operation="
            if (parameter.startsWith(operationParameterString) && parameter.length > operationParameterString.length) {
              Some(parameter.substring(operationParameterString.length))
            } else {
              None
            }
          }
      }.headOption.map(operationId => OperationId(operationId))

      def handleValidateAccount(child: Self[ActivateAccountProps, Unit]): Unit = {
        UserService.validateAccount(userId, verificationToken, operationId).onComplete {
          case Success(_) => {
            redirectAfterValidation(operationId, child)
            dispatch(NotifySuccess(message = I18n.t("activate-account.notifications.success")))
          }
          case Failure(e) => {
            redirectAfterValidation(operationId, child)
            dispatch(NotifyError(message = I18n.t("activate-account.notifications.failure")))
          }
        }
      }

      def redirectAfterValidation(maybeOperationId: Option[OperationId], child: Self[ActivateAccountProps, Unit]) = {
        val operationSlug: Option[String] = maybeOperationId.flatMap { operationId =>
          OperationExpanded.getOperationById(operationId.value, state).map(_.slug)
        }

        operationSlug match {
          case Some(slug) => child.props.history.push(s"/consultation/$slug")
          case _          => child.props.history.push("/")
        }
      }

      ActivateAccount.ActivateAccountProps(handleValidateAccount)
    }
}
