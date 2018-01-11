package org.make.front.models

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
import org.make.front.facades.{VFFDarkerLogo, VFFLogo, _}
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}

// toDo: manage translations
// toDo: refactor to use a friendly configuration format
final case class OperationDesignData(slug: String,
                                     wording: OperationWording,
                                     color: String,
                                     gradient: Option[GradientColor] = None,
                                     logoUrl: Option[String] = None,
                                     logoMaxWidth: Option[Int],
                                     darkerLogoUrl: Option[String] = None,
                                     labelAlignment: Option[String] = Some("left"),
                                     featuredIllustration: Option[Illustration] = None,
                                     illustration: Option[Illustration] = None,
                                     partners: Seq[OperationPartner] = Seq.empty,
                                     extraSlides: (OperationExtraSlidesParams) => Seq[ExtraSlide])

object OperationDesignData {
  val defaultUrl = "consultation/{slug}/selection"
  val defaultCountry = "FR"
  val featuredOperationSlug = "vff"
  def getBySlug(slug: String): Option[OperationDesignData] = {
    val resultList: Seq[OperationDesignData] = defaultOperationDesignList.filter(_.slug == slug)
    resultList match {
      case operations if operations.nonEmpty => Some(operations.head)
      case _                                 => None
    }
  }

  val defaultSlides: OperationExtraSlidesParams => Seq[ExtraSlide] = (params: OperationExtraSlidesParams) => {
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
          DisplayTracker("display-sign-up-card", trackingContext, Map("sequenceId" -> params.sequence.sequenceId.value))
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
            maybeLocation = params.maybeLocation
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
            sequenceId = params.sequence.sequenceId
          )
        },
        position = { slides =>
          slides.size
        }
      )
    )
  }
  val vffSlides: OperationExtraSlidesParams => Seq[ExtraSlide] = defaultSlides

  val cpSlides: OperationExtraSlidesParams => Seq[ExtraSlide] = (params: OperationExtraSlidesParams) => {

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
                .track("click-sequence-launch", trackingContext, Map("sequenceId" -> params.sequence.sequenceId.value))
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
            maybeLocation = params.maybeLocation
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
            sequenceId = params.sequence.sequenceId
          )
        },
        position = { slides =>
          slides.size
        }
      )
    )
  }

  val vffOperationDesignData: OperationDesignData = OperationDesignData(
    slug = "vff",
    color = "#660779",
    gradient = Some(GradientColor("#AB92CA", "#54325A")),
    logoUrl = Some(VFFLogo.toString),
    darkerLogoUrl = Some(VFFDarkerLogo.toString),
    logoMaxWidth = Some(470),
    wording = OperationWording(
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
    extraSlides = vffSlides
  )

  val climatParisOperationDesignData: OperationDesignData = OperationDesignData(
    slug = "climatparis",
    color = "#459ba6",
    gradient = Some(GradientColor("#bfe692", "#69afde")),
    logoUrl = Some(climatParisLogo.toString),
    logoMaxWidth = Some(360),
    darkerLogoUrl = Some(ClimatParisDarkerLogo.toString),
    wording = OperationWording(
      title = "Climat Paris",
      question = "Comment lutter contre le changement climatique &agrave; Paris&nbsp;?",
      purpose = None,
      period = None,
      label = None,
      explanation = Some(
        "Les changements climatiques sont au coeur de l’actualité politique et internationale. La COP21 a démontré la volonté des décideurs politiques d’avancer. Un changement de comportement de chaque citoyen est maintenant nécessaire : à nous de transformer la prise de conscience planétaire en idées concrètes pour changer notre rapport à la planète."
      ),
      learnMoreUrl = Some("https://climatparis.make.org/about-climatparis")
    ),
    featuredIllustration = None,
    illustration = Some(Illustration(illUrl = climatParisIll.toString, ill2xUrl = climatParisIll2x.toString)),
    partners = Seq.empty,
    extraSlides = cpSlides
  )

  val defaultOperationDesignList: Seq[OperationDesignData] = Seq(vffOperationDesignData, climatParisOperationDesignData)
}

final case class OperationPartner(name: String, imageUrl: String, imageWidth: Int)

final case class OperationWording(title: String,
                                  question: String,
                                  label: Option[String] = None,
                                  purpose: Option[String] = None,
                                  period: Option[String] = None,
                                  mentionUnderThePartners: Option[String] = None,
                                  explanation: Option[String] = None,
                                  learnMoreUrl: Option[String] = None)

final case class OperationExtraSlidesParams(operation: OperationExpanded,
                                            isConnected: Boolean,
                                            sequence: Sequence,
                                            maybeLocation: Option[Location])

final case class OperationExpanded(operationId: OperationId,
                                   url: String,
                                   slug: String,
                                   label: String,
                                   actionsCount: Int,
                                   proposalsCount: Int,
                                   color: String,
                                   gradient: Option[GradientColor] = None,
                                   illustration: Option[Illustration],
                                   featuredIllustration: Option[Illustration],
                                   tags: Seq[Tag] = Seq.empty,
                                   logoUrl: Option[String] = None,
                                   logoMaxWidth: Option[Int],
                                   darkerLogoUrl: Option[String] = None,
                                   sequence: SequenceId,
                                   greatCauseLabelAlignment: Option[String] = Some("left"),
                                   wording: OperationWording,
                                   partners: Seq[OperationPartner] = Seq.empty,
                                   extraSlides: (OperationExtraSlidesParams) => Seq[ExtraSlide])

// @todo: use a sealed trait and case object like Source and Location
object OperationExpanded {
  def getOperationExpandedFromOperation(operation: Operation): OperationExpanded = {
    val maybeOperationDesignData: Option[OperationDesignData] = OperationDesignData.getBySlug(operation.slug)
    maybeOperationDesignData match {
      case Some(operationDesignData) =>
        OperationExpanded(
          operationId = operation.operationId,
          url = OperationDesignData.defaultUrl.replace("{slug}", operation.slug),
          slug = operation.slug,
          label = operation.slug,
          actionsCount = 0,
          proposalsCount = 0,
          color = operationDesignData.color,
          gradient = operationDesignData.gradient,
          logoUrl = operationDesignData.logoUrl,
          logoMaxWidth = operationDesignData.logoMaxWidth,
          darkerLogoUrl = operationDesignData.darkerLogoUrl,
          sequence = operation.sequenceLandingId,
          greatCauseLabelAlignment = operationDesignData.labelAlignment,
          wording = OperationWording(
            // toDo: manage different languages
            title =
              operation.translations.filter(_.language.toLowerCase == operation.defaultLanguage.toLowerCase).head.title,
            question = operationDesignData.wording.question,
            label = operationDesignData.wording.label,
            purpose = operationDesignData.wording.purpose,
            period = operationDesignData.wording.period,
            mentionUnderThePartners = operationDesignData.wording.mentionUnderThePartners,
            explanation = operationDesignData.wording.explanation,
            learnMoreUrl = operationDesignData.wording.learnMoreUrl
          ),
          // toDo: manage different countries
          tags =
            operation.countriesConfiguration.filter(_.countryCode == OperationDesignData.defaultCountry).head.TagIds,
          partners = operationDesignData.partners,
          illustration = operationDesignData.illustration,
          featuredIllustration = operationDesignData.featuredIllustration,
          extraSlides = operationDesignData.extraSlides
        )
      case _ =>
        OperationExpanded(
          operationId = operation.operationId,
          url = OperationDesignData.defaultUrl.replace("{slug}", operation.slug),
          slug = operation.slug,
          label = operation.slug,
          actionsCount = 0,
          proposalsCount = 0,
          color = "",
          gradient = None,
          logoUrl = None,
          logoMaxWidth = None,
          darkerLogoUrl = None,
          sequence = operation.sequenceLandingId,
          wording = OperationWording(
            // toDo: manage different languages
            title =
              operation.translations.filter(_.language.toLowerCase == operation.defaultLanguage.toLowerCase).head.title,
            question = "",
            label = None,
            purpose = None,
            period = None,
            mentionUnderThePartners = None,
            explanation = None,
            learnMoreUrl = None
          ),
          // toDo: manage different countries
          tags =
            operation.countriesConfiguration.filter(_.countryCode == OperationDesignData.defaultCountry).head.TagIds,
          illustration = None,
          featuredIllustration = None,
          extraSlides = (_: OperationExtraSlidesParams) => Seq.empty
        )
    }
  }
}
