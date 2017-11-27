package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.LoadConfiguration
import org.make.front.components.AppState

object MaybeThemeContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(MaybeTheme.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => MaybeTheme.MaybeThemeProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val slug = props.`match`.params("themeSlug")

        if (state.configuration.isEmpty) {
          dispatch(LoadConfiguration)
        }

        MaybeTheme.MaybeThemeProps(
          maybeTheme = state.findTheme(slug = slug),
          maybeOperation = None,
          maybeSequence = None,
          maybeLocation = None
        )

      }
    }
}
