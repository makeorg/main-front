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

package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport(module = "react-transition-group", name = "Transition")
object NativeReactTransition extends ReactClass

object ReactTransition {

  implicit class ReactTransitionVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Transition: ReactClassElementSpec = elements(NativeReactTransition)
  }

  implicit class ReactTransitionDOMAttributes(attributes: VirtualDOMAttributes) {
    val in: NativeBooleanAttribute =
      NativeBooleanAttribute("in")
    val mountOnEnter: NativeBooleanAttribute =
      NativeBooleanAttribute("mountOnEnter")
    val unmountOnExit: NativeBooleanAttribute =
      NativeBooleanAttribute("unmountOnExit")
    val appear: NativeBooleanAttribute =
      NativeBooleanAttribute("appear")
    val enter: NativeBooleanAttribute =
      NativeBooleanAttribute("enter")
    val exit: NativeBooleanAttribute =
      NativeBooleanAttribute("exit")
    val timeout: NativeIntAttribute =
      NativeIntAttribute("timeout")
    val onTransitionEnter: NativeFunction2Attribute[HTMLElement, Boolean, Unit] =
      NativeFunction2Attribute[HTMLElement, Boolean, Unit]("onEnter")
    val onTransitionEntering: NativeFunction2Attribute[HTMLElement, Boolean, Unit] =
      NativeFunction2Attribute[HTMLElement, Boolean, Unit]("onEntering")
    val onTransitionEntered: NativeFunction2Attribute[HTMLElement, Boolean, Unit] =
      NativeFunction2Attribute[HTMLElement, Boolean, Unit]("onEntered")
    val onTransitionExit: NativeFunction1Attribute[HTMLElement, Unit] =
      NativeFunction1Attribute[HTMLElement, Unit]("onExit")
    val onTransitionExiting: NativeFunction1Attribute[HTMLElement, Unit] =
      NativeFunction1Attribute[HTMLElement, Unit]("onExiting")
    val onTransitionExited: NativeFunction1Attribute[HTMLElement, Unit] =
      NativeFunction1Attribute[HTMLElement, Unit]("onExited")
    val addEndListener: NativeFunction2Attribute[HTMLElement, js.Function0[Unit], Unit] =
      NativeFunction2Attribute[HTMLElement, js.Function0[Unit], Unit]("addEndListener")

  }

}
