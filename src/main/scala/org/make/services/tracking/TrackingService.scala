package org.make.services.tracking

import org.make.front.facades.FacebookPixel
import scala.scalajs.js.JSConverters._

object TrackingService {

  def track(eventName: String, trackingContext: TrackingContext, parameters: Map[String, String] = Map.empty): Unit = {

    var allParameters = parameters +
      ("location" -> trackingContext.location.name) +
      ("source" -> trackingContext.source.name)

    trackingContext.operationSlug.foreach { slug =>
      allParameters += "operation" -> slug
    }

    FacebookPixel.fbq("trackCustom", eventName, allParameters.toJSDictionary)
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
