package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.StringAttributeSpec

import scala.scalajs.js.annotation.JSImport
import scalajs.js

@js.native
@JSImport("react-infinite-scroller", JSImport.Default)
object NativeReactInfiniteScroller extends ReactClass

object ReactInfiniteScroller {
  implicit class ReactInfiniteScrollerVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val InfiniteScroll: ReactClassElementSpec = elements(NativeReactInfiniteScroller)
  }
  implicit class ReactInfiniteScrollerVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val element: StringAttributeSpec = StringAttributeSpec("element")
    lazy val hasMore: NativeBooleanAttribute = NativeBooleanAttribute("hasMore")
    lazy val initialLoad: NativeBooleanAttribute = NativeBooleanAttribute("initialLoad")
    lazy val isReverse: NativeBooleanAttribute = NativeBooleanAttribute("isReverse")
    lazy val pageStart: NativeIntAttribute = NativeIntAttribute("pageStart")
    lazy val threshold: NativeIntAttribute = NativeIntAttribute("threshold")
    lazy val useCapture: NativeBooleanAttribute = NativeBooleanAttribute("useCapture")
    lazy val useWindow: NativeBooleanAttribute = NativeBooleanAttribute("useWindow")
    lazy val loader: NativeReactElementAttribute = NativeReactElementAttribute("loader")
    lazy val loadMore: NativeFunction0Attribute[Unit] = NativeFunction0Attribute[Unit]("loadMore")
  }
}
