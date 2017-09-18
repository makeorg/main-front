package org.make.front.components.PoliticalActions

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadPoliticalAction
import org.make.front.components.PoliticalActionss.PoliticalActionsListComponent
import org.make.front.models.AppState

import scalajs.js.Dynamic.{global => g}

object PoliticalActionsContainerComponent {

  lazy val reactClass: ReactClass =
    ReactRedux.connectAdvanced(selectorFactory)(PoliticalActionsListComponent.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[Unit]) => PoliticalActionsListComponent.PoliticalActionsListProps =
    (dispatch: Dispatch) => { (state: AppState, _: Props[Unit]) =>
      val politicalActionsList = state.politicalActions

      dispatch(LoadPoliticalAction)

      org.scalajs.dom.window.console.log(politicalActionsList)

      if (politicalActionsList.isEmpty)
        PoliticalActionsListComponent.PoliticalActionsListProps(Seq.empty)
      else
        PoliticalActionsListComponent.PoliticalActionsListProps(politicalActionsList)
    }

}
