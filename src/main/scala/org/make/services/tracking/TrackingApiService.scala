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

package org.make.services.tracking

import org.make.client.MakeApiClient
import org.make.services.ApiService
import org.make.core.URI._
import org.make.services.tracking.TrackingService.TrackingContext

import scala.concurrent.Future
import scala.scalajs.js.JSConverters._
import scala.scalajs.js
import scala.scalajs.js.JSON

import scala.concurrent.ExecutionContext.Implicits.global

object TrackingApiService extends ApiService {
  override val resourceName = "tracking"

  def track(eventType: String,
            eventName: String,
            allParameters: Map[String, String] = Map.empty,
            trackingContext: TrackingContext): Future[Unit] = {
    val params = js.Dictionary(
      "eventType" -> eventType,
      "eventName" -> eventName,
      "eventParameters" -> allParameters.toJSDictionary
    )

    MakeApiClient
      .post[js.Object](resourceName / "front", data = JSON.stringify(params))
      .map(_ => ())
  }

  def performance(): Future[Unit] = {
    val parameters =
      js.Dictionary[js.Any]("applicationName" -> "legacy-front", "timings" -> org.scalajs.dom.window.performance.timing)

    MakeApiClient
      .post[js.Object](resourceName / "performance", data = JSON.stringify(parameters))
      .map(_ => ())
  }

}
