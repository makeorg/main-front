package org.make.front.components.politicalActions

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadPoliticalAction
import org.make.front.components.AppState

object PoliticalActionsContainer {

  lazy val reactClass: ReactClass =
    ReactRedux.connectAdvanced(selectorFactory)(PoliticalActionsList.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => PoliticalActionsList.PoliticalActionsListProps =
    (dispatch: Dispatch) => { (state: AppState, _: Props[Unit]) =>
      val politicalActionsList = state.politicalActions

      dispatch(LoadPoliticalAction)

      if (politicalActionsList.isEmpty)
        PoliticalActionsList.PoliticalActionsListProps(Seq.empty)
      else
        PoliticalActionsList.PoliticalActionsListProps(politicalActionsList)
    }

}
