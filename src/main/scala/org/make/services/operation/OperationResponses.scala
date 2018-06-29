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

import scala.scalajs.js

@js.native
trait OperationResponse extends js.Object {
  val operationId: String
  val status: String
  val slug: String
  val translations: js.Array[OperationTranslationResponse]
  val defaultLanguage: String
  @deprecated("will be removed", "") val sequenceLandingId: js.UndefOr[String]
  val createdAt: js.UndefOr[String]
  val updatedAt: js.UndefOr[String]
  val countriesConfiguration: js.Array[OperationCountryConfigurationResponse]
}

@js.native
trait OperationContextResponse extends js.Object {
  val operation: js.UndefOr[String]
  val source: js.UndefOr[String]
  val location: js.UndefOr[String]
  val question: js.UndefOr[String]
}

@js.native
trait OperationTranslationResponse extends js.Object {
  val title: String
  val language: String
}

@js.native
trait OperationCountryConfigurationResponse extends js.Object {
  val countryCode: String
  val tagIds: js.Array[String]
  val landingSequenceId: js.UndefOr[String]
  val startDate: js.UndefOr[String]
  val endDate: js.UndefOr[String]
}
