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
    lazy val loadMore: NativeFunction1Attribute[Int, Unit] = NativeFunction1Attribute[Int, Unit]("loadMore")
  }
}
