package org.make.front.operations

import org.make.front.facades.chanceAuxJeunesLogo
import org.make.front.models._

object ChanceAuxJeunesOperationStaticData extends StaticDataOfOperation {

  override val data: OperationStaticData = {
    OperationStaticData(
      slug = "chance-aux-jeunes",
      country = "FR",
      color = "#2999d5",
      gradient = Some(GradientColor("#2999D5", "#B6D0CE")),
      logoUrl = chanceAuxJeunesLogo.toString,
      whiteLogoUrl = chanceAuxJeunesLogo.toString,
      shareUrl = "/chance-aux-jeunes.html_UTM_#/FR/consultation/chance-aux-jeunes/selection",
      wording = Seq(
        OperationWording(
          language = "fr",
          title = "Une chance pour chaque jeune",
          question = "Comment donner une chance à chaque jeune&nbsp;?",
          learnMoreUrl = Some("http://www.UneChancePourChaqueJeune.org")
        )
      ),
      extraSlides = (params: OperationExtraSlidesParams) => {
        Seq(
          Slides.displaySequenceIntroCard(params),
          Slides.displaySignUpCard(params, !params.isConnected),
          Slides.displayProposalPushCard(params),
          Slides.displayFinalCard(params)
        )
      }
    )
  }
}
