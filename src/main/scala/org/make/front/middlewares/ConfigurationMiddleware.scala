package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.{LoadConfiguration, NotifyError, SetConfiguration}
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.services.ConfigurationService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.{Failure, Success}

object ConfigurationMiddleware {

  var lastCall: Option[js.Date] = None

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (appStore: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case LoadConfiguration =>
        if ((lastCall.isEmpty || lastCall.exists { time =>
              (new js.Date().getTime() - time.getTime()) > 2000
            })
            && appStore.getState.configuration.isEmpty) {
          lastCall = Some(new js.Date())
          ConfigurationService.fetchConfiguration().onComplete {
            case Success(configuration) => dispatch(SetConfiguration(configuration))
            case Failure(e)             => dispatch(NotifyError(I18n.t("errors.main"), None))
          }
        }
      case action => dispatch(action)
  }

}
