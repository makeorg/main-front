package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js

@js.native
@JSImport("react-textarea-autosize", JSImport.Default)
object NativeReactTextareaAutosize extends ReactClass

object ReactTextareaAutosize {
  implicit class ReactTooltipVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val TextareaAutosize: ReactClassElementSpec = elements(NativeReactTextareaAutosize)
  }
}
