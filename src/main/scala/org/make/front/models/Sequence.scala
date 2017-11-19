package org.make.front.models

import org.make.services.sequence.SequenceResponse

import scala.scalajs.js

final case class Sequence(sequenceId: SequenceId, slug: String, title: String, proposals: Seq[Proposal] = Seq.empty)

object Sequence {
  def apply(sequenceResponse: SequenceResponse): Sequence = {
    Sequence(
      sequenceId = SequenceId(sequenceResponse.id),
      slug = sequenceResponse.slug,
      title = sequenceResponse.title,
      proposals = sequenceResponse.proposals.map(Proposal.apply(_))
    )
  }
}

@js.native
trait SequenceId extends js.Object {
  val value: String
}

object SequenceId {
  def apply(value: String): SequenceId = {
    js.Dynamic.literal(value = value).asInstanceOf[SequenceId]
  }
}
