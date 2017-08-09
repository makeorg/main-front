package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{Attribute, AttributeSpec, SpaceSeparatedStringAttributeSpec, StringAttributeSpec}
import org.scalajs.dom.experimental.Response

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-facebook-login", JSImport.Default)
object NativeReactFacebookLogin extends ReactClass

object ReactFacebookLogin {

  type callback = js.Function1[Response, Unit]

  @js.native
  trait Data extends js.Object {
    def url: String
  }

  @js.native
  trait Picture extends js.Object {
    def data: Data
  }

  @js.native
  trait FacebookAuthResponse extends js.Object {
    def email: String
    def first_name: String
    def last_name: String
    def name: String
    def userId: String
    def picture: Picture
    def accessToken: String
  }

  case class callbackAttribute(name: String) extends AttributeSpec {
    def :=(callback: (Response) => Unit): Attribute[callback] =
      Attribute(name = name, value = callback, AS_IS)
  }

  implicit class ReactFacebookLoginVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactFacebookLogin: ReactClassElementSpec = elements(NativeReactFacebookLogin)
  }

  implicit class ReactFacebookLoginVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val appId: StringAttributeSpec = StringAttributeSpec("appId")
    lazy val scope: StringAttributeSpec = StringAttributeSpec("scope")
    lazy val fields: StringAttributeSpec = StringAttributeSpec("fields")
    lazy val callback: callbackAttribute = callbackAttribute("callback")
    lazy val cssClass: SpaceSeparatedStringAttributeSpec = SpaceSeparatedStringAttributeSpec("cssClass")
    lazy val iconClass: StringAttributeSpec = StringAttributeSpec("icon")
    lazy val iconComponent: NativeReactElementAttribute = NativeReactElementAttribute("icon")
    lazy val textButton: StringAttributeSpec = StringAttributeSpec("textButton")
  }
}
