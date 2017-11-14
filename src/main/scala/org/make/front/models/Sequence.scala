package org.make.front.models

import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.components.AppState

import scala.scalajs.js

@js.native
trait SequenceResponse extends js.Object {
  val sequenceId: String
  val slug: String
  val title: String
  val proposalsSlugs: js.Array[String]

}

final case class Sequence(sequenceId: SequenceId, slug: String, title: String)

object Sequence {
  def apply(sequenceResponse: SequenceResponse): Sequence = {
    Sequence(
      sequenceId = SequenceId(sequenceResponse.sequenceId),
      slug = sequenceResponse.slug,
      title = sequenceResponse.title
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
