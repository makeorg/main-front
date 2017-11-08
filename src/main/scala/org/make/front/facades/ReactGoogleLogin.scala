package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags._
import org.scalajs.dom.experimental.Response

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-google-login", JSImport.Default)
object NativeReactGoogleLogin extends ReactClass

object ReactGoogleLogin {

  @js.native
  trait Profile extends js.Object {
    def email: String
    def familyName: String
    def givenName: String
    def googleId: String
    def imageUrl: String
    def name: String
  }

  @js.native
  trait GoogleAuthResponse extends js.Object {
    def accessToken: String
    def tokenObj: String
    def googleId: String
    def profileObj: Profile
    def tokenId: String
  }

  implicit class ReactGoogleLoginVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactGoogleLogin: ReactClassElementSpec = elements(NativeReactGoogleLogin)
  }

  implicit class ReactGoogleLoginVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val clientID: StringAttributeSpec = StringAttributeSpec("clientId")
    lazy val scope: StringAttributeSpec = StringAttributeSpec("scope")
    lazy val onSuccess: NativeFunction1Attribute[Response, Unit] = NativeFunction1Attribute("onSuccess")
    lazy val onFailure: NativeFunction1Attribute[Response, Unit] = NativeFunction1Attribute("onFailure")
    lazy val isSignIn: TrueOrFalseAttributeSpec = TrueOrFalseAttributeSpec("isSignIn")
    lazy val buttonText: StringAttributeSpec = StringAttributeSpec("buttonText")
    lazy val className: SpaceSeparatedStringAttributeSpec = SpaceSeparatedStringAttributeSpec("className")
  }

}
