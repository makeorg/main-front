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

  val vffFrDesignOperation: OperationDesignData = OperationDesignData(
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

  val operationDesignList: Seq[OperationDesignData] = Seq(
    vffFrDesignOperation,
    vffFrDesignOperation.copy(
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
          purpose = Some("Grâce à vos votes et à vos propositions, 17 grandes idées ont été identifiées."),
          period = None,
          label = Some("La Grande Causa di Make.org"),
          mentionUnderThePartners = Some("e i nostri partner sostegno e azione"),
          explanation = Some(
            "La violenza sulle donne è al centro dell'attualità politica e mediatica. Le idee stanno cambiando, ma tutto inizia da qui. Sta a noi trasformare questa presa di coscienza generale in azioni concrete e dare una risposta decisiva a questo flagello."
          ),
          learnMoreUrl = Some("https://about.make.org/it/about-vff")
        )
      )
    ),
    vffFrDesignOperation.copy(
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
          purpose = Some("Grâce à vos votes et à vos propositions, 17 grandes idées ont été identifiées."),
          period = Some("Consultation open from Nov. 25 2017 to the end of January"),
          label = Some("Make.org outreach action"),
          mentionUnderThePartners = Some("and our support and actions partners"),
          explanation = Some(
            "Violence against women is at the heart of political events and news coverage. Mindsets are changing. But it all begins now. It is up to us to transform this generalised awareness into concrete actions and to provide a decisive response to this epidemic."
          ),
          learnMoreUrl = Some("https://about.make.org/gb/about-vff")
        )
      )
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
    ),
    OperationDesignData(
      slug = "lpae",
      startDate = None,
      endDate = Some(new js.Date("2018-03-01")),
      country = "FR",
      color = "#602a7a",
      gradient = Some(GradientColor("#683577", "#782f8b")),
      logoUrl = Some(LpaeLogo.toString),
      logoMaxWidth = Some(830),
      darkerLogoUrl = Some(LpaeLogoDarker.toString),
      greatCauseLabelAlignment = None,
      wording = Seq(
        OperationWording(
          language = "fr",
          title = "La Parole aux Etudiants",
          question = "Vous avez les cl&eacute;s du monde, que changez-vous&nbsp;?",
          purpose = None,
          period = None,
          label = None,
          mentionUnderThePartners = None,
          explanation = Some(
            "Dans une société bousculée, disruptée et en permanente évolution cette consultation est l’occasion pour vous d’exprimer ce que vous voulez changer, de proposer vos idées pour la société de demain, de dessiner les contours du Monde dans lequel vous souhaitez vivre. Vous avez entre 18 et 28 ans, vous avez des choses à dire, c’est le moment&nbsp;!"
          ),
          learnMoreUrl = Some("https://about.make.org/about-lpae")
        )
      ),
      featuredIllustration = None,
      illustration = Some(Illustration(illUrl = LpaeIll.toString, ill2xUrl = LpaeIll2x.toString)),
      partners = Seq(
        OperationPartner(
          name = "Le Cercle des Economistes",
          imageUrl = CercleEconomistesLogo.toString,
          imageWidth = 74
        ),
        OperationPartner(name = "x", imageUrl = XPartners.toString, imageWidth = 9),
        OperationPartner(name = "Make.org", imageUrl = MakeOrgLogo.toString, imageWidth = 51)
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
  )
}
