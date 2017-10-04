package org.make.front.facades

import io.github.shogowada.scalajs.reactjs.VirtualDOM.VirtualDOMElements.ReactClassElementSpec
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{VirtualDOMAttributes, VirtualDOMElements}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.statictags.StringAttributeSpec

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.annotation.JSImport.Default
import scala.scalajs.js.|

@js.native
@JSImport("react-slick", Default)
object NativeReactSlick extends ReactClass

object ReactSlick {

  @js.native
  trait Slider extends js.Object {
    def slickNext(): Unit = js.native
    def slickPrev(): Unit = js.native
    def slickGoTo(slide: Int): Unit = js.native
  }

  @js.native
  trait ResponsiveSettings extends js.Object {
    def slidesToShow: Int
  }

  @js.native
  trait ResponsiveParameters extends js.Object {
    def breakpoint: Int
    def settings: String | ResponsiveSettings
  }

  object ResponsiveParameters {
    def unslick(breakpoint: Int): ResponsiveParameters = {
      js.Dynamic.literal(breakpoint = breakpoint, settings = "unslick").asInstanceOf[ResponsiveParameters]
    }

    def apply(breakpoint: Int, slidesToShow: Int): ResponsiveParameters = {
      val settings = js.Dynamic.literal(slidesToShow = slidesToShow).asInstanceOf[ResponsiveSettings]
      js.Dynamic.literal(breakpoint = breakpoint, settings = settings).asInstanceOf[ResponsiveParameters]
    }
  }

  implicit class ReactTooltipVirtualDOMElements(elements: VirtualDOMElements) {
    lazy val Slider: ReactClassElementSpec = elements(NativeReactSlick)
  }

  implicit class ReactTooltipVirtualDOMAttributes(attributes: VirtualDOMAttributes) {
    lazy val accessibility: NativeBooleanAttribute = NativeBooleanAttribute("accessibility")
    lazy val innerDivClassName: StringAttributeSpec = StringAttributeSpec("className")
    lazy val adaptiveHeight: NativeBooleanAttribute = NativeBooleanAttribute("adaptiveHeight")
    lazy val arrows: NativeBooleanAttribute = NativeBooleanAttribute("arrows")
    lazy val nextArrow: NativeReactElementAttribute = NativeReactElementAttribute("nextArrow")
    lazy val prevArrow: NativeReactElementAttribute = NativeReactElementAttribute("prevArrow")
    lazy val autoPlay: NativeBooleanAttribute = NativeBooleanAttribute("autoPlay")
    lazy val autoPlaySpeed: NativeIntAttribute = NativeIntAttribute("autoPlaySpeed")
    lazy val centerMode: NativeBooleanAttribute = NativeBooleanAttribute("centerMode")
    lazy val centerPadding: StringAttributeSpec = StringAttributeSpec("centerPadding")
    lazy val cssEase = StringAttributeSpec("cssEase")
    lazy val customPaging: NativeFunction1Attribute[Int, ReactElement] = NativeFunction1Attribute("customPaging")
    lazy val dots: NativeBooleanAttribute = NativeBooleanAttribute("dots")
    lazy val dotsClass: StringAttributeSpec = StringAttributeSpec("dotsClass")
    lazy val draggable: NativeBooleanAttribute = NativeBooleanAttribute("draggable")
    lazy val easing: StringAttributeSpec = StringAttributeSpec("easing")
    lazy val fade: NativeBooleanAttribute = NativeBooleanAttribute("fade")
    lazy val focusOnSelect: NativeBooleanAttribute = NativeBooleanAttribute("focusOnSelect")
    lazy val infinite: NativeBooleanAttribute = NativeBooleanAttribute("infinite")
    lazy val initialSlide: NativeIntAttribute = NativeIntAttribute("initialSlide")
    lazy val lazyLoad: NativeBooleanAttribute = NativeBooleanAttribute("lazyLoad")
    lazy val pauseOnHover: NativeBooleanAttribute = NativeBooleanAttribute("pauseOnHover")
    lazy val responsive: NativeJsObjectAttributeSpec[ResponsiveParameters] = NativeJsObjectAttributeSpec("responsive")
    lazy val rtl: NativeBooleanAttribute = NativeBooleanAttribute("rtl")
    lazy val slide: StringAttributeSpec = StringAttributeSpec("slide")
    lazy val slidesToShow: NativeIntAttribute = NativeIntAttribute("slidesToShow")
    lazy val slidesToScroll: NativeIntAttribute = NativeIntAttribute("slidesToScroll")
    lazy val speed: NativeIntAttribute = NativeIntAttribute("speed")
    lazy val swipe: NativeBooleanAttribute = NativeBooleanAttribute("swipe")
    lazy val swipeToSlide: NativeBooleanAttribute = NativeBooleanAttribute("swipeToSlide")
    lazy val touchMove: NativeBooleanAttribute = NativeBooleanAttribute("touchMove")
    lazy val touchThreshold: NativeIntAttribute = NativeIntAttribute("touchThreshold")
    lazy val variableWidth: NativeBooleanAttribute = NativeBooleanAttribute("variableWidth")
    lazy val useCss: NativeBooleanAttribute = NativeBooleanAttribute("useCss")
    lazy val vertical: NativeBooleanAttribute = NativeBooleanAttribute("vertical")
    lazy val afterChange: NativeFunction1Attribute[Int, Unit] = NativeFunction1Attribute("afterChange")
    lazy val beforeChange: NativeFunction2Attribute[Int, Int, Unit] = NativeFunction2Attribute("beforeChange")
    lazy val slickGoTo: NativeIntAttribute = NativeIntAttribute("slickGoTo")

  }

}
