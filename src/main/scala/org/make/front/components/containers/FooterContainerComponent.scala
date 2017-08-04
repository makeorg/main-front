package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.LoadThemes
import org.make.front.components.presentationals.FooterComponent
import org.make.front.models.AppState

object FooterContainerComponent {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(FooterComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => FooterComponent.WrappedProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        if (state.themes.isEmpty) {
          dispatch(LoadThemes)
        }

        FooterComponent.WrappedProps(state.themes)

      }
    }
}
