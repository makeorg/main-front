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
import io.github.shogowada.statictags.StringAttributeSpec
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport(module = "react-transition-group", name = "CSSTransition")
object NativeReactCSSTransition extends ReactClass

object ReactCSSTransition {

  implicit class ReactCSSTransitionVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val CSSTransition: ReactClassElementSpec = elements(NativeReactCSSTransition)
  }

  implicit class ReactCSSTransitionDOMAttributes(attributes: VirtualDOMAttributes) {
    val classNames: StringAttributeSpec = StringAttributeSpec("classNames")
    val classNamesMap: NativeJsObjectAttributeSpec[TransitionClasses] =
      NativeJsObjectAttributeSpec[TransitionClasses]("classNames")
    val onCSSTransitionEnter: NativeFunction2Attribute[HTMLElement, Boolean, Unit] =
      NativeFunction2Attribute[HTMLElement, Boolean, Unit]("onEnter")
    val onCSSTransitionEntering: NativeFunction2Attribute[HTMLElement, Boolean, Unit] =
      NativeFunction2Attribute[HTMLElement, Boolean, Unit]("onEntering")
    val onCSSTransitionEntered: NativeFunction2Attribute[HTMLElement, Boolean, Unit] =
      NativeFunction2Attribute[HTMLElement, Boolean, Unit]("onEntered")
    val onCSSTransitionExit: NativeFunction0Attribute[Unit] =
      NativeFunction0Attribute[Unit]("onExit")
    val onCSSTransitionExiting: NativeFunction0Attribute[Unit] =
      NativeFunction0Attribute[Unit]("onExiting")
    val onCSSTransitionExited: NativeFunction0Attribute[Unit] =
      NativeFunction0Attribute[Unit]("onExited")

  }

  @js.native
  trait TransitionClasses extends js.Object {
    val appear: String = js.native
    val appearActive: String = js.native
    val enter: String = js.native
    val enterActive: String = js.native
    val enterDone: String = js.native
    val exit: String = js.native
    val exitActive: String = js.native
    val exitDone: String = js.native
  }

  object TransitionClasses {
    def apply(appear: String = "",
              appearActive: String = "",
              enter: String = "",
              enterActive: String = "",
              enterDone: String = "",
              exit: String = "",
              exitActive: String = "",
              exitDone: String = ""): TransitionClasses = {
      js.Dynamic
        .literal(
          appearActive = appearActive,
          enter = enter,
          enterActive = enterActive,
          enterDone = enterDone,
          exit = exit,
          exitActive = exitActive,
          exitDone = exitDone
        )
        .asInstanceOf[TransitionClasses]
    }
  }

}
