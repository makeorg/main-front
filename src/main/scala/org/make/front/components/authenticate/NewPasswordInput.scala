package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.styles._
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scalacss.internal.StyleA
import scalacss.internal.mutable.StyleSheet

object NewPasswordInput {

  final case class NewPasswordInputProps(value: String = "",
                                         placeHolder: String = "",
                                         className: String = "",
                                         required: Boolean = false,
                                         onChange: (FormSyntheticEvent[HTMLInputElement]) => Unit = (_) => {})
  final case class NewPasswordInputState(passwordInputType: String)

  val reactClass: ReactClass =
    React.createClass[NewPasswordInputProps, NewPasswordInputState](
      displayName = "NewPasswordInput",
      getInitialState = { _ =>
        NewPasswordInputState("password")

      },
      render = { self =>
        val toggleHidePassword: (SyntheticEvent) => Unit = { event =>
          event.preventDefault()
          self.setState { state =>
            val newType = if (state.passwordInputType == "password") "text" else "password"
            state.copy(passwordInputType = newType)
          }
        }

        val props = self.props.wrapped

        <.label(^.className := Seq(NewPasswordInputStyles.withIconWrapper, InputStyles.wrapper, InputStyles.withIcon))(
          <.span(^.className := NewPasswordInputStyles.innerWrapper)(
            <.span(^.className := NewPasswordInputStyles.inputWrapper)(
              <.input(
                ^.required := props.required,
                ^.`type` := self.state.passwordInputType,
                ^.value := props.value,
                ^.placeholder := props.placeHolder,
                ^.className := props.className,
                ^.onChange := props.onChange
              )()
            ),
            <.span(^.className := NewPasswordInputStyles.switchInputTypeButtonWrapper)(
              <.button(
                ^.className := Seq(
                  NewPasswordInputStyles.switchInputTypeButton(self.state.passwordInputType == "password")
                ),
                ^.onClick := toggleHidePassword
              )()
            )
          ),
          <.style()(NewPasswordInputStyles.render[String])
        )
      }
    )
}

object NewPasswordInputStyles extends StyleSheet.Inline {
  import dsl._

  val withIconWrapper: StyleA =
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), (&.before)(content := "'\\f023'"))

  val innerWrapper: StyleA =
    style(display.table, width(100.%%))

  val inputWrapper: StyleA =
    style(display.tableCell, width(100.%%))

  val switchInputTypeButtonWrapper: StyleA =
    style(display.tableCell, paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val switchInputTypeButton: (Boolean) => StyleA = styleF.bool(
    typePassword =>
      if (typePassword) {
        styleS(
          (&.hover)(cursor.pointer),
          addClassName(FontAwesomeStyles.eyeSlash.htmlClass),
          addClassName(FontAwesomeStyles.fa.htmlClass),
          color(ThemeStyles.TextColor.lighter)
        )
      } else {
        styleS(
          (&.hover)(cursor.pointer),
          addClassName(FontAwesomeStyles.eye.htmlClass),
          addClassName(FontAwesomeStyles.fa.htmlClass)
        )
    }
  )
}