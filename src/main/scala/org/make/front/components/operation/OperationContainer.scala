package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.components.AppState
import org.make.front.models.{Location, OperationExpanded}
import org.make.services.operation.OperationService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object OperationContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Operation.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Operation.OperationProps =
    (_: Dispatch) => { (_: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("operationSlug")

        val operationExpanded: Future[Option[OperationExpanded]] = OperationService
          .getOperationBySlug(slug)
          .map(_.map { operation =>
            OperationExpanded.getOperationExpandedFromOperation(operation)
          })

        Operation.OperationProps(operationExpanded, None, None)
      }
    }
}
