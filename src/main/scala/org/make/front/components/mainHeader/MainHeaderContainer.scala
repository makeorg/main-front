package org.make.front.components.mainHeader

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState

object MainHeaderContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(MainHeader.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => MainHeader.MainHeaderProps =
    (_: Dispatch) => { (appState: AppState, _: Props[Unit]) =>
      {
        MainHeader.MainHeaderProps(country = appState.country)
      }
    }
}
