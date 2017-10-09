package org.make.front.components.cookieAlert

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.make.front.components.Components._
import org.make.front.styles._
import org.make.front.styles.base.{ColRulesStyles, RowRulesStyles, TextStyles}
import org.make.front.styles.utils._

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object CookieAlert {

  case class CookieAlertState(isAlertOpened: Boolean)

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, CookieAlertState](
        displayName = "cookieAlert",
        getInitialState = { _ =>
          CookieAlertState(isAlertOpened = true)
        },
        render = (self) => {
          def close(): (MouseSyntheticEvent) => Unit = { event =>
            event.preventDefault()
            self.setState(state => state.copy(isAlertOpened = false))
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
                    ^.dangerouslySetInnerHTML := "En poursuivant votre navigation sur notre site, vous acceptez l'installation et l'utilisation de cookies sur votre poste, dans le respect de notre <a href=\"\">politique de protection de votre vie privée</a>."
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
