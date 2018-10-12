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

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-share", "FacebookShareButton")
object NativeFacebookShareButton extends ReactClass

@js.native
@JSImport("react-share", "TwitterShareButton")
object NativeTwitterShareButton extends ReactClass

@js.native
@JSImport("react-share", "LinkedinShareButton")
object NativeLinkedinShareButton extends ReactClass

object ReactShare {

  implicit class ReactShareVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val FacebookShareButton: ReactClassElementSpec = elements(NativeFacebookShareButton)
    lazy val TwitterShareButton: ReactClassElementSpec = elements(NativeTwitterShareButton)
    lazy val LinkedinShareButton: ReactClassElementSpec = elements(NativeLinkedinShareButton)
  }

  implicit class ReactShareVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val url: StringAttributeSpec = StringAttributeSpec("url")
    lazy val quote: StringAttributeSpec = StringAttributeSpec("quote")
    lazy val beforeOnClick: NativeFunction0Attribute[Unit] = NativeFunction0Attribute("beforeOnClick")
    lazy val onShareWindowClose: NativeFunction0Attribute[Unit] = NativeFunction0Attribute("onShareWindowClose")
  }
}
