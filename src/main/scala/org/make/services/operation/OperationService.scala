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
  var operationsCache: Option[Future[Seq[Operation]]] = None

  def getOperationById(operationId: OperationId, forceRefresh: Boolean = false): Future[Operation] = {
    if (forceRefresh) {
      client
        .get[OperationResponse](
          apiEndpoint = resourceName / operationId.value,
          urlParams = Seq.empty,
          headers = Map.empty
        )
        .map(Operation.apply)
    } else {
      getOperations.map { operations =>
        operations.filter(_.operationId == operationId) match {
          case operations if operations.nonEmpty => operations.head
          case _                                 => throw new NotFoundException
        }
      }
    }
  }

  def getOperationBySlug(slug: String): Future[Option[Operation]] = {
    getOperations.map { operations =>
      operations.filter(_.slug == slug) match {
        case operations if operations.nonEmpty => Some(operations.head)
        case _                                 => None
      }
    }
  }

  def getOperations(): Future[Seq[Operation]] = {

    val futureOperations: Future[Seq[Operation]] = operationsCache match {
      case Some(futureOperations) => futureOperations
      case _ =>
        val futureOperations: Future[Seq[Operation]] = client
          .get[js.Array[OperationResponse]](apiEndpoint = resourceName, urlParams = Seq.empty, headers = Map.empty)
          .map(_.map((Operation.apply)))

        operationsCache = Some(futureOperations)

        futureOperations
    }
    futureOperations
  }

  final case class UnexpectedException(message: String = I18n.t("errors.unexpected")) extends Exception(message)
  final case class NotFoundException(message: String = I18n.t("errors.unexpected")) extends Exception(message)
}
