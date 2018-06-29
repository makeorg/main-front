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

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMAttributes.Type.AS_IS
import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.statictags.{Attribute, AttributeSpec, StringAttributeSpec}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.JSImport

/**
  * Facade for react-tooltip
  * @deprecated Simple css tooltip is used at the moment due to static methods not working yet
  */
@js.native
@JSImport("react-tooltip", "default")
object NativeReactTooltip extends ReactClass {
  def show(target: js.Any): Unit = js.native
  def hide(target: js.Any): Unit = js.native
  def rebuild(): Unit = js.native
}

object ReactTooltip {

  def show(target: js.Any): Unit = NativeReactTooltip.show(target)
  def hide(target: js.Any): Unit = NativeReactTooltip.show(target)
  def rebuild(): Unit = NativeReactTooltip.rebuild()

  implicit class ReactTooltipVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val ReactTooltip: ReactClassElementSpec = elements(NativeReactTooltip)
  }

  implicit class ReactTooltipVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val dataFor = StringAttributeSpec("data-for")
    lazy val dataTip = StringAttributeSpec("data-tip")
    lazy val children = NativeReactElementAttribute("children")
    lazy val place: TooltipEnumAttribute[PlaceType] = TooltipEnumAttribute("place")
    lazy val `type`: TooltipEnumAttribute[TipType] = TooltipEnumAttribute("type")
    lazy val effect: TooltipEnumAttribute[EffectType] = TooltipEnumAttribute("effect")
    lazy val offset: NativeJsObjectAttributeSpec[DataOffset] = NativeJsObjectAttributeSpec("offset")
    lazy val multiline = NativeBooleanAttribute("multiline")
    lazy val border = NativeBooleanAttribute("border")
    lazy val insecure = NativeBooleanAttribute("insecure")
    lazy val className = StringAttributeSpec("className")
    lazy val id = StringAttributeSpec("id")
    lazy val html = NativeBooleanAttribute("html")
    lazy val delayHide = NativeFloatAttribute("delayHide")
    lazy val delayShow = NativeFloatAttribute("delayShow")
    lazy val event = StringAttributeSpec("event")
    lazy val eventOff = StringAttributeSpec("eventOff")
    lazy val watchWindow = NativeBooleanAttribute("watchWindow")
    lazy val isCapture = NativeBooleanAttribute("isCapture")
    lazy val globalEventOff = StringAttributeSpec("globalEventOff")
    lazy val getContent = GetContentAttribute("getContent")
    lazy val afterShow: NativeFunction0Attribute[Unit] = NativeFunction0Attribute("afterShow")
    lazy val afterHide: NativeFunction0Attribute[Unit] = NativeFunction0Attribute("afterHide")
    lazy val disable = NativeBooleanAttribute("disable")
    lazy val scrollHide = NativeBooleanAttribute("scrollHide")
    lazy val resizeHide = NativeBooleanAttribute("resizeHide")
    lazy val wrapper: TooltipEnumAttribute[WrapperType] = TooltipEnumAttribute("wrapper")
  }

  sealed trait TooltipEnum {
    def name: String
  }

  sealed trait WrapperType extends TooltipEnum

  object WrapperType {
    case object DivWrapper extends WrapperType {
      override val name: String = "div"
    }
    case object SpanWrapper extends WrapperType {
      override val name: String = "span"
    }
  }

  sealed trait EffectType extends TooltipEnum

  object EffectType {
    case object SolidEffect extends EffectType {
      override val name: String = "solid"
    }
    case object FloatEffect extends EffectType {
      override val name: String = "float"
    }
  }

  sealed trait TipType extends TooltipEnum

  object TipType {
    case object SuccessTipType extends TipType {
      override val name: String = "success"
    }
    case object WarningTipType extends TipType {
      override val name: String = "warning"
    }
    case object ErrorTipType extends TipType {
      override val name: String = "error"
    }
    case object InfoTipType extends TipType {
      override val name: String = "info"
    }
    case object LightTipType extends TipType {
      override val name: String = "light"
    }
    case object DarkTipType extends TipType {
      override val name: String = "dark"
    }
  }

  sealed trait PlaceType extends TooltipEnum

  object PlaceType {
    case object TopPlace extends PlaceType {
      override val name: String = "top"
    }
    case object RightPlace extends PlaceType {
      override val name: String = "right"
    }
    case object LeftPlace extends PlaceType {
      override val name: String = "left"
    }
    case object BottomPlace extends PlaceType {
      override val name: String = "bottom"
    }
  }

  case class TooltipEnumAttribute[T <: TooltipEnum](name: String) extends AttributeSpec {
    def :=(value: T): Attribute[String] =
      Attribute(name = name, value = value.name)
  }

  case class GetContentAttribute(name: String) extends AttributeSpec {
    def :=(getContent: () => String): Attribute[js.Function0[String]] =
      Attribute(name = name, value = getContent, AS_IS)
    def :=(getContents: js.Array[() => String]): Attribute[js.Array[js.Function0[String]]] =
      Attribute(name = name, value = getContents.map(_.asInstanceOf[js.Function0[String]]).toJSArray, AS_IS)
  }

  @js.native
  trait DataOffset extends js.Object {
    def top: UndefOr[Int]
    def left: UndefOr[Int]
    def bottom: UndefOr[Int]
    def right: UndefOr[Int]
  }

  object DataOffset {
    def apply(top: Option[Int] = None,
              left: Option[Int] = None,
              bottom: Option[Int] = None,
              right: Option[Int] = None): DataOffset = {
      js.Dynamic
        .literal(top = top.orUndefined, left = left.orUndefined, right = right.orUndefined, bottom.orUndefined)
        .asInstanceOf[DataOffset]
    }
  }
}
