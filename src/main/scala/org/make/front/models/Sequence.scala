package org.make.front.models

import io.circe.{Decoder, Encoder, Json}
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.core.StringValue
import org.make.front.components.AppState

final case class Sequence(sequenceId: SequenceId, slug: String, title: String, proposals: Seq[ProposalId])

final case class SequenceId(value: String) extends StringValue

object SequenceId {
  implicit lazy val operationIdEncoder: Encoder[SequenceId] = (a: SequenceId) => Json.fromString(a.value)
  implicit lazy val operationIdDecoder: Decoder[SequenceId] = Decoder.decodeString.map(SequenceId(_))
}

object Sequence {
  def getSequenceById(id: String, store: Store[AppState]): Sequence = {
    store.getState.sequences.find(sequence => sequence.sequenceId.value == id).get
  }
}
