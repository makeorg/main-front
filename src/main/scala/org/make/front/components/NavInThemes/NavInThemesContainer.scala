package org.make.front.components.NavInThemes

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadThemes
import org.make.front.models.AppState

object NavInThemesContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(NavInThemesComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => NavInThemesComponent.WrappedProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        if (state.themes.isEmpty) {
          dispatch(LoadThemes)
        }

        NavInThemesComponent.WrappedProps(state.themes)

      }
    }
}
