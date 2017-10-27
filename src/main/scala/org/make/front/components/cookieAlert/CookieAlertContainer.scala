package org.make.front.components.cookieAlert

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState

import org.make.front.facades.Cookies

object CookieAlertContainer {

  final case class CookieAlertContainerProps()

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(CookieAlert.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[CookieAlertContainerProps]) => CookieAlert.CookieAlertProps =
    (_: Dispatch) => { (appState: AppState, ownProps: Props[CookieAlertContainerProps]) =>
      var isAlertOpened: Boolean = !Cookies.get("cookieconsent_status").isDefined
      def dismissCookieAlert: () => Unit = { () =>
        Cookies.set("cookieconsent_status", "dismiss")
      }
      CookieAlert.CookieAlertProps(isAlertOpened = isAlertOpened, closeCallback = dismissCookieAlert)
    }
}
