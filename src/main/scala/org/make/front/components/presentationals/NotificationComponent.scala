package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.facades.Translate.{TranslateVirtualDOMAttributes, TranslateVirtualDOMElements}
import org.make.front.models.{Notification, NotificationLevel}
import org.make.front.styles.{BulmaStyles, FontAwesomeStyles}

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
    <.li(^.className := Seq(BulmaStyles.Element.notification, props.notification.notificationLevel match {
      case NotificationLevel.Error   => BulmaStyles.Syntax.isDanger
      case NotificationLevel.Alert   => BulmaStyles.Syntax.isWarning
      case NotificationLevel.Success => BulmaStyles.Syntax.isSuccess
      case NotificationLevel.Info    => BulmaStyles.Syntax.isInfo
      case _                         => BulmaStyles.Syntax.isPrimary
    }))(
      <.button(
        ^.key := props.notification.identifier.toString,
        ^.id := props.notification.identifier.toString,
        ^.className := BulmaStyles.Element.notificationDelete,
        ^.onClick := ((event: MouseSyntheticEvent) => {
          event.preventDefault()
          props.onClose(props.notification.identifier)
        })
      )(),
      <.span(^.className := BulmaStyles.Element.icon)(<.i(^.className := FontAwesomeStyles.infoCircle)()),
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

  import dsl._

  val container: StyleA = style(
    zIndex(1000),
    top(1.rem),
    left(`0`),
    position.fixed,
    width(100.%%),
    opacity(0.98),
    fontWeight._600,
    fontSize(1.5.rem)
  )
  val text: StyleA = style(paddingLeft(1.rem), paddingRight(1.rem))
}
