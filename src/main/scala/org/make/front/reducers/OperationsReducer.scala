package org.make.front.reducers

import org.make.front.actions.SetConfiguration
import org.make.front.models.{Operation => OperationModel}

object OperationsReducer {
  def reduce(maybeOperations: Option[Seq[OperationModel]], action: Any): Seq[OperationModel] = {
    val operations = maybeOperations.getOrElse(OperationModel.defaultOperations)
    action match {
      case SetConfiguration(configuration) =>
        operations.map { operation =>
          if (operation.operationId.value == OperationModel.vff) {
            operation.copy(tags = configuration.tagsVFF)
          } else {
            operation
          }
        }
      case _ => operations
    }
  }
}
