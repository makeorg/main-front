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

package org.make.front.models

import org.make.front.helpers.UndefToOption.undefToOption
import org.make.services.operation.{
  OperationCountryConfigurationResponse,
  OperationResponse,
  OperationTranslationResponse
}

import scala.scalajs.js

final case class Operation(status: String,
                           operationId: OperationId,
                           slug: String,
                           translations: js.Array[OperationTranslation] = js.Array(),
                           defaultLanguage: String,
                           createdAt: Option[js.Date],
                           updatedAt: Option[js.Date],
                           countriesConfiguration: js.Array[OperationCountryConfiguration])
object Operation {
  def apply(operationResponse: OperationResponse): Operation = {
    Operation(
      status = operationResponse.status,
      operationId = OperationId(operationResponse.operationId),
      slug = operationResponse.slug,
      translations =
        operationResponse.translations.map(translationResponse => OperationTranslation.apply(translationResponse)),
      defaultLanguage = operationResponse.defaultLanguage,
      createdAt = undefToOption(operationResponse.createdAt).map(new js.Date(_)),
      updatedAt = undefToOption(operationResponse.updatedAt).map(new js.Date(_)),
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

@js.native
trait QuestionId extends js.Object {
  val value: String
}

object QuestionId {
  def apply(value: String): QuestionId = {
    js.Dynamic.literal(value = value).asInstanceOf[QuestionId]
  }
}

final case class OperationTranslation(title: String, language: String)
object OperationTranslation {
  def apply(operationResponse: OperationTranslationResponse): OperationTranslation = {
    OperationTranslation(operationResponse.title, operationResponse.language)
  }
}

final case class OperationCountryConfiguration(countryCode: String,
                                               tagIds: js.Array[Tag],
                                               landingSequenceId: SequenceId,
                                               startDate: Option[js.Date],
                                               endDate: Option[js.Date],
                                               questionId: QuestionId)
object OperationCountryConfiguration {
  def apply(
    operationCountryConfigurationResponse: OperationCountryConfigurationResponse
  ): OperationCountryConfiguration = {
    OperationCountryConfiguration(
      countryCode = operationCountryConfigurationResponse.countryCode,
      tagIds = operationCountryConfigurationResponse.tagIds.map(tagId => Tag(tagId = TagId(tagId), label = tagId)),
      landingSequenceId = undefToOption(operationCountryConfigurationResponse.landingSequenceId)
        .map(SequenceId(_)) match {
        case Some(sequenceId) => sequenceId
        case _                => throw new IllegalArgumentException("a landing sequence id is required")
      },
      startDate = undefToOption(operationCountryConfigurationResponse.startDate).map(new js.Date(_)),
      endDate = undefToOption(operationCountryConfigurationResponse.endDate).map(new js.Date(_)),
      questionId = QuestionId(operationCountryConfigurationResponse.questionId)
    )
  }
}
