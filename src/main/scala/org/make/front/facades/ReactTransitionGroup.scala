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
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.StringAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport(module = "react-transition-group", name = "TransitionGroup")
object NativeReactTransitionGroup extends ReactClass

object ReactTransitionGroup {

  implicit class ReactTransitionGroupVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TransitionGroup: ReactClassElementSpec = elements(NativeReactTransitionGroup)
  }

  implicit class ReactTransitionGroupDOMAttributes(attributes: VirtualDOMAttributes) {
    val component: StringAttributeSpec =
      StringAttributeSpec("component")
    val appear: NativeBooleanAttribute =
      NativeBooleanAttribute("appear")
    val enter: NativeBooleanAttribute =
      NativeBooleanAttribute("enter")
    val exit: NativeBooleanAttribute =
      NativeBooleanAttribute("exit")
    val childFactory: NativeFunction1Attribute[ReactElement, ReactElement] =
      NativeFunction1Attribute[ReactElement, ReactElement]("childFactory")
  }

}
