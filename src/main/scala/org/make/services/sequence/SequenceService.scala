package org.make.services.sequence

import org.make.client.MakeApiClient
import org.make.core.URI._
import org.make.front.models._
import org.make.services.ApiService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SequenceService extends ApiService {

  override val resourceName: String = "sequences"

  def startSequenceBySlug(slug: String, includes: Seq[ProposalId]): Future[Sequence] = {
    MakeApiClient.get[SequenceResponse](resourceName / slug, includes.map("include" -> _.value)).map(Sequence.apply)
  }

  def startSequenceById(sequenceId: SequenceId, includes: Seq[ProposalId]): Future[Sequence] = {
    MakeApiClient
      .get[SequenceResponse](resourceName / "start-sequence" / sequenceId.value, includes.map("include" -> _.value))
      .map(Sequence.apply)
  }
}
