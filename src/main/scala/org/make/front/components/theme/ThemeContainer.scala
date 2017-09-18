package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadThemes
import org.make.front.components.AppState
import org.make.front.models.{Theme => ThemeModel}

object ThemeContainerComponent {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Theme.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Theme.ThemeProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("themeSlug")
        val themeList: Seq[ThemeModel] = state.themes.filter(_.slug == slug)
        if (themeList.isEmpty) {
          // @toDo: manage 404 redirect
          Theme.ThemeProps(None, slug)
        } else {
          dispatch(LoadThemes)
          Theme.ThemeProps(Some(themeList.head), slug)
        }
      }
    }
}
