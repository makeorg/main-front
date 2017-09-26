package org.make.front

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

  case class NativeBooleanAttribute(name: String) extends AttributeSpec {
    def :=(value: Boolean): Attribute[Boolean] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeFunction0Attribute[T](name: String) extends AttributeSpec {
    def :=(value: () => T): Attribute[js.Function0[T]] =
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

  case class NativeFloatAttribute(name: String) extends AttributeSpec {
    def :=(value: Float): Attribute[Float] =
      Attribute(name = name, value = value, AS_IS)
  }

}
