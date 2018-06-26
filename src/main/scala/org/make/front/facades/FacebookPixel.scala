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
  *   TrackingService.track("trackCustom", "testLoadHeader1")
  * </code>
  *
  * <b>Triggering Event with Parameters</b>
  * <code>
  *   import org.make.front.facades.FacebookPixel
  *   import scala.scalajs.js.JSConverters._
  *   TrackingService.track("trackCustom", "testLoadHeader", Map("Hello" -> "World").toJSDictionary)
  * </code>
  *
  *
  */
@js.native
@JSGlobalScope
object FacebookPixel extends js.Object {
  def fbq(eventType: String, eventName: String, eventParameter: js.Dictionary[String]): Unit = js.native
}
