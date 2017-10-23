package org.make.front.models

import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.components.AppState

final case class Sequence(sequenceId: SequenceId, slug: String, title: String)

final case class SequenceId(value: String) extends StringValue


object Sequence {
  def getSequenceById(id: String, store: Store[AppState]): Sequence = {
    store.getState.sequences.find(sequence => sequence.sequenceId.value == id).get
  }
}