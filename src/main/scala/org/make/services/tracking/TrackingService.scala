package org.make.services.tracking

import org.make.front.facades.FacebookPixel
import scala.scalajs.js.JSConverters._

object TrackingService {

  def track(eventName: String, parameters: Map[String, String] = Map.empty): Unit = {
    if (parameters.isEmpty) {
      FacebookPixel.fbq("trackCustom", eventName)
    } else {
      FacebookPixel.fbq("trackCustom", eventName, parameters.toJSDictionary)
    }
  }

}
