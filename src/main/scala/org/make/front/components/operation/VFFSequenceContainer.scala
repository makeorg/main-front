package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState
import org.make.front.models.{Operation => OperationModel, Sequence => SequenceModel}

object VFFSequenceContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(OperationSequence.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => OperationSequence.OperationSequenceProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {

        val OperationsList: Seq[OperationModel] =
          state.operations.filter(_.slug == "stop-aux-violences-faites-aux-femmes")

        val SequencesList: Seq[SequenceModel] =
          state.sequences.filter(_.slug == "comment-lutter-contre-les-violences-faites-aux-femmes")

        dispatch(LoadConfiguration)
        OperationSequence.OperationSequenceProps(OperationsList.head, SequencesList.head)

      }
    }
}
