package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState
import org.make.front.models.{Location, Operation => OperationModel, OperationId => OperationIdModel}

object OperationContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Operation.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Operation.OperationProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("operationSlug")
        val OperationsList: Seq[OperationModel] = state.operations.filter(_.slug == slug)
        if (OperationsList.isEmpty) {
          props.history.push("/")
          Operation.OperationProps(
            OperationModel(OperationIdModel("fake"), "", "", "", "", 0, 0, "", None),
            None,
            Some(Location.Homepage)
          )
        } else {
          dispatch(LoadConfiguration)
          Operation.OperationProps(OperationsList.head, None, None)
        }
      }
    }
}
