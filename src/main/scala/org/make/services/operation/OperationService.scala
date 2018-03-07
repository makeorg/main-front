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
      listOperations().map { operations =>
        operations.find(_.operationId.value == operationId.value).getOrElse(throw new NotFoundException)
      }
    }
  }

  def getOperationBySlug(slug: String): Future[Option[Operation]] = {
    listOperations().map { operations =>
      operations.find(_.slug == slug)
    }
  }

  def getOperationBySlugAndCountry(slug: String, country: String): Future[Option[Operation]] = {
    listOperations().map { operations =>
      operations.find(
        operation => operation.slug == slug && operation.countriesConfiguration.map(_.countryCode).contains(country)
      )
    }
  }

  def getOperationsByCountry(country: String): Future[Seq[Operation]] = {
    listOperations().map { operations =>
      operations.filter(operation => operation.countriesConfiguration.map(_.countryCode).contains(country))
    }
  }

  def listOperations(): Future[Seq[Operation]] = {

    val futureOperations: Future[Seq[Operation]] = operationsCache match {
      case Some(matchingFutureOperations) => matchingFutureOperations
      case _ =>
        val futureOperations: Future[Seq[Operation]] = client
          .get[js.Array[OperationResponse]](apiEndpoint = resourceName, urlParams = Seq.empty, headers = Map.empty)
          .map(_.map(Operation.apply))
        operationsCache = Some(futureOperations)
        futureOperations
    }
    futureOperations

    Future.successful(
      Seq(
        Operation(
          status = "Active",
          operationId = OperationId("test"),
          slug = "mieux-vivre-ensemble",
          translations = Seq(OperationTranslation(title = "mieux vivre ensemble", language = "fr")),
          defaultLanguage = "fr",
          createdAt = None,
          updatedAt = None,
          countriesConfiguration = Seq(
            OperationCountryConfiguration(
              countryCode = "FR",
              tagIds = Seq(Tag(tagId = TagId("acces-a-l-emploi"), label = "acces emploi")),
              landingSequenceId = SequenceId("34d3bd9a-c894-4278-981e-2aa13e0efe6b"),
              startDate = None,
              endDate = None
            )
          )
        )
      )
    )
  }

  final case class UnexpectedException(message: String = I18n.t("errors.unexpected")) extends Exception(message)
  final case class NotFoundException(message: String = I18n.t("errors.unexpected")) extends Exception(message)
}
