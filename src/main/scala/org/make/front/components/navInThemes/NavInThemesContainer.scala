package org.make.front.components.navInThemes

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState

object NavInThemesContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(NavInThemes.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => NavInThemes.WrappedProps =
    (dispatch: Dispatch) => { (state: AppState, _: Props[Unit]) =>
      if (state.configuration.isEmpty) {
        dispatch(LoadConfiguration)
      }

      NavInThemes.WrappedProps(state.themes)
    }
}
