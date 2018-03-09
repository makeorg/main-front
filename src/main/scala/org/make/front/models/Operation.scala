package org.make.front.models

import org.make.services.operation.{
  OperationCountryConfigurationResponse,
  OperationResponse,
  OperationTranslationResponse
}
import scala.scalajs.js

final case class Operation(status: String,
                           operationId: OperationId,
                           slug: String,
                           translations: Seq[OperationTranslation] = Seq.empty,
                           defaultLanguage: String,
                           createdAt: Option[js.Date],
                           updatedAt: Option[js.Date],
                           countriesConfiguration: Seq[OperationCountryConfiguration])
object Operation {
  def apply(operationResponse: OperationResponse): Operation = {
    Operation(
      status = operationResponse.status,
      operationId = OperationId(operationResponse.operationId),
      slug = operationResponse.slug,
      translations =
        operationResponse.translations.map(translationResponse => OperationTranslation.apply(translationResponse)),
      defaultLanguage = operationResponse.defaultLanguage,
      createdAt = operationResponse.createdAt.toOption.map(new js.Date(_)),
      updatedAt = operationResponse.updatedAt.toOption.map(new js.Date(_)),
      countriesConfiguration = operationResponse.countriesConfiguration.map(
        countryConfigurationResponse => OperationCountryConfiguration.apply(countryConfigurationResponse)
      )
    )
  }
}
@js.native
trait OperationId extends js.Object {
  val value: String
}

object OperationId {
  def apply(value: String): OperationId = {
    js.Dynamic.literal(value = value).asInstanceOf[OperationId]
  }
}

final case class OperationTranslation(title: String, language: String)
object OperationTranslation {
  def apply(operationResponse: OperationTranslationResponse): OperationTranslation = {
    OperationTranslation(operationResponse.title, operationResponse.language)
  }
}

final case class OperationCountryConfiguration(countryCode: String,
                                               tagIds: Seq[Tag],
                                               landingSequenceId: SequenceId,
                                               startDate: Option[js.Date],
                                               endDate: Option[js.Date])
object OperationCountryConfiguration {
  def apply(
    operationCountryConfigurationResponse: OperationCountryConfigurationResponse
  ): OperationCountryConfiguration = {
    OperationCountryConfiguration(
      countryCode = operationCountryConfigurationResponse.countryCode,
      tagIds = operationCountryConfigurationResponse.tagIds.map(tagId => Tag(tagId = TagId(tagId), label = tagId)),
      landingSequenceId = operationCountryConfigurationResponse.landingSequenceId.toOption
        .map(SequenceId(_)) match {
        case Some(sequenceId) => sequenceId
        case _                => throw new IllegalArgumentException("a landing sequence id is required")
      },
      startDate = operationCountryConfigurationResponse.startDate.toOption.map(new js.Date(_)),
      endDate = operationCountryConfigurationResponse.endDate.toOption.map(new js.Date(_))
    )
  }
}
