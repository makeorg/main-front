package org.make.services.tracking

import org.make.front.facades.{Configuration, FacebookPixel, GoogleTag}

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object TrackingService {

  private var globalTrackingContext: Map[String, String] = Map.empty

  def setGlobalTrackingParameter(globalTrackingParameter: GlobalTrackingParameter, value: String): Unit = {
    globalTrackingContext = globalTrackingContext ++ Map(globalTrackingParameter.name -> value)
  }

  def setGlobalTrackingParameters(globalTrackingParameters: Map[GlobalTrackingParameter, String]): Unit = {
    globalTrackingContext = globalTrackingContext ++ globalTrackingParameters.map {
      case (globalTracking, value) => (globalTracking.name, value)
    }
  }

  def resetGlobalTrackingContext(): Unit = {
    globalTrackingContext = Map.empty
  }

  def track(eventName: String, trackingContext: TrackingContext, parameters: Map[String, String] = Map.empty): Unit = {

    val eventType: String = "trackCustom"
    var allParameters = parameters +
      ("location" -> trackingContext.location.name) +
      ("source" -> trackingContext.source.name)

    trackingContext.operationSlug.foreach { slug =>
      allParameters += "operation" -> slug
    }

    allParameters = allParameters ++ globalTrackingContext

    TrackingApiService.track(eventType, eventName, allParameters, trackingContext)
    FacebookPixel.fbq(eventType, eventName, allParameters.toJSDictionary)
    GoogleTag.gtag("event", eventName, allParameters.toJSDictionary)

  }

  case class TrackingContext(location: TrackingLocation,
                             operationSlug: Option[String] = None,
                             source: TrackingSource = TrackingSource.core)

}

trait BasicEnum {
  def name: String
}

sealed trait TrackingSource extends BasicEnum

object TrackingSource {
  private sealed case class MakeTrackingSource(override val name: String) extends TrackingSource

  val core: TrackingSource = MakeTrackingSource("core")
}
sealed trait TrackingLocation extends BasicEnum

object TrackingLocation {

  private sealed case class MakeTrackingLocation(override val name: String) extends TrackingLocation

  val unknown: TrackingLocation = MakeTrackingLocation("unknown")
  val homepage: TrackingLocation = MakeTrackingLocation("homepage")
  val showcaseHomepage: TrackingLocation = MakeTrackingLocation("showcase-home")
  val themePage: TrackingLocation = MakeTrackingLocation("page-theme")
  val operationPage: TrackingLocation = MakeTrackingLocation("page-operation")
  val sequencePage: TrackingLocation = MakeTrackingLocation("sequence")
  val sequenceProposalPushCard: TrackingLocation = MakeTrackingLocation("sequence-proposal-push-card")
  val searchResultsPage: TrackingLocation = MakeTrackingLocation("page-searchresults")
  val submitProposalPage: TrackingLocation = MakeTrackingLocation("proposal-submit")
  val endProposalPage: TrackingLocation = MakeTrackingLocation("end-proposal-form")
  val navBar: TrackingLocation = MakeTrackingLocation("nav-bar")
  val proposalPage: TrackingLocation = MakeTrackingLocation("proposal-page")

}

sealed trait GlobalTrackingParameter extends BasicEnum

object GlobalTrackingParameter {

  private sealed case class MakeGlobalTrackingParameter(override val name: String) extends GlobalTrackingParameter

  val country: GlobalTrackingParameter = MakeGlobalTrackingParameter("country")
  val language: GlobalTrackingParameter = MakeGlobalTrackingParameter("language")
}
