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

package org.make.front.components.spinner

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._

object Spinner {

  val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](
        displayName = "Spinner",
        render = (self) => {
          <.span(^.className := SpinnerStyles.wrapper)(
            <("svg")(
              ^("xmlns") := "http://www.w3.org/2000/svg",
              ^("x") := "0",
              ^("y") := "0",
              ^("width") := "40",
              ^("height") := "40",
              ^("viewBox") := "0 0 66 66",
              ^.className := SpinnerStyles.shape
            )(
              <("linearGradient")(^("id") := "gradient")(
                <("stop")(^("offset") := "0", ^.style := Map("stopColor" -> "#000000", "stopOpacity" -> 0))(),
                <("stop")(^("offset") := "1", ^.style := Map("stopColor" -> "#000000", "stopOpacity" -> 1))()
              ),
              <("path")(
                ^("fill") := "url(#gradient)",
                ^("d") := "M0,33C0,14.8,14.8,0,33,0s33,14.8,33,33S51.2,66,33,66S0,51.2,0,33z M6,33c0,14.9,12.1,27,27,27s27-12.1,27-27 S47.9,6,33,6S6,18.1,6,33z"
              )()
            ),
            <.style()(SpinnerStyles.render[String])
          )
        }
      )
}

object SpinnerStyles extends StyleSheet.Inline {

  import dsl._

  val rotate =
    keyframes(0.%% -> keyframe(transform := s"rotate(0deg)"), 100.%% -> keyframe(transform := s"rotate(360deg)"))

  val wrapper: StyleA =
    style(display.block, textAlign.center)

  val shape: StyleA = style(
    position.relative,
    zIndex(1),
    opacity(0.3),
    animationName(rotate),
    animationTimingFunction.linear,
    animationDuration :=! s"0.5s",
    animationIterationCount.infinite
  )

}
