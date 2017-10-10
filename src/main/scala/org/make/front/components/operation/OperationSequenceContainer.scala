package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState
import org.make.front.models.{
  OperationId => OperationIdModel,
  Operation   => OperationModel,
  SequenceId  => SequenceIdModel,
  Sequence    => SequenceModel
}

object OperationSequenceContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(OperationSequence.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => OperationSequence.OperationSequenceProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val operationSlug = props.`match`.params("operationSlug")
        val OperationsList: Seq[OperationModel] = state.operations.filter(_.slug == operationSlug)

        val sequenceSlug = props.`match`.params("sequenceSlug")
        val SequencesList: Seq[SequenceModel] = state.sequences.filter(_.slug == sequenceSlug)

        if (OperationsList.isEmpty || SequencesList.isEmpty) {
          props.history.push("/")
          OperationSequence.OperationSequenceProps(
            OperationModel(OperationIdModel("fake"), "", "", 0, 0, "", None),
            SequenceModel(SequenceIdModel("fake"), "", "")
          )
        } else {
          dispatch(LoadConfiguration)
          OperationSequence.OperationSequenceProps(OperationsList.head, SequencesList.head)
        }
      }
    }
}
