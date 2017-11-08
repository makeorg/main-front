package org.make.front.components.sequence.contents

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import org.make.front.components.AppState

object ConclusionOfTheSequenceContainer extends RouterProps {
  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced {
    dispatch: Dispatch => (state: AppState, _: Props[Unit]) =>
      ConclusionOfTheSequence.ConclusionOfTheSequenceProps(isConnected = state.connectedUser.isDefined)
  }(ConclusionOfTheSequence.reactClass)
}
