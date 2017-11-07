package org.make.front.components.notifications

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.middlewares.NotificationMiddleware
import org.make.front.middlewares.NotificationMiddleware.NotificationListener
import org.make.front.models.NotificationLevel._
import org.make.front.models.{Notification => NotificationModel, NotificationLevel => NotificationLevelModel}
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

object Notifications {
  type NotificationsProps = Unit

  case class NotificationsState(id: String, notifications: List[NotificationModel])

  lazy val reactClass: ReactClass =
    React.createClass[NotificationsProps, NotificationsState](
      displayName = "Notifications",
      getInitialState = { _ =>
        NotificationsState(UUID.randomUUID().toString, Nil)
      },
      componentDidMount = { self =>
        val onNewNotification: (NotificationModel) => Unit = { notification =>
          self.setState(state => state.copy(notifications = notification :: state.notifications))
        }
        val onDismiss: (String) => Unit = { id =>
          self.setState(state => state.copy(notifications = state.notifications.filter(_.identifier != id)))
        }
        NotificationMiddleware
          .addNotificationListener(self.state.id, NotificationListener(onNewNotification, onDismiss))
      },
      componentWillUnmount = { self =>
        NotificationMiddleware.removeNotificationListener(self.state.id)
      },
      render = (self) => {

        def closeNotification(id: String): (MouseSyntheticEvent) => Unit = { event =>
          event.preventDefault()
          self.setState(state => state.copy(notifications = state.notifications.filter(_.identifier != id)))
        }

        def NotificationClasses(level: NotificationLevelModel) =
          Seq(NotificationsStyles.notification.htmlClass, level match {
            case Error   => NotificationsStyles.error.htmlClass
            case Alert   => NotificationsStyles.alert.htmlClass
            case Success => NotificationsStyles.success.htmlClass
            case Info    => NotificationsStyles.info.htmlClass
          }).mkString(" ")

        <.ul(^.className := RowRulesStyles.centeredRow)(self.state.notifications.map(notification => {
          <.li(^.className := Seq(NotificationsStyles.item, ColRulesStyles.col))(
            <.div(^.className := NotificationClasses(notification.level))(
              <.button(
                ^.className := NotificationsStyles.closeButton,
                ^.onClick := closeNotification(notification.identifier)
              )(
                <("svg")(
                  ^("xmlns") := "http://www.w3.org/2000/svg",
                  ^("x") := "0px",
                  ^("y") := "0px",
                  ^("width") := "20",
                  ^("height") := "20",
                  ^("viewBox") := "0 0 25 25"
                )(
                  <("path")(
                    ^("d") := "M12.5,9.3L3.9,0.7l0,0c-0.3-0.3-0.8-0.3-1.1,0L0.7,2.9c-0.3,0.3-0.3,0.8,0,1.1l8.6,8.6l-8.6,8.6 c-0.3,0.3-0.3,0.8,0,1.1l2.1,2.1c0.3,0.3,0.8,0.3,1.1,0l8.6-8.6l8.6,8.6c0.3,0.3,0.8,0.3,1.1,0l2.1-2.1c0.3-0.3,0.3-0.8,0-1.1 l-8.6-8.6l8.6-8.6l0,0c0.3-0.3,0.3-0.8,0-1.1l-2.1-2.1c-0.3-0.3-0.8-0.3-1.1,0L12.5,9.3z"
                  )()
                )
              ),
              <.p(^.className := TextStyles.smallText, ^.dangerouslySetInnerHTML := notification.message)()
            )
          )
        }), <.style()(NotificationsStyles.render[String]))

      }
    )
}

object NotificationsStyles extends StyleSheet.Inline {

  import dsl._

  val item: StyleA = style(margin :=! s"${ThemeStyles.SpacingValue.smaller.pxToEm().value} 0 0")

  val notification: StyleA =
    style(
      position.relative,
      minHeight(ThemeStyles.SpacingValue.large.pxToEm()),
      padding :=! s"${ThemeStyles.SpacingValue.small.pxToEm().value} ${ThemeStyles.SpacingValue.large
        .pxToEm()
        .value} ${ThemeStyles.SpacingValue.small.pxToEm().value} ${ThemeStyles.SpacingValue.small.pxToEm().value}",
      borderRadius(10.pxToEm()),
      backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
      overflow.hidden,
      boxShadow := "0 2px 4px 0 rgba(0,0,0,0.50)"
    )
  val error: StyleA = style(backgroundColor(rgb(249, 223, 227)))
  val alert: StyleA = style(backgroundColor(rgb(249, 223, 227)))
  val success: StyleA = style()
  val info: StyleA = style()

  val closeButton: StyleA = style(
    position.absolute,
    top(ThemeStyles.SpacingValue.small.pxToEm()),
    right(ThemeStyles.SpacingValue.small.pxToEm()),
    unsafeChild("svg")(verticalAlign.bottom, opacity(0.1), transition := "opacity .2s ease-in-out"),
    (&.hover)(unsafeChild("svg")(opacity(0.3)))
  )

}
