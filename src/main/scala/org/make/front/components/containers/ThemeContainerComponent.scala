package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadThemes
import org.make.front.components.presentationals.ThemeComponent
import org.make.front.models.{AppState, Theme}

object ThemeContainerComponent {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ThemeComponent.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ThemeComponent.ThemeProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("themeSlug")
        val themeList: Seq[Theme] = state.themes.filter(_.slug == slug)
        if (themeList.isEmpty) {
          // @toDo: manage 404 redirect
          ThemeComponent.ThemeProps(None)
        } else {
          dispatch(LoadThemes)
          ThemeComponent.ThemeProps(Some(themeList.head))
        }
      }
    }
}
