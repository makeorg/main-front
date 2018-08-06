/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, SyntheticEvent}
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.styles._
import org.make.front.styles.base.TableLayoutStyles
import org.make.front.styles.ui.InputStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

object NewPasswordInput {

  final case class NewPasswordInputProps(
    id: String = "",
    htmlClass: js.Array[StyleA] =
      js.Array(NewPasswordInputStyles.withIconWrapper, InputStyles.wrapper, InputStyles.withIcon),
    value: String = "",
    placeHolder: String = "",
    className: String = "",
    required: Boolean = false,
    onChange: (FormSyntheticEvent[HTMLInputElement]) => Unit = (_) => {}
  )
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

        <.label(^.className := props.htmlClass)(
          <.span(^.className := TableLayoutStyles.wrapper)(
            <.span(^.className := js.Array(TableLayoutStyles.cell, NewPasswordInputStyles.inputWrapper))(
              <.input(
                ^.id := props.id,
                ^.required := props.required,
                ^.`type` := self.state.passwordInputType,
                ^.value := props.value,
                ^.placeholder := props.placeHolder,
                ^.className := props.className,
                ^.onChange := props.onChange
              )()
            ),
            <.span(
              ^.className := js.Array(TableLayoutStyles.cell, NewPasswordInputStyles.switchInputTypeButtonWrapper)
            )(
              <.button(
                ^.className := js
                  .Array(NewPasswordInputStyles.switchInputTypeButton(self.state.passwordInputType == "password")),
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
    style(backgroundColor(ThemeStyles.BackgroundColor.lightGrey), &.before(content := "'\\f023'"))

  val inputWrapper: StyleA =
    style(width(100.%%))

  val switchInputTypeButtonWrapper: StyleA =
    style(paddingLeft(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val switchInputTypeButton: (Boolean) => StyleA = styleF.bool(
    typePassword =>
      if (typePassword) {
        styleS(
          &.hover(cursor.pointer),
          addClassName(FontAwesomeStyles.eyeSlash.htmlClass),
          color(ThemeStyles.TextColor.lighter)
        )
      } else {
        styleS(&.hover(cursor.pointer), addClassName(FontAwesomeStyles.eye.htmlClass))
    }
  )
}
