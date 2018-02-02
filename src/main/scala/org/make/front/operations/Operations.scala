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
import org.make.front.facades._
import org.make.front.models._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}
import scala.scalajs.js

object Operations {

  val featuredOperationSlug: String = "vff"
  val operationDesignList: Seq[OperationDesignData] = Seq(
    OperationDesignData(
      slug = "vff",
      startDate = None,
      endDate = Some(new js.Date("2018-01-31")),
      country = "FR",
      color = "#660779",
      gradient = Some(GradientColor("#AB92CA", "#54325A")),
      logoUrl = Some(VFFLogo.toString),
      logoMaxWidth = Some(470),
      darkerLogoUrl = Some(VFFDarkerLogo.toString),
      greatCauseLabelAlignment = Some("left"),
      wording = Seq(
        OperationWording(
          language = "fr",
          title = "Stop aux Violences Faites aux&nbsp;Femmes",
          question = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
          purpose = Some(
            "Make.org a décidé de lancer sa première Grande Cause en la consacrant à la lutte contre les Violences faites aux&nbsp;femmes."
          ),
          period = Some("Consultation ouverte du 25 nov. 2017 à fin janvier"),
          label = Some("Grande cause Make.org"),
          mentionUnderThePartners = Some("et nos partenaires soutien et&nbsp;actions"),
          explanation = Some(
            "Les violences faites aux femmes sont au coeur de l’actualité politique et médiatique. Les mentalités sont en train de changer. Mais pour autant tout commence maintenant. À nous de transformer cette prise de conscience généralisée en actions concrètes et d’apporter une réponse décisive face à ce&nbsp;fléau."
          ),
          learnMoreUrl = Some("https://stopvff.make.org/about-vff")
        )
      ),
      featuredIllustration = Some(
        Illustration(
          smallIllUrl = Some(featuredVFFSmall.toString),
          smallIll2xUrl = Some(featuredVFFSmall2x.toString),
          mediumIllUrl = Some(featuredVFFMedium.toString),
          mediumIll2xUrl = Some(featuredVFFMedium2x.toString),
          illUrl = featuredVFF.toString,
          ill2xUrl = featuredVFF2x.toString
        )
      ),
      illustration = Some(Illustration(illUrl = VFFIll.toString, ill2xUrl = VFFIll2x.toString)),
      partners = Seq(
        OperationPartner(name = "Kering Foundation", imageUrl = keringFoundationLogo.toString, imageWidth = 80),
        OperationPartner(name = "Facebook", imageUrl = facebookLogo.toString, imageWidth = 80)
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
                authenticateHandler = handler
              )
            },
            position = { slides =>
              slides.size
            },
            displayed = !params.isConnected
          ),
          ExtraSlide(
            maybeTracker =
              Some(DisplayTracker("display-proposal-push-card", trackingContext, defaultTrackingParameters)),
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
                language = params.language
              )
            },
            position = { slides =>
              slides.size
            }
          )
        )
      }
    ),
    OperationDesignData(
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
            maybeTracker =
              Some(DisplayTracker("display-proposal-push-card", trackingContext, defaultTrackingParameters)),
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
                language = params.language
              )
            },
            position = { slides =>
              slides.size
            }
          )
        )
      }
    )
  )
}
