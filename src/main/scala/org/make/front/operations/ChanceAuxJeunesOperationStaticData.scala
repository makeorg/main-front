package org.make.front.operations

import org.make.front.components.operation.intro.ChanceAuxJeunesOperationIntro
import org.make.front.components.operation.intro.ChanceAuxJeunesOperationIntro.ChanceAuxJeunesOperationIntroProps
import org.make.front.facades.{chanceAuxJeunesLogo, chanceAuxJeunesLogoWhite}
import org.make.front.models._

import scala.scalajs.js

object ChanceAuxJeunesOperationStaticData extends StaticDataOfOperation {

  override val data: OperationStaticData = {
    OperationStaticData(
      slug = "chance-aux-jeunes",
      country = "FR",
      color = "#2999d5",
      gradient = Some(GradientColor("#2999D5", "#B6D0CE")),
      logoUrl = chanceAuxJeunesLogo.toString,
      whiteLogoUrl = chanceAuxJeunesLogoWhite.toString,
      shareUrl = "/chance-aux-jeunes.html_UTM_#/FR/consultation/chance-aux-jeunes/selection",
      wording = js.Array(
        OperationWording(
          language = "fr",
          title = "Une chance pour chaque jeune",
          question = "Comment donner une chance à chaque jeune&nbsp;?",
          learnMoreUrl = Some("http://www.UneChancePourChaqueJeune.org")
        )
      ),
      extraSlides = (params: OperationExtraSlidesParams) => {
        js.Array(
          Slides.displaySequenceIntroCard(
            params,
            introWording = OperationIntroWording(
              title = Some("Ensemble, changeons l'avenir des jeunes !"),
              explanation1 = Some("Éducation, logement, emploi, pouvoir d'achat... nous devons trouver des solutions."),
              explanation2 =
                Some("Les propositions les + soutenues seront mises en action par Make.org et ses partenaires.")
            )
          ),
          Slides.displaySignUpCard(params, !params.isConnected),
          Slides.displayProposalPushCard(params, displayed = false),
          Slides.displayFinalCard(params)
        )
      },
      headerComponent = ChanceAuxJeunesOperationIntro.reactClass,
      headerProps = (operation) => {
        ChanceAuxJeunesOperationIntroProps(operation = operation)
      }
    )
  }
}
