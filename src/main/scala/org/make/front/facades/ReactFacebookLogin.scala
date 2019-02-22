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
import io.github.shogowada.statictags.{BooleanAttributeSpec, SpaceSeparatedStringAttributeSpec, StringAttributeSpec}
import org.scalajs.dom.experimental.Response

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-facebook-login", JSImport.Default)
object NativeReactFacebookLogin extends ReactClass

object ReactFacebookLogin {

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

  implicit class ReactFacebookLoginVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactFacebookLogin: ReactClassElementSpec = elements(NativeReactFacebookLogin)
  }

  implicit class ReactFacebookLoginVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val appId: StringAttributeSpec = StringAttributeSpec("appId")
    lazy val scope: StringAttributeSpec = StringAttributeSpec("scope")
    lazy val fields: StringAttributeSpec = StringAttributeSpec("fields")
    lazy val callback: NativeFunction1Attribute[Response, Unit] = NativeFunction1Attribute("callback")
    lazy val cssClass: SpaceSeparatedStringAttributeSpec = SpaceSeparatedStringAttributeSpec("cssClass")
    lazy val iconElement: NativeReactElementAttribute = NativeReactElementAttribute("icon")
    lazy val iconClass: StringAttributeSpec = StringAttributeSpec("icon")
    lazy val iconComponent: NativeReactElementAttribute = NativeReactElementAttribute("icon")
    lazy val textButton: StringAttributeSpec = StringAttributeSpec("textButton")
    lazy val version: StringAttributeSpec = StringAttributeSpec("version")
    lazy val containerStyle: NativeJsObjectAttributeSpec[js.Dictionary[Any]] = NativeJsObjectAttributeSpec(
      "containerStyle"
    )
    lazy val disableMobileRedirect: BooleanAttributeSpec = BooleanAttributeSpec("disableMobileRedirect")
  }
}
