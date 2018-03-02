package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState

object SearchFormContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SearchForm.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => SearchForm.SearchFormProps =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        SearchForm.SearchFormProps(country = appState.country)
      }
    }
}
