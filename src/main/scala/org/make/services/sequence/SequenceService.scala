package org.make.services.sequence

import org.make.client.MakeApiClient
import org.make.core.URI._
import org.make.front.models._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object SequenceService extends ApiService {

  override val resourceName: String = "sequences"

  def startSequenceById(sequenceId: SequenceId, includes: js.Array[ProposalId]): Future[Sequence] = {
    MakeApiClient
      .get[SequenceResponse](resourceName / "start" / sequenceId.value, includes.map("include" -> _.value))
      .map(Sequence.apply)
  }
}
