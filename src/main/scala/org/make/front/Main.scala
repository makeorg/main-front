package org.make.front

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.devtools.ReduxDevTools
import io.github.shogowada.scalajs.reactjs.redux.{Redux, Store}
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.actions.{LoggedInAction, SetConfiguration}
import org.make.front.components.AppState
import org.make.front.components.Components.RichVirtualDOMElements
import org.make.front.facades.{I18n, NativeReactModal}
import org.make.front.middlewares._
import org.make.front.models.User
import org.make.front.reducers.Reducer
import org.make.services.ConfigurationService
import org.make.services.user.UserService
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalacss.defaults.Exports
import scalacss.internal.mutable.Settings

object Main {

  val CssSettings: Exports with Settings = scalacss.devOrProdDefaults

  def main(args: Array[String]): Unit = {
    Translations.loadTranslations()
    I18n.setLocale("fr")

    NativeReactModal.defaultStyles.overlay.update("zIndex", "9")
    NativeReactModal.defaultStyles.overlay.update("backgroundColor", "rgba(0, 0, 0, 0.7)")

    NativeReactModal.defaultStyles.content.update("position", "fixed")
    NativeReactModal.defaultStyles.content.update("top", "0")
    NativeReactModal.defaultStyles.content.update("right", "0")
    NativeReactModal.defaultStyles.content.update("bottom", "0")
    NativeReactModal.defaultStyles.content.update("left", "0")
    NativeReactModal.defaultStyles.content.update("width", "100%")
    NativeReactModal.defaultStyles.content.update("height", "100%")
    NativeReactModal.defaultStyles.content.update("padding", "0")
    NativeReactModal.defaultStyles.content.update("border", "none")
    NativeReactModal.defaultStyles.content.update("borderRadius", "0")
    NativeReactModal.defaultStyles.content.update("background", "transparent")
    NativeReactModal.defaultStyles.content.update("overflow", "auto")

    val configurationFuture = ConfigurationService.fetchConfiguration()
    val connectedUserFuture =
      UserService.getCurrentUser().map(Some(_)).recoverWith { case _ => Future.successful(None) }

    val configurationAndUser = for {
      configuration <- configurationFuture
      maybeUser     <- connectedUserFuture
    } yield (configuration, maybeUser)

    configurationAndUser.onComplete {
      case Failure(_) => main(args)
      case Success((configuration, maybeUser)) =>
        val connectedUserMiddleware = new ConnectedUserMiddleware()

        val store: Store[AppState] = Redux.createStore(
          Reducer.reduce,
          ReduxDevTools.composeWithDevTools(
            Redux.applyMiddleware(
              ConfigurationMiddleware.handle,
              PoliticalActionMiddleware.handle,
              connectedUserMiddleware.handle,
              CookieAlertMiddleware.handle,
              NotificationMiddleware.handle
            )
          )
        )

        store.dispatch(SetConfiguration(configuration))
        maybeUser.foreach { user =>
          store.dispatch(LoggedInAction(user))
        }

        startAppWhenReady(maybeUser, store)
    }
  }

  private def startAppWhenReady(maybeUser: Option[User], store: Store[AppState]): Unit = {
    if (store.getState.configuration.isEmpty || store.getState.connectedUser.isDefined != maybeUser.isDefined) {
      org.scalajs.dom.window.setTimeout(() => startAppWhenReady(maybeUser, store), 20d)
    } else {
      ReactDOM.render(
        <.Provider(^.store := store)(<.HashRouter()(<.AppComponent.empty)),
        dom.document.getElementById("make-app")
      )
    }
  }
}
