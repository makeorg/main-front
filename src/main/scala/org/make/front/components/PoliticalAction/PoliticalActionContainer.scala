package org.make.front.components.PoliticalAction

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadPoliticalAction
import org.make.front.models.AppState

object PoliticalActionContainerComponent {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(PoliticalActionComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => PoliticalActionComponent.WrappedProps =
    (dispatch: Dispatch) => { (state: AppState, _: Props[Unit]) =>
      val politicalActionList = state.politicalActions
      // todo: load political actions according to theme
      dispatch(LoadPoliticalAction)
      if (politicalActionList.isEmpty)
        PoliticalActionComponent.WrappedProps(Seq.empty)
      else
        PoliticalActionComponent.WrappedProps(politicalActionList)
    }

}
