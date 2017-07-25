package org.make.front

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.statictags.{Attribute, AttributeSpec}

package object facades {

  case class NativeIntAttribute(name: String) extends AttributeSpec {
    def :=(value: Int): Attribute[Int] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeBooleanAttribute(name: String) extends AttributeSpec {
    def :=(value: Boolean): Attribute[Boolean] =
      Attribute(name = name, value = value, AS_IS)
  }

}
