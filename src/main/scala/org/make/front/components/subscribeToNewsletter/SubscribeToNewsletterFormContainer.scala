package org.make.front.components.subscribeToNewsletter

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyInfo
import org.make.front.components.AppState
import org.make.front.components.subscribeToNewsletter.SubscribeToNewsletterForm.SubscribeToNewsletterFormProps
import org.make.front.facades.I18n
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object SubscribeToNewsletterFormContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SubscribeToNewsletterForm.reactClass)

  case class SubscribeToNewsletterFormContainerProps(onSubscribeToNewsletterSuccess: () => Unit = () => {})

  def selectorFactory
    : (Dispatch) => (AppState, Props[SubscribeToNewsletterFormContainerProps]) => SubscribeToNewsletterFormProps =
    (dispatch: Dispatch) => { (_: AppState, props) =>
      def handleSubmit(email: String): Future[_] = {
        val future = UserService.subscribeToNewsletter(email)
        future.onComplete {
          case Success(_) =>
            dispatch(NotifyInfo(message = I18n.t("subscribe-to-newsletter.notifications.success")))
            props.wrapped.onSubscribeToNewsletterSuccess()
          case Failure(_) =>
        }
        future
      }
      SubscribeToNewsletterForm.SubscribeToNewsletterFormProps(handleSubmit = handleSubmit)
    }
}
