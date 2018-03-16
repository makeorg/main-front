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
@JSImport("react-share", "GooglePlusShareButton")
object NativeGooglePlusShareButton extends ReactClass

@js.native
@JSImport("react-share", "LinkedinShareButton")
object NativeLinkedinShareButton extends ReactClass

object ReactShare {

  implicit class ReactShareVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val FacebookShareButton: ReactClassElementSpec = elements(NativeFacebookShareButton)
    lazy val TwitterShareButton: ReactClassElementSpec = elements(NativeTwitterShareButton)
    lazy val GooglePlusShareButton: ReactClassElementSpec = elements(NativeGooglePlusShareButton)
    lazy val LinkedinShareButton: ReactClassElementSpec = elements(NativeLinkedinShareButton)
  }

  implicit class ReactShareVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val url: StringAttributeSpec = StringAttributeSpec("url")
    lazy val quote: StringAttributeSpec = StringAttributeSpec("quote")
  }
}
