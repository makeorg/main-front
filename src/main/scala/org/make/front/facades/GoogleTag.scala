package org.make.front.facades

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobalScope

@js.native
@JSGlobalScope
object GoogleTag extends js.Object {
  def gtag(eventType: String, eventName: String, parameters: js.Dictionary[String]): Unit = js.native
}
