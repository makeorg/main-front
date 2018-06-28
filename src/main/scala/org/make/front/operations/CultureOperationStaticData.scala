package org.make.front.operations
import org.make.front.components.operation.intro.CultureOperationIntro
import org.make.front.components.operation.intro.CultureOperationIntro.CultureOperationIntroProps
import org.make.front.components.consultation.partners.CulturePartnersList
import org.make.front.facades.{cultureLogo, cultureLogoWhite}
import org.make.front.models._

import scala.scalajs.js

object CultureOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    country = "FR",
    slug = "culture",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "Accès à la culture pour tous",
        question = "Comment rendre la culture accessible à tous ?",
        learnMoreUrl = Some("https://about.make.org/about-culture"),
        presentation = Some(
          """Grâce au soutien de ses partenaires fondateurs, le ministère de la Culture, la Fondation Engie et Arte, Make.org lance une vaste consultation citoyenne en ligne autour de la question : "Comment rendre la Culture accessible à tous ?". Les milliers de propositions citoyennes déposées vont nous permettre de bâtir le 1er grand Plan d'actions citoyen en faveur de la Culture. Toutes seront concrétisées. Entreprises, associations, pouvoirs publics, citoyens, nous nous donnons trois ans pour écrire ensemble cette histoire collective inédite."""
        )
      )
    ),
    color = "#6B26E8",
    gradient = Some(GradientColor("#7921DB", "#FEC736")),
    logoUrl = cultureLogo.toString,
    whiteLogoUrl = cultureLogoWhite.toString,
    logoWidth = 295,
    shareUrl = "/FR/consultation/culture/selection_UTM_&language=fr#/FR/consultation/culture/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(params, introWording = OperationIntroWording()),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.displayLightFinalCard(params, onFocus = () => {
          js.timers.setTimeout(1500d) {
            params.redirect(s"/${params.country}/consultation/${params.operation.slug}/consultation")
          }
        })
      )
    },
    headerComponent = CultureOperationIntro.reactClass,
    partnersComponent = CulturePartnersList.reactClass,
    headerProps = operation => CultureOperationIntroProps(operation),
    startDateActions = None,
    consultationVersion = ConsultationVersion.V2
  )
}
