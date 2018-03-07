package org.make.front.operations
import org.make.front.components.sequence.Sequence.{DisplayTracker, ExtraSlide}
import org.make.front.components.sequence.contents.{
  IntroductionOfTheSequence,
  PromptingToConnect,
  PromptingToGoBackToOperation,
  PromptingToProposeInRelationToOperation
}
import org.make.front.components.sequence.contents.IntroductionOfTheSequence.IntroductionOfTheSequenceProps
import org.make.front.components.sequence.contents.PromptingToConnect.PromptingToConnectProps
import org.make.front.components.sequence.contents.PromptingToGoBackToOperation.PromptingToGoBackToOperationProps
import org.make.front.components.sequence.contents.PromptingToProposeInRelationToOperation.PromptingToProposeInRelationToOperationProps
import org.make.front.facades.{climatParisIll, climatParisIll2x, climatParisLogo, ClimatParisDarkerLogo}
import org.make.front.models._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

object ClimatParisDesignOperation extends DesignOperation {
  override val designData: OperationDesignData = OperationDesignData(
    slug = "climatparis",
    startDate = None,
    endDate = None,
    country = "FR",
    color = "#459ba6",
    gradient = Some(GradientColor("#bfe692", "#69afde")),
    logoUrl = Some(climatParisLogo.toString),
    logoMaxWidth = Some(360),
    darkerLogoUrl = Some(ClimatParisDarkerLogo.toString),
    greatCauseLabelAlignment = Some("left"),
    wording = Seq(
      OperationWording(
        language = "fr",
        title = "Climat Paris",
        question = "Comment lutter contre le changement climatique &agrave; Paris&nbsp;?",
        purpose = None,
        period = None,
        label = None,
        mentionUnderThePartners = None,
        explanation = Some(
          "Les changements climatiques sont au coeur de l’actualité politique et internationale. La COP21 a démontré la volonté des décideurs politiques d’avancer. Un changement de comportement de chaque citoyen est maintenant nécessaire : à nous de transformer la prise de conscience planétaire en idées concrètes pour changer notre rapport à la planète."
        ),
        learnMoreUrl = Some("https://climatparis.make.org/about-climatparis")
      )
    ),
    featuredIllustration = None,
    illustration = Some(Illustration(illUrl = climatParisIll.toString, ill2xUrl = climatParisIll2x.toString)),
    partners = Seq.empty,
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
          displayed = false,
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
