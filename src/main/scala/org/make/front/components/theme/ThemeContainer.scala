package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState
import org.make.front.models.{ThemeId, Theme => ThemeModel}

object ThemeContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Theme.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Theme.ThemeProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("themeSlug")
        val themesList: Seq[ThemeModel] = state.themes.filter(_.slug == slug)
        if (themesList.isEmpty) {
          props.history.push("/")
          Theme.ThemeProps(ThemeModel(ThemeId("fake"), "", "", 0, 0, "", None))
        } else {
          dispatch(LoadConfiguration)
          Theme.ThemeProps(themesList.head)
        }
      }
    }
}
