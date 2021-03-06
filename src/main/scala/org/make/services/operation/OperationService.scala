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

package org.make.services.operation

import org.make.client.{Client, MakeApiClient}
import org.make.core.URI._
import org.make.front.facades.I18n
import org.make.front.models._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object OperationService extends ApiService {

  override val resourceName: String = "operations"
  var client: Client = MakeApiClient

  val defaultResultsCount = 20
  var operationsCache: Option[Future[js.Array[Operation]]] = None

  def getOperationById(operationId: OperationId, forceRefresh: Boolean = false): Future[Operation] = {
    if (forceRefresh) {
      client
        .get[OperationResponse](
          apiEndpoint = resourceName / operationId.value,
          urlParams = js.Array(),
          headers = Map.empty
        )
        .map(Operation.apply)
    } else {
      listOperations().map { operations =>
        operations.findById(operationId).getOrElse(throw new NotFoundException)
      }
    }
  }

  def listOperations(force: Boolean = false): Future[OperationList] = {

    val futureOperations: Future[js.Array[Operation]] = operationsCache match {
      case Some(matchingFutureOperations) if !force => matchingFutureOperations
      case _ =>
        val futureOperations: Future[js.Array[Operation]] = client
          .get[js.Array[OperationResponse]](apiEndpoint = resourceName, urlParams = js.Array(), headers = Map.empty)
          .map(_.map(Operation.apply))
        operationsCache = Some(futureOperations)
        futureOperations
    }
    futureOperations.map(OperationList(_))
  }

  final case class UnexpectedException(message: String = I18n.t("errors.unexpected")) extends Exception(message)
  final case class NotFoundException(message: String = I18n.t("errors.unexpected")) extends Exception(message)
}
