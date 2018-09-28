/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.activateAccount

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.{NotifyError, NotifySuccess}
import org.make.front.components.AppState
import org.make.front.components.activateAccount.ActivateAccount.ActivateAccountProps
import org.make.front.facades.I18n
import org.make.front.models.{OperationId, OperationStaticData}
import org.make.services.operation.OperationService
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ActivateAccountContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ActivateAccount.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ActivateAccountProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      val userId = props.`match`.params("userId")
      val verificationToken = props.`match`.params("verificationToken")

      val search: String = if (props.location.search.startsWith("?")) {
        props.location.search.substring(1)
      } else {
        props.location.search
      }
      val getParams: Map[String, String] = search
        .split("&")
        .map(_.split("=", 2))
        .map {
          case Array(key, value) => key -> value
        }
        .toMap
      val operationId: Option[OperationId] = getParams.get("operation").map(OperationId.apply)
      val country: String = getParams.getOrElse("country", state.country)

      def handleValidateAccount(child: Self[ActivateAccountProps, Unit]): Unit = {
        UserService.validateAccount(userId, verificationToken, operationId).onComplete {
          case Success(_) =>
            redirectAfterValidation(operationId, country, child.props.history, error = false)
          case Failure(_) =>
            redirectAfterValidation(operationId, state.country, child.props.history, error = true)
        }
      }

      def redirectAfterValidation(operationId: Option[OperationId],
                                  country: String,
                                  history: History,
                                  error: Boolean): Unit = {
        val notifyAction =
          if (error) NotifyError(message = I18n.t("activate-account.notifications.error-message"))
          else NotifySuccess(message = I18n.t("activate-account.notifications.success"))
        operationId match {
          case Some(value) =>
            OperationService.getOperationById(value).onComplete {
              case Success(operation) =>
                OperationStaticData.findBySlugAndCountry(operation.slug, country) match {
                  case Some(_) => history.push(s"/$country/consultation/${operation.slug}")
                  case _       => history.push(s"/$country")
                }
                dispatch(notifyAction)
              case Failure(_) =>
                history.push(s"/$country")
                dispatch(notifyAction)
            }
          case _ =>
            history.push(s"/$country")
            dispatch(notifyAction)
        }
      }

      ActivateAccount.ActivateAccountProps(handleValidateAccount)
    }

}
