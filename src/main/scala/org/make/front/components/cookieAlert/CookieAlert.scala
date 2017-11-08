package org.make.front.components.cookieAlert

import java.util.UUID

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.facades.I18n
import org.make.front.middlewares.CookieAlertMiddleware
import org.make.front.middlewares.CookieAlertMiddleware.CookieAlertListener
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

object CookieAlert {

  final case class CookieAlertProps(isAlertOpened: Boolean, closeCallback: () => Unit)

  final case class CookieAlertState(id: String, isAlertOpened: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[CookieAlertProps, CookieAlertState](
        displayName = "cookieAlert",
        getInitialState = { _ =>
          CookieAlertState(UUID.randomUUID().toString, isAlertOpened = true)
        },
        componentDidMount = { self =>
          val onDismissCookieAlert: () => Unit = { () =>
            self.setState(_.copy(isAlertOpened = false))
          }

          CookieAlertMiddleware
            .addCookieAlertListener(self.state.id, CookieAlertListener(onDismissCookieAlert))
        },
        componentWillUnmount = { self =>
          CookieAlertMiddleware.removeCookieAlertListener(self.state.id)
        },
        componentWillReceiveProps = { (self, props) =>
          self.setState(_.copy(isAlertOpened = props.wrapped.isAlertOpened))
        },
        render = (self) => {

          def close(): (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(_.copy(isAlertOpened = false))
            self.props.wrapped.closeCallback()
          }

          if (self.state.isAlertOpened) {
            <.div(^.className := Seq(CookieAlertStyles.wrapper))(
              <.div(^.className := Seq(RowRulesStyles.centeredRow, CookieAlertStyles.innerWrapper))(
                <.div(^.className := ColRulesStyles.col)(
                  <.button(^.className := CookieAlertStyles.closeButton, ^.onClick := close())(
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
                  <.p(
                    ^.className := Seq(TextStyles.smallText, CookieAlertStyles.message),
                    ^.dangerouslySetInnerHTML := I18n.t("cookie-alert")
                  )()
                )
              ),
              <.style()(CookieAlertStyles.render[String])
            )
          } else { <.div()() }
        }
      )
}

object CookieAlertStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA = style(
    backgroundColor(ThemeStyles.BackgroundColor.lightGrey),
    paddingTop(ThemeStyles.SpacingValue.small.pxToEm()),
    paddingBottom(ThemeStyles.SpacingValue.small.pxToEm())
  )

  val innerWrapper: StyleA = style(position.static)

  val closeButton: StyleA = style(
    position.absolute,
    top(ThemeStyles.SpacingValue.small.pxToEm()),
    right(ThemeStyles.SpacingValue.small.pxToEm()),
    unsafeChild("svg")(verticalAlign.bottom, opacity(0.1), transition := "opacity .2s ease-in-out"),
    (&.hover)(unsafeChild("svg")(opacity(0.3)))
  )

  val message: StyleA = style(
    unsafeChild("a")(color(ThemeStyles.ThemeColor.primary)),
    paddingRight(20.pxToEm(13)),
    ThemeStyles.MediaQueries.beyondSmall(paddingRight(20.pxToEm(16))),
    ThemeStyles.MediaQueries.beyondLarge(paddingRight.initial)
  )
}
