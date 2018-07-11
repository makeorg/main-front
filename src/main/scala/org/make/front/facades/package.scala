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

package org.make.front

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.{Attribute, AttributeSpec}

import scala.scalajs.js

package object facades {

  case class NativeIntAttribute(name: String) extends AttributeSpec {
    def :=(value: Int): Attribute[Int] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeFloatAttribute(name: String) extends AttributeSpec {
    def :=(value: Float): Attribute[Float] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeBooleanAttribute(name: String) extends AttributeSpec {
    def :=(value: Boolean): Attribute[Boolean] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeFunction0Attribute[RETURN](name: String) extends AttributeSpec {
    def :=(value: () => RETURN): Attribute[js.Function0[RETURN]] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeFunction1Attribute[ARG0, RETURN](name: String) extends AttributeSpec {
    def :=(value: (ARG0) => RETURN): Attribute[js.Function1[ARG0, RETURN]] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeFunction2Attribute[ARG0, ARG1, RETURN](name: String) extends AttributeSpec {
    def :=(value: (ARG0, ARG1) => RETURN): Attribute[js.Function2[ARG0, ARG1, RETURN]] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeFunction3Attribute[ARG0, ARG1, ARG2, RETURN](name: String) extends AttributeSpec {
    def :=(value: (ARG0, ARG1, ARG2) => RETURN): Attribute[js.Function3[ARG0, ARG1, ARG2, RETURN]] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeArrayAttribute(name: String) extends AttributeSpec {
    def :=(value: js.Array[js.Object]): Attribute[js.Array[js.Object]] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeReactElementAttribute(name: String) extends AttributeSpec {
    def :=(value: ReactElement): Attribute[ReactElement] =
      Attribute(name = name, value = value, AS_IS)
    def :=(value: ReactClass): Attribute[ReactClass] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeJsObjectAttributeSpec[T <: js.Any](name: String) extends AttributeSpec {
    def :=(value: T): Attribute[T] =
      Attribute(name = name, value = value, AS_IS)
  }

  implicit class ExtraReactVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val componentElement: NativeFunction0Attribute[ReactElement] = NativeFunction0Attribute("component")
  }
}
