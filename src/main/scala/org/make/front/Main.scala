package org.make.front

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.{NativeAction, ReactRedux, Redux}
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.router.Router._
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.redux.{ReactRouterRedux, ReactRouterReduxAction}
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux.RouterReduxVirtualDOMElements
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterReduxAction._
import org.make.front.FrontPage.FrontPageDebateProps
import org.scalajs.dom

import scala.scalajs.js.JSApp

object Main extends JSApp {

  case class AppState(
                       debates: Seq[FrontPageDebateProps]
                     )


  override def main(): Unit = {
    val history = History.createHashHistory()

    val store = Redux.createStore(
      Redux.combineReducers(
        Map(
          "wrapped" -> Reducer.reduce,
          "router" -> ReactRouterRedux.routerReducer
        )
      ),
      ReduxDevTools.composeWithDevTools(
        Redux.applyMiddleware(
          ReactRouterRedux.routerMiddleware(history)
        )
      )
    )

    ReactDOM.render(
      <.Provider(^.store := store)(
        <.ConnectedRouter(^.history := history)(
          <.Route(^.component := RouteControllerComponent.reactClass)()
        )
      ),
      dom.document.getElementById("make-app")
    )

    // <(FrontPage.debateContainer).empty

  }


  object RouteControllerComponent {
    lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(
      (dispatch: Dispatch) => {
        val act = (action: NativeAction) => dispatch(action)
        (_: AppState, _: Unit) => {
          RouteControllerPresentationalComponent.WrappedProps(act)
        }
      }
    )(RouteControllerPresentationalComponent.reactClass)
  }


  object RouteControllerPresentationalComponent extends RouterProps {

    case class WrappedProps(act: (NativeAction) => _)

    type Self = React.Self[WrappedProps, Unit]

    lazy val reactClass: ReactClass = React.createClass[WrappedProps, Unit](_ =>
      <.Switch()(
        <.Route(^.path := "/",           ^.component := FrontPage.debateContainer)(),
        <.Route(^.path := "/debate/:id", ^.component := BRouteComponent.reactClass)()
      )
    )

    private def act(self: Self, action: NativeAction) =
      (event: SyntheticEvent) => {
        event.preventDefault()
        self.props.wrapped.act(action)
      }
  }

}
