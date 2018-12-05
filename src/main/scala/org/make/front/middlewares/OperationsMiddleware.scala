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

package org.make.front.middlewares
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions._
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{OperationExpanded, Tag, Operation => OperationModel}
import org.make.services.operation.OperationService
import org.make.services.tag.TagService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object OperationsMiddleware {
  var lastCall: Option[js.Date] = None

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (appStore: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case LoadOperations =>
        if ((lastCall.isEmpty || lastCall.exists { time =>
              (new js.Date().getTime() - time.getTime()) > 2000
            })
            && appStore.getState.operations.isEmpty) {
          lastCall = Some(new js.Date())
          OperationService.listOperations(force = true).onComplete {
            case Success(operations) => dispatch(SetOperations(operations))
            case Failure(_)          => dispatch(NotifyError(I18n.t("error-message.main"), None))
          }
        }
      case SetCurrentOperationSlug(maybeSlug) =>
        if (appStore.getState.currentOperation.slug != maybeSlug) {
          dispatch(SetCurrentOperationSlug(maybeSlug))

          maybeSlug.foreach { slug =>
            val operationExpanded: Future[Option[OperationExpanded]] = {
              val operationAndTags: Future[(Option[OperationModel], js.Array[Tag])] = for {
                operation <- Future.successful(
                  appStore.getState.operations.getOperationBySlugAndCountry(slug, appStore.getState.country)
                )
                tags <- TagService.getTags
              } yield (operation, tags)

              operationAndTags.map {
                case (maybeOperation, tagsList) =>
                  OperationExpanded.getOperationExpandedFromOperation(
                    maybeOperation,
                    tagsList,
                    appStore.getState.country
                  )
              }
            }
            operationExpanded.onComplete {
              case Success(operation) => dispatch(SetCurrentOperation(operation))
              case Failure(_)         => dispatch(NotifyError(I18n.t("error-message.main"), None))
            }
          }
        }
      case action => dispatch(action)
  }
}
