package org.make.front.operations

import org.make.front.components.sequence.Sequence.{DisplayTracker, ExtraSlide}
import org.make.front.components.sequence.contents.IntroductionOfTheSequence.IntroductionOfTheSequenceProps
import org.make.front.components.sequence.contents.PromptingToConnect.PromptingToConnectProps
import org.make.front.components.sequence.contents.PromptingToGoBackToOperation.PromptingToGoBackToOperationProps
import org.make.front.components.sequence.contents.{
  IntroductionOfTheSequence,
  PromptingToConnect,
  PromptingToGoBackToOperation,
  PromptingToProposeInRelationToOperation
}
import org.make.front.components.sequence.contents.PromptingToProposeInRelationToOperation.PromptingToProposeInRelationToOperationProps
import org.make.front.models._
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

trait StaticDataOfOperation {
  val data: OperationStaticData
}

object Operations {
  val featuredOperationSlug: String = "vff"

  val operationStaticDataList: Seq[OperationStaticData] = Seq(
    VFFOperationStaticData.data,
    VFFITOperationStaticData.data,
    VFFGBOperationStaticData.data,
    ClimatParisOperationStaticData.data,
    LPAEOperationStaticData.data,
    MVEOperationStaticData.data,
    MakeEuropeOperationStaticData.data
  )
}

object Slides {
  def trackingContext(params: OperationExtraSlidesParams) =
    TrackingContext(TrackingLocation.sequencePage, Some(params.operation.slug))
  def defaultTrackingParameters(params: OperationExtraSlidesParams) =
    Map("sequenceId" -> params.sequence.sequenceId.value)

  def displaySequenceIntroCard(params: OperationExtraSlidesParams, displayed: Boolean = true): ExtraSlide = {
    ExtraSlide(
      reactClass = IntroductionOfTheSequence.reactClass,
      props = { (handler: () => Unit) =>
        {
          val onClick: () => Unit = () => {
            TrackingService
              .track("click-sequence-launch", trackingContext(params), defaultTrackingParameters(params))
            handler()
          }
          IntroductionOfTheSequenceProps(clickOnButtonHandler = onClick)
        }
      },
      position = _ => 0,
      displayed = displayed,
      maybeTracker = Some(
        DisplayTracker(
          name = "display-sequence-intro-card",
          context = trackingContext(params),
          parameters = defaultTrackingParameters(params)
        )
      )
    )
  }

  def displaySignUpCard(params: OperationExtraSlidesParams, displayed: Boolean = true) = ExtraSlide(
    reactClass = PromptingToConnect.reactClass,
    props = { handler =>
      PromptingToConnectProps(
        operation = params.operation,
        sequenceId = params.sequence.sequenceId,
        trackingContext = trackingContext(params),
        trackingParameters = defaultTrackingParameters(params),
        clickOnButtonHandler = handler,
        authenticateHandler = () => {}
      )
    },
    position = { slides =>
      slides.size
    },
    displayed = displayed,
    maybeTracker =
      Some(DisplayTracker("display-sign-up-card", trackingContext(params), defaultTrackingParameters(params)))
  )

  def displayProposalPushCard(params: OperationExtraSlidesParams, displayed: Boolean = true): ExtraSlide = {
    ExtraSlide(
      reactClass = PromptingToProposeInRelationToOperation.reactClass,
      props = { handler =>
        PromptingToProposeInRelationToOperationProps(
          operation = params.operation,
          clickOnButtonHandler = handler,
          proposeHandler = handler,
          sequenceId = params.sequence.sequenceId,
          maybeLocation = params.maybeLocation,
          language = params.language
        )
      },
      position = { slides =>
        slides.size / 2
      },
      displayed = displayed,
      maybeTracker =
        Some(DisplayTracker("display-proposal-push-card", trackingContext(params), defaultTrackingParameters(params)))
    )
  }

  def displayFinalCard(params: OperationExtraSlidesParams, displayed: Boolean = true) = ExtraSlide(
    reactClass = PromptingToGoBackToOperation.reactClass,
    props = { handler =>
      PromptingToGoBackToOperationProps(
        operation = params.operation,
        clickOnButtonHandler = handler,
        sequenceId = params.sequence.sequenceId,
        language = params.language,
        country = params.country
      )
    },
    position = { slides =>
      slides.size
    },
    displayed = displayed,
    maybeTracker =
      Some(DisplayTracker("display-finale-card", trackingContext(params), defaultTrackingParameters(params)))
  )

}
