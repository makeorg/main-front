package org.make.front.components.notifications

import javax.swing.LayoutStyle

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.components.Components._
import org.make.front.models.{Notification => NotificationModel, NotificationLevel => NotificationLevelModel}
import NotificationLevelModel._
import org.make.front.styles.{LayoutRulesStyles, TextStyles, ThemeStyles}

import scalacss.DevDefaults._
import scalacss.internal.Length
import scalacss.internal.mutable.StyleSheet

object Notifications {
  case class NotificationsProps(notifications: Seq[NotificationModel])

  case class NotificationsState()

  lazy val reactClass: ReactClass =
    React.createClass[NotificationsProps, NotificationsState](
      getInitialState = { self =>
        NotificationsState( /*isNotificationOpened = self.state.isNotificationOpened*/ )
      },
      render = (self) => {

        val closeNotification: (MouseSyntheticEvent) => Unit = { event =>
          event.preventDefault()
        //self.setState(state => state.copy(isNotificationOpened = true))
        }

        def NotificationClasses(level: NotificationLevelModel) =
          Seq(NotificationsStyles.notification.htmlClass, level match {
            case Error   => NotificationsStyles.error.htmlClass
            case Alert   => NotificationsStyles.alert.htmlClass
            case Success => NotificationsStyles.success.htmlClass
            case Info    => NotificationsStyles.info.htmlClass
          }).mkString(" ")
        <.div(^.className := Seq(NotificationsStyles.wrapper))(
          <.ul(^.className := Seq(NotificationsStyles.list, LayoutRulesStyles.centeredRow))(
            self.props.wrapped.notifications.map(notification => {
              <.li(^.className := Seq(NotificationsStyles.item, LayoutRulesStyles.col))(
                <.div(^.className := NotificationClasses(notification.level))(
                  <.button(
                    ^.className := NotificationsStyles.closeModalButton,
                    ^.key := notification.identifier.toString,
                    ^.id := notification.identifier.toString,
                    ^.onClick := closeNotification
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
                  <.p(^.className := TextStyles.smallText)(notification.message)
                )
              )
            })
          ),
          <.style()(NotificationsStyles.render[String])
        )
      }
    )
}

object NotificationsStyles extends StyleSheet.Inline {

  import dsl._

  //TODO: globalize function
  implicit class NormalizedSize(val baseSize: Int) extends AnyVal {
    def pxToEm(browserContextSize: Int = 16): Length[Double] = {
      (baseSize.toFloat / browserContextSize.toFloat).em
    }
  }

  val wrapper: StyleA = style(
    position.fixed,
    zIndex(1),
    top((50).pxToEm()), // TODO: dynamise calcul, if main intro is first child of page
    ThemeStyles.MediaQueries.beyondSmall(top((80).pxToEm())),
    left(`0`),
    width(100.%%)
  )

  val list: StyleA = style()

  val item: StyleA = style(margin := s"${ThemeStyles.SpacingValue.small.pxToEm().value} 0")

  val notification: StyleA =
    style(
      position.relative,
      minHeight(ThemeStyles.SpacingValue.large.pxToEm()),
      padding := s"${ThemeStyles.SpacingValue.small.pxToEm().value} ${ThemeStyles.SpacingValue.large
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

  val closeModalButton = style(
    position.absolute,
    top(ThemeStyles.SpacingValue.small.pxToEm()),
    right(ThemeStyles.SpacingValue.small.pxToEm()),
    unsafeChild("svg")(verticalAlign.bottom, opacity(0.1), transition := "opacity .2s ease-in-out"),
    (&.hover)(unsafeChild("svg")(opacity(0.3)))
  )

}
