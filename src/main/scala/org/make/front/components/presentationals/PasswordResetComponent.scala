package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.styles.MakeStyles
import org.make.front.facades.Translate.TranslateVirtualDOMElements

import scalacss.DevDefaults._
import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object PasswordResetComponent {

  case class PasswordResetProps()
  case class PasswordResetState()

  lazy val reactClass: ReactClass =
    React.createClass[PasswordResetProps, PasswordResetState](
      getInitialState = { self =>
        PasswordResetState()
      },
      render = self => {
        <.div(^.className := PasswordResetComponentStyles.container)(
          <.div(^.className := PasswordResetComponentStyles.content)(
            <.Translate(^.className := MakeStyles.Modal.title, ^.value := { "form.passwordReset.title" })()
          ),

          <.style()(PasswordResetComponentStyles.render[String])
        )
      }
    )
}

object PasswordResetComponentStyles extends StyleSheet.Inline {
  import dsl._

  val container: StyleA = style(
    paddingTop(7.rem),
    paddingBottom(7.rem)
  )

  val content: StyleA = style(
    width(100%%),
    backgroundColor(MakeStyles.Color.white),
    maxWidth(114.rem),
    marginRight.auto,
    marginLeft.auto,
    width(100.%%),
    height(100.%%)
  )

}
