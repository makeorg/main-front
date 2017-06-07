package org.make.front

import io.github.shogowada.scalajs.history.History
import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.redux.{ReactRedux, Redux}
import io.github.shogowada.scalajs.reactjs.router.Router._
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux
import io.github.shogowada.scalajs.reactjs.router.redux.ReactRouterRedux.RouterReduxVirtualDOMElements
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.make.front.FrontPage.{ThemeListProps, ThemeProps}
import org.make.front.Main.RouteControllerPresentationalComponent.WrappedProps
import org.scalajs.dom

import scala.annotation.meta.field
import scala.scalajs.js
import scala.scalajs.js.{Dynamic, JSApp}
import scala.scalajs.js.annotation.JSExport

object Main extends JSApp {

  case class AppState(
                       themes: Seq[ThemeProps],
                       @(JSExport@field) router: js.Object
                     )


  override def main(): Unit = {
    val history = History.createHashHistory()

    val store = Redux.createStore(
      Reducer.reduce,
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

  }


  object RouteControllerComponent extends RouterProps{

    lazy val reactClass: ReactClass = ReactRedux.connectAdvanced { dispatch =>
       (state: AppState, props: Props[Unit]) => {
         Dynamic.global.console.info("called connect")
         WrappedProps(props = ThemeListProps(FrontPage.themes))
       }
    }(RouteControllerPresentationalComponent.reactClass)
  }


  object RouteControllerPresentationalComponent {

    case class WrappedProps(props: ThemeListProps)

    type Self = React.Self[WrappedProps, Unit]

    lazy val reactClass: ReactClass =
      React.createClass[WrappedProps, Unit](self =>
      <.Switch()(
        <.Route(^.path := "/",     ^.component := FrontPage())(),
        <.Route(^.path := "/home", ^.component := FrontPage())()
      )
    )

  }

}
