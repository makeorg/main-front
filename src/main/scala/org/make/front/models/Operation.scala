package org.make.front.models

import io.circe.{Decoder, Encoder, Json}
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.core.StringValue
import org.make.front.components.AppState

final case class Operation(id: OperationId,
                           slug: String,
                           title: String,
                           actionsCount: Int,
                           proposalsCount: Int,
                           color: String,
                           gradient: Option[GradientColor] = None,
                           tags: Seq[Tag] = Seq.empty)

final case class OperationId(value: String) extends StringValue

object OperationId {
  implicit lazy val operationIdEncoder: Encoder[OperationId] = (a: OperationId) => Json.fromString(a.value)
  implicit lazy val operationIdDecoder: Decoder[OperationId] = Decoder.decodeString.map(OperationId(_))
}

object Operation {
  def getOperationById(id: String, store: Store[AppState]): Operation = {
    store.getState.operations.find(operation => operation.id.value == id).get
  }
}