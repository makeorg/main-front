package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadThemes
import org.make.front.models.{AppState, Theme}

object ThemeContainerComponent {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced(selectorFactory)(ThemeHeaderComponent(_))

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => ThemeHeaderComponent.WrappedProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("themeSlug")
        val themeList: Seq[Theme] = state.themes.filter(_.slug == slug)
        if (themeList.isEmpty) {
          // @toDo: manage 404 redirect
          ThemeHeaderComponent.WrappedProps(None)
        } else {
          dispatch(LoadThemes)
          ThemeHeaderComponent.WrappedProps(Some(themeList.head))
        }
      }
    }
}
