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
import org.make.front.facades.{
  facebookLogo,
  featuredVFF,
  featuredVFF2x,
  featuredVFFMedium,
  featuredVFFMedium2x,
  featuredVFFSmall,
  featuredVFFSmall2x,
  keringFoundationLogo,
  VFFDarkerLogo,
  VFFDarkerLogoGB,
  VFFDarkerLogoIT,
  VFFIll,
  VFFIll2x,
  VFFLogo,
  VFFLogoGB,
  VFFLogoIT
}
import org.make.front.models._
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

import scala.scalajs.js

object VffFRDesignOperation extends DesignOperation {
  override val designData: OperationDesignData = OperationDesignData(
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
        purpose = Some("Grâce à vos votes et à vos propositions, 17 grandes idées ont été identifiées."),
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

object VffITDesignOperation extends DesignOperation {
  override val designData: OperationDesignData = VffFRDesignOperation.designData.copy(
    country = "IT",
    logoUrl = Some(VFFLogoIT.toString),
    darkerLogoUrl = Some(VFFDarkerLogoIT.toString),
    endDate = None,
    startDate = None,
    wording = Seq(
      OperationWording(
        language = "it",
        title = "Stop alla violenza sulle donne",
        question = "Come far fronte alla violenza sulle donne?",
        purpose = None,
        period = None,
        label = Some("Grande Causa Make.org"),
        mentionUnderThePartners = None,
        explanation = Some(
          "La violenza sulle donne è al centro dell'attualità politica e mediatica. Le idee stanno cambiando, ma tutto inizia da qui. Sta a noi trasformare questa presa di coscienza generale in azioni concrete e dare una risposta decisiva a questo flagello."
        ),
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
              authenticateHandler = handler
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

object VffGBDesignOperation extends DesignOperation {
  override val designData: OperationDesignData = VffFRDesignOperation.designData.copy(
    country = "GB",
    logoUrl = Some(VFFLogoGB.toString),
    darkerLogoUrl = Some(VFFDarkerLogoGB.toString),
    endDate = None,
    startDate = None,
    wording = Seq(
      OperationWording(
        language = "en",
        title = "Stop violence against women",
        question = "How to combat violence against women?",
        purpose = None,
        period = None,
        label = Some("Make.org Grand Challenge"),
        mentionUnderThePartners = None,
        explanation = Some(
          "Violence against women is at the heart of political events and news coverage. Mindsets are changing. But it all begins now. It is up to us to transform this generalised awareness into concrete actions and to provide a decisive response to this epidemic."
        ),
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
              authenticateHandler = handler
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
