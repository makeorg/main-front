package org.make.front.components.navInThemes

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadThemes
import org.make.front.components.AppState

object NavInThemesContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(NavInThemes.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => NavInThemes.WrappedProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        if (state.themes.isEmpty) {
          dispatch(LoadThemes)
        }

        NavInThemes.WrappedProps(state.themes)

      }
    }
}
