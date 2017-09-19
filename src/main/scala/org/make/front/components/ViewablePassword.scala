package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.front.components.Components._
import org.make.front.styles.{FontAwesomeStyles, MakeStyles}
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

import org.make.front.Main.CssSettings._

object ViewablePassword {

  final case class ViewablePasswordProps(value: String = "",
                                         placeHolder: String = "",
                                         className: String = "",
                                         required: Boolean = false,
                                         onChange: (FormSyntheticEvent[HTMLInputElement]) => Unit = (_) => {})
  final case class ViewablePasswordState(displayType: String)

  val reactClass: ReactClass =
    React.createClass[ViewablePasswordProps, ViewablePasswordState](
      getInitialState = { _ =>
        ViewablePasswordState("password")

      },
      render = { self =>
        val toggleHidePassword: (SyntheticEvent) => Unit = { event =>
          event.preventDefault()
          self.setState { state =>
            val newType = if (state.displayType == "password") "input" else "password"
            state.copy(displayType = newType)
          }
        }

        val props = self.props.wrapped

        <.div()(
          <.i(
            ^.className := Seq(
              ViewablePasswordStyles.eye(self.state.displayType == "password"),
              MakeStyles.Form.inputIconLeft
            ),
            ^.onClick := toggleHidePassword
          )(),
          <.input(
            ^.required := props.required,
            ^.`type` := self.state.displayType,
            ^.value := props.value,
            ^.placeholder := props.placeHolder,
            ^.className := props.className,
            ^.onChange := props.onChange
          )(),
          <.style()(ViewablePasswordStyles.render[String])
        )
      }
    )

  object ViewablePasswordStyles extends StyleSheet.Inline {
    import dsl._

    val eye: (Boolean) => StyleA = styleF.bool(
      typePassword =>
        if (typePassword) {
          styleS(
            (&.hover)(cursor.pointer),
            addClassName(FontAwesomeStyles.eyeSlash.htmlClass),
            color(MakeStyles.Color.grey)
          )
        } else {
          styleS((&.hover)(cursor.pointer), addClassName(FontAwesomeStyles.eye.htmlClass))
      }
    )

  }

}
