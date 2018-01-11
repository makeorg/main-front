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
    var headers =
      Map[String, String](MakeApiClient.sourceHeader -> trackingContext.source.name)

    val params = js.Dictionary(
      "eventType" -> eventType,
      "eventName" -> eventName,
      "eventParameters" -> allParameters.toJSDictionary
    )

    MakeApiClient
      .post[js.Object](resourceName / "front", data = JSON.stringify(params), headers = headers)
      .map { _ =>
        }
  }

}
