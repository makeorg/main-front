package org.make.front.operations

import org.make.front.components.sequence.Sequence.{DisplayTracker, ExtraSlide}
import org.make.front.components.sequence.contents.IntroductionOfTheSequence.IntroductionOfTheSequenceProps
import org.make.front.components.sequence.contents.PromptingToConnect.PromptingToConnectProps
import org.make.front.components.sequence.contents.PromptingToGoBackToOperation.PromptingToGoBackToOperationProps
import org.make.front.components.sequence.contents.{
  IntroductionOfTheSequence,
  PromptingToConnect,
  PromptingToGoBackToOperation
}
import org.make.front.facades.MveLogo
import org.make.front.models._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object MVEOperationStaticData extends StaticDataOfOperation {

  override val data: OperationStaticData = {
    OperationStaticData(
      slug = "mieux-vivre-ensemble",
      startDate = None,
      endDate = Some(new js.Date("2018-03-21")),
      country = "FR",
      color = "#f16481",
      gradient = Some(GradientColor("#f6dee3", "#d5e7ff")),
      logoUrl = Some(MveLogo.toString),
      darkerLogoUrl = None,
      wording = Seq(
        OperationWording(
          language = "fr",
          title = "Mieux Vivre Ensemble",
          question = "Comment mieux vivre ensemble&nbsp;?",
          learnMoreUrl = Some("https://about.make.org/about-mieux-vivre-ensemble")
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
                parameters = Map("sequenceId" -> params.sequence.sequenceId.value)
              )
            ),
            props = { (handler: () => Unit) =>
              {
                val onClick: () => Unit = () => {
                  TrackingService
                    .track(
                      "click-sequence-launch",
                      trackingContext,
                      Map("sequenceId" -> params.sequence.sequenceId.value)
                    )
                  handler()
                }
                IntroductionOfTheSequenceProps(clickOnButtonHandler = onClick)
              }
            },
            position = _ => 0
          ),
          ExtraSlide(
            maybeTracker = Some(DisplayTracker("display-sign-up-card", trackingContext, defaultTrackingParameters)),
            reactClass = PromptingToConnect.reactClass,
            props = { handler =>
              PromptingToConnectProps(
                operation = params.operation,
                sequenceId = params.sequence.sequenceId,
                trackingContext = trackingContext,
                trackingParameters = defaultTrackingParameters,
                clickOnButtonHandler = handler,
                authenticateHandler = handler
              )
            },
            position = { slides =>
              slides.size
            },
            displayed = !params.isConnected
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
}
