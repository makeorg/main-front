package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.components.AppState
import org.make.front.actions._

import scala.util.Try

object CookieAlertMiddleware {

  private var listeners: Map[String, CookieAlertListener] = Map.empty

  final case class CookieAlertListener(onDismissCookieAlert: () => Unit)

  def addCookieAlertListener(id: String, listener: CookieAlertListener): Unit = {
    listeners += id -> listener
  }

  def removeCookieAlertListener(id: String): Unit = {
    listeners -= id
  }

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (_: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case OnDismissCookieAlert =>
        listeners.values.foreach(listener => Try(listener.onDismissCookieAlert()))
      case action => dispatch(action)
  }
}
