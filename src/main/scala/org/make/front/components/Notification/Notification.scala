package org.make.front.components.Notification

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.models.{Notification, NotificationLevel}
import org.make.front.styles.FontAwesomeStyles
import org.make.front.components.presentationals._

import scalacss.DevDefaults._

object NotificationListComponent {
  case class WrappedProps(notifications: Seq[Notification], onClose: Int => _)

  def apply(props: Props[WrappedProps]): ReactElement =
    <.div()(
      <.style()(NotificationStyles.render[String]),
      <.ul(^.className := NotificationStyles.container)(props.wrapped.notifications.map(notification => {
        NotificationComponent(NotificationComponent.Props(notification = notification, props.wrapped.onClose))
      }))
    )
}

object NotificationComponent {

  case class Props(notification: Notification, onClose: Int => _)

  def apply(props: Props): ReactElement = {
    <.li(^.className := Seq(props.notification.notificationLevel match {
      case NotificationLevel.Error   => ""
      case NotificationLevel.Alert   => ""
      case NotificationLevel.Success => ""
      case NotificationLevel.Info    => ""
      case _                         => ""
    }))(
      <.button(
        ^.key := props.notification.identifier.toString,
        ^.id := props.notification.identifier.toString,
        ^.onClick := ((event: MouseSyntheticEvent) => {
          event.preventDefault()
          props.onClose(props.notification.identifier)
        })
      )(),
      <.i(^.className := FontAwesomeStyles.infoCircle)(),
      <.Translate(^.className := NotificationStyles.text, ^.value := props.notification.title.getOrElse(""))(),
      <.Translate(
        ^.className := NotificationStyles.text,
        ^.value := props.notification.message,
        ^.dangerousHtml := true
      )()
    )
  }
}

object NotificationStyles extends StyleSheet.Inline {

  val container: StyleA = style()
  val text: StyleA = style()
}
