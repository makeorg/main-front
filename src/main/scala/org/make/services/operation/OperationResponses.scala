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
