package org.make.front.facades

import scala.scalajs.js
import scala.scalajs.js.annotation._

/**
  * Facade to Facebook Pixel SDK
  *
  * Example usage:
  *
  * <b>Triggering Event</b>
  * <code>
  *   import org.make.front.facades.FacebookPixel
  *   FacebookPixel.fbq("trackCustom", "testLoadHeader1")
  * </code>
  *
  * <b>Triggering Event with Parameters</b>
  * <code>
  *   import org.make.front.facades.FacebookPixel
  *   import scala.scalajs.js.JSConverters._
  *   FacebookPixel.fbq("trackCustom", "testLoadHeader", Map("Hello" -> "World").toJSDictionary)
  * </code>
  *
  *
  */
@js.native
@JSGlobalScope
object FacebookPixel extends js.Object {
  def fbq(eventType: String, eventName: String): Unit = js.native

  def fbq(eventType: String, eventName: String, eventParameter: js.Dictionary[String]): Unit = js.native
}
