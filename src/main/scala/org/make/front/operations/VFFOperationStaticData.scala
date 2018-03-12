package org.make.front.operations
import org.make.front.components.sequence.Sequence.{DisplayTracker, ExtraSlide}
import org.make.front.components.sequence.contents.IntroductionOfTheSequence.IntroductionOfTheSequenceProps
import org.make.front.components.sequence.contents.PromptingToConnect.PromptingToConnectProps
import org.make.front.components.sequence.contents.PromptingToGoBackToOperation.PromptingToGoBackToOperationProps
import org.make.front.components.sequence.contents.PromptingToProposeInRelationToOperation.PromptingToProposeInRelationToOperationProps
import org.make.front.components.sequence.contents.{
  IntroductionOfTheSequence,
  PromptingToConnect,
  PromptingToGoBackToOperation,
  PromptingToProposeInRelationToOperation
}
import org.make.front.facades.{VFFGBLogo, VFFGBWhiteLogo, VFFITLogo, VFFITWhiteLogo, VFFLogo, VFFWhiteLogo}
import org.make.front.models._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object VFFOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    slug = "vff",
    startDate = None,
    endDate = Some(new js.Date("2018-01-31")),
    country = "FR",
    color = "#660779",
    gradient = Some(GradientColor("#AB92CA", "#54325A")),
    logoUrl = VFFLogo.toString,
    whiteLogoUrl = VFFWhiteLogo.toString,
    wording = Seq(
      OperationWording(
        language = "fr",
        title = "Stop aux Violences Faites aux&nbsp;Femmes",
        question = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
        learnMoreUrl = Some("https://stopvff.make.org/about-vff")
      )
    ),
    extraSlides = (params: OperationExtraSlidesParams) => {
      val trackingContext = TrackingContext(TrackingLocation.sequencePage, Some(params.operation.slug))
      val defaultTrackingParameters = Map("sequenceId" -> params.sequence.sequenceId.value)
      Seq(
        ExtraSlide(
          reactClass = IntroductionOfTheSequence.reactClass,
          maybeTracker = Some(
            DisplayTracker(
              name = "display-sequence-intro-card",
              context = trackingContext,
              parameters = defaultTrackingParameters
            )
          ),
          displayed = false,
          props = { (handler: () => Unit) =>
            {
              val onClick: () => Unit = () => {
                TrackingService
                  .track("click-sequence-launch", trackingContext, defaultTrackingParameters)
                handler()
              }
              IntroductionOfTheSequenceProps(clickOnButtonHandler = onClick)
            }
          },
          position = _ => 0
        ),
        ExtraSlide(
          maybeTracker = Some(
            DisplayTracker(
              "display-sign-up-card",
              trackingContext,
              Map("sequenceId" -> params.sequence.sequenceId.value)
            )
          ),
          reactClass = PromptingToConnect.reactClass,
          props = { handler =>
            PromptingToConnectProps(
              operation = params.operation,
              sequenceId = params.sequence.sequenceId,
              trackingContext = trackingContext,
              trackingParameters = defaultTrackingParameters,
              clickOnButtonHandler = handler,
              authenticateHandler = () => {}
            )
          },
          position = { slides =>
            slides.size
          },
          displayed = !params.isConnected
        ),
        ExtraSlide(
          maybeTracker = Some(DisplayTracker("display-proposal-push-card", trackingContext, defaultTrackingParameters)),
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
          }
        ),
        ExtraSlide(
          maybeTracker = Some(DisplayTracker("display-finale-card", trackingContext, defaultTrackingParameters)),
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
          }
        )
      )
    }
  )
}

object VFFITOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = VFFOperationStaticData.data.copy(
    country = "IT",
    logoUrl = VFFITLogo.toString,
    whiteLogoUrl = VFFITWhiteLogo.toString,
    endDate = None,
    startDate = None,
    wording = Seq(
      OperationWording(
        language = "it",
        title = "Stop alla violenza sulle donne",
        question = "Come far fronte alla violenza sulle donne?",
        learnMoreUrl = Some("https://about.make.org/it/about-vff")
      )
    ),
    extraSlides = (params: OperationExtraSlidesParams) => {
      val trackingContext = TrackingContext(TrackingLocation.sequencePage, Some(params.operation.slug))
      val defaultTrackingParameters = Map("sequenceId" -> params.sequence.sequenceId.value)
      Seq(
        ExtraSlide(
          reactClass = IntroductionOfTheSequence.reactClass,
          maybeTracker = Some(
            DisplayTracker(
              name = "display-sequence-intro-card",
              context = trackingContext,
              parameters = defaultTrackingParameters
            )
          ),
          props = { (handler: () => Unit) =>
            {
              val onClick: () => Unit = () => {
                TrackingService
                  .track("click-sequence-launch", trackingContext, defaultTrackingParameters)
                handler()
              }
              IntroductionOfTheSequenceProps(clickOnButtonHandler = onClick)
            }
          },
          position = _ => 0
        ),
        ExtraSlide(
          maybeTracker = Some(
            DisplayTracker(
              "display-sign-up-card",
              trackingContext,
              Map("sequenceId" -> params.sequence.sequenceId.value)
            )
          ),
          reactClass = PromptingToConnect.reactClass,
          props = { handler =>
            PromptingToConnectProps(
              operation = params.operation,
              sequenceId = params.sequence.sequenceId,
              trackingContext = trackingContext,
              trackingParameters = defaultTrackingParameters,
              clickOnButtonHandler = handler,
              authenticateHandler = () => {}
            )
          },
          position = { slides =>
            slides.size
          },
          displayed = !params.isConnected
        ),
        ExtraSlide(
          maybeTracker = Some(DisplayTracker("display-proposal-push-card", trackingContext, defaultTrackingParameters)),
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
          }
        ),
        ExtraSlide(
          maybeTracker = Some(DisplayTracker("display-finale-card", trackingContext, defaultTrackingParameters)),
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
          }
        )
      )
    }
  )
}

object VFFGBOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = VFFOperationStaticData.data.copy(
    country = "GB",
    logoUrl = VFFGBLogo.toString,
    whiteLogoUrl = VFFGBWhiteLogo.toString,
    endDate = None,
    startDate = None,
    wording = Seq(
      OperationWording(
        language = "en",
        title = "Stop violence against women",
        question = "How to combat violence against women?",
        learnMoreUrl = Some("https://about.make.org/gb/about-vff")
      )
    ),
    extraSlides = (params: OperationExtraSlidesParams) => {
      val trackingContext = TrackingContext(TrackingLocation.sequencePage, Some(params.operation.slug))
      val defaultTrackingParameters = Map("sequenceId" -> params.sequence.sequenceId.value)
      Seq(
        ExtraSlide(
          reactClass = IntroductionOfTheSequence.reactClass,
          maybeTracker = Some(
            DisplayTracker(
              name = "display-sequence-intro-card",
              context = trackingContext,
              parameters = defaultTrackingParameters
            )
          ),
          props = { (handler: () => Unit) =>
            {
              val onClick: () => Unit = () => {
                TrackingService
                  .track("click-sequence-launch", trackingContext, defaultTrackingParameters)
                handler()
              }
              IntroductionOfTheSequenceProps(clickOnButtonHandler = onClick)
            }
          },
          position = _ => 0
        ),
        ExtraSlide(
          maybeTracker = Some(
            DisplayTracker(
              "display-sign-up-card",
              trackingContext,
              Map("sequenceId" -> params.sequence.sequenceId.value)
            )
          ),
          reactClass = PromptingToConnect.reactClass,
          props = { handler =>
            PromptingToConnectProps(
              operation = params.operation,
              sequenceId = params.sequence.sequenceId,
              trackingContext = trackingContext,
              trackingParameters = defaultTrackingParameters,
              clickOnButtonHandler = handler,
              authenticateHandler = () => {}
            )
          },
          position = { slides =>
            slides.size
          },
          displayed = !params.isConnected
        ),
        ExtraSlide(
          maybeTracker = Some(DisplayTracker("display-proposal-push-card", trackingContext, defaultTrackingParameters)),
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
          }
        ),
        ExtraSlide(
          maybeTracker = Some(DisplayTracker("display-finale-card", trackingContext, defaultTrackingParameters)),
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
          }
        )
      )
    }
  )
}
