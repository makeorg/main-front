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

  case class NativeFunction0Attribute[T](name: String) extends AttributeSpec {
    def :=(value: () => T): Attribute[js.Function0[T]] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeFunction1Attribute[T, R](name: String) extends AttributeSpec {
    def :=(value: (T) => R): Attribute[js.Function1[T, R]] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeFunction2Attribute[T, U, R](name: String) extends AttributeSpec {
    def :=(value: (T, U) => R): Attribute[js.Function2[T, U, R]] =
      Attribute(name = name, value = value, AS_IS)
  }

  case class NativeFunction3Attribute[T, U, V, R](name: String) extends AttributeSpec {
    def :=(value: (T, U, V) => R): Attribute[js.Function3[T, U, V, R]] =
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
