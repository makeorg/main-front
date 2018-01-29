package org.make.front.components.error

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.{LoadConfiguration}
import org.make.front.components.AppState
import scala.util.Random

object ErrorContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Error.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Error.ErrorProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      def redirectToRandomTheme: () => Unit = { () =>
        if (state.configuration.isEmpty) {
          dispatch(LoadConfiguration)
        }

        val randomThemeSlug = Random.shuffle(state.themes).head.slug
        props.history.push(s"/theme/$randomThemeSlug")
      }

      Error.ErrorProps(redirectToRandomTheme = redirectToRandomTheme)

    }
}
