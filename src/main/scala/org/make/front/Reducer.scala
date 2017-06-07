package org.make.front

import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux
import org.make.front.Main.AppState

import scala.scalajs.js.Dynamic

object Reducer {

  def reduce(maybeState: Option[AppState], action: Any): AppState = {

    Dynamic.global.console.info(s"Reducing ${maybeState.toString}, with action ${action.toString}")

    val state = maybeState.getOrElse(defaultState())
    state.copy(router = ReactRouterRedux.routerReducer(maybeState.map(_.router), action))

  }

  private def defaultState(action: Any): AppState =
    AppState(
      themes = FrontPage.themes,
      router = ReactRouterRedux.routerReducer(None, action)
    )

}
