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
import org.scalajs.dom

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
    lazy val place = PlaceTypeAttribute("place")
    lazy val `type` = TipTypeAttribute("type")
    lazy val effect = EffectAttribute("effect")
    lazy val offset = DataOffsetAttribute("offset")
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
    lazy val afterShow = AfterShowAttribute("afterShow")
    lazy val afterHide = AfterHideAttribute("afterHide")
    lazy val disable = NativeBooleanAttribute("disable")
    lazy val scrollHide = NativeBooleanAttribute("scrollHide")
    lazy val resizeHide = NativeBooleanAttribute("resizeHide")
    lazy val wrapper = WrapperAttribute("wrapper")
  }

  type AfterShow = js.Function0[Unit]
  type AfterHide = js.Function0[Unit]
  type GetContent = js.Function0[String]

  sealed trait WrapperType {
    def name: String
  }
  case object DivWrapper extends WrapperType {
    override val name: String = "div"
  }
  case object SpanWrapper extends WrapperType {
    override val name: String = "span"
  }

  sealed trait EffectType {
    def name: String
  }
  case object SolidEffect extends EffectType {
    override val name: String = "solid"
  }
  case object FloatEffect extends EffectType {
    override val name: String = "float"
  }

  sealed trait TipType {
    def name: String
  }
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

  sealed trait PlaceType {
    def name: String
  }
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

  case class AfterShowAttribute(name: String) extends AttributeSpec {
    def :=(afterShow: () => Unit): Attribute[AfterShow] =
      Attribute(name = name, value = afterShow, AS_IS)
  }
  case class AfterHideAttribute(name: String) extends AttributeSpec {
    def :=(afterHide: () => Unit): Attribute[AfterHide] =
      Attribute(name = name, value = afterHide, AS_IS)
  }
  case class DataOffsetAttribute(name: String) extends AttributeSpec {
    def :=(dataOffset: DataOffset): Attribute[DataOffset] =
      Attribute(name = name, value = dataOffset, AS_IS)
  }
  case class WrapperAttribute(name: String) extends AttributeSpec {
    def :=(wrapperType: WrapperType): Attribute[String] =
      Attribute(name = name, value = wrapperType.name)
  }
  case class EffectAttribute(name: String) extends AttributeSpec {
    def :=(effectType: EffectType): Attribute[String] =
      Attribute(name = name, value = effectType.name)
  }
  case class TipTypeAttribute(name: String) extends AttributeSpec {
    def :=(tipType: TipType): Attribute[String] =
      Attribute(name = name, value = tipType.name)
  }
  case class PlaceTypeAttribute(name: String) extends AttributeSpec {
    def :=(placeType: PlaceType): Attribute[String] =
      Attribute(name = name, value = placeType.name)
  }
  case class GetContentAttribute(name: String) extends AttributeSpec {
    def :=(getContent: () => String): Attribute[GetContent] =
      Attribute(name = name, value = getContent, AS_IS)
    def :=(getContents: Seq[() => String]): Attribute[js.Array[GetContent]] =
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
