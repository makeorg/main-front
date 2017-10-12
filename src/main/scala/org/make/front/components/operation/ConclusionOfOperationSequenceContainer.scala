package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.front.components.AppState

object ConclusionOfOperationSequenceContainer extends RouterProps {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced {
    dispatch: Dispatch => (state: AppState, _: Props[Unit]) =>
      ConclusionOfOperationSequence.ConclusionOfOperationSequenceProps(isConnected = state.connectedUser.isDefined)
  }(ConclusionOfOperationSequence.reactClass)
}
