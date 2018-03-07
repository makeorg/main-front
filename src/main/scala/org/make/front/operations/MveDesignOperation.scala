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
import org.make.front.facades.{CercleEconomistesLogo, LpaeIll, LpaeIll2x, LpaeLogo, LpaeLogoDarker, MveLogo, XPartners}
import org.make.front.models._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object MveDesignOperation extends DesignOperation {

  override val designData: OperationDesignData = {
    OperationDesignData(
      slug = "mieux-vivre-ensemble",
      startDate = None,
      endDate = Some(new js.Date("2018-03-21")),
      country = "FR",
      color = "#f16481",
      gradient = Some(GradientColor("#f6dee3", "#d5e7ff")),
      logoUrl = Some(MveLogo.toString),
      logoMaxWidth = Some(481),
      darkerLogoUrl = None,
      greatCauseLabelAlignment = None,
      wording = Seq(
        OperationWording(
          language = "fr",
          title = "Mieux Vivre Ensemble",
          question = "Comment mieux vivre ensemble&nbsp;?",
          purpose = None,
          period = None,
          label = None,
          mentionUnderThePartners = None,
          explanation = Some(
            "Les Français ont le sentiment de vivre dans une société désunie : 84% dépeignent une cohésion sociale fragile voire inexistante (étude Credoc 2013). 12,5% des Français n’ont aucun réseau social et 1 Français sur 10 se sent exclu, abandonné ou inutile.\nIl est donc essentiel d’imaginer des solutions pour recréer du lien, de la solidarité, la participation de tous à la vie de la société."
          ),
          learnMoreUrl = None
        )
      ),
      featuredIllustration = None,
      illustration = Some(Illustration(illUrl = LpaeIll.toString, ill2xUrl = LpaeIll2x.toString)),
      partners = Seq(
        OperationPartner(name = "BlaBlaCar", imageUrl = CercleEconomistesLogo.toString, imageWidth = 74),
        OperationPartner(name = "Orange", imageUrl = XPartners.toString, imageWidth = 9),
        OperationPartner(name = "AccorHotels", imageUrl = XPartners.toString, imageWidth = 9),
        OperationPartner(name = "Mairie de Paris", imageUrl = XPartners.toString, imageWidth = 9)
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
