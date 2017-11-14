package org.make.front.models

import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.components.AppState
import org.make.front.models.{Sequence => SequenceModel}

import scala.scalajs.js

final case class Operation(operationId: OperationId,
                           url: String,
                           slug: String,
                           title: String,
                           label: String,
                           actionsCount: Int,
                           proposalsCount: Int,
                           color: String,
                           gradient: Option[GradientColor] = None,
                           tags: Seq[Tag] = Seq.empty,
                           logoUrl: Option[String] = None,
                           darkerLogoUrl: Option[String] = None,
                           sequence: Option[SequenceModel] = None)
@js.native
trait OperationId extends js.Object {
  val value: String
}

object OperationId {
  def apply(value: String): OperationId = {
    js.Dynamic.literal(value = value).asInstanceOf[OperationId]
  }
}
// @todo: use a sealaed trait and case object like Source and Location
object Operation {
  val vff: String = "vff"

  def getOperationById(id: String, store: Store[AppState]): Option[Operation] = {
    store.getState.operations.find(operation => operation.operationId.value == id)
  }
}
