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
