package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.components.AppState
import org.make.front.components.search.Search.SearchProps
import org.make.front.helpers.QueryString

object SearchContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Search.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => SearchProps =
    (_: Dispatch) => { (_: AppState, props: Props[Unit]) =>
      {
        val queryParams: Map[String, String] = QueryString.parse(props.location.search)
        def searchValue: Option[String] = queryParams.get("q") match {
          case Some(value) if value.isEmpty => None
          case other                        => other
        }

        Search.SearchProps(searchValue)
      }
    }
}
