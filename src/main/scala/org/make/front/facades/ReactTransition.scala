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
