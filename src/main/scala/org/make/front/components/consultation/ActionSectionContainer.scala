package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.components.consultation.ActionsSection.ActionsSectionProps
import org.make.front.models.{OperationExpanded}

object ActionsSectionContainer {

  case class ActionsSectionContainerProps(operation: OperationExpanded,
                                          language: String)

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _: Dispatch => (state: AppState, props: Props[ActionsSectionContainerProps]) =>
      ActionsSectionProps(
        operation = props.wrapped.operation,
        language = props.wrapped.language,
        isConnected = state.connectedUser.isDefined
      )

  }(ActionsSection.reactClass)
}
