package org.make.front.operations

import org.make.front.facades.{makeEuropeLogo, makeEuropeWhiteLogo}
import org.make.front.models.{GradientColor, OperationExtraSlidesParams, OperationStaticData, OperationWording}

class EuropeOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData =
    OperationStaticData(
      country = "GB",
      slug = "make-europe",
      wording = Seq(
        OperationWording(
          language = "en",
          title = "Consultation Européenne (démo)",
          question = "Comment réinventer l'Europe concrètement ?",
          learnMoreUrl = None
        )
      ),
      color = "#003399",
      gradient = Some(GradientColor("#00A5FF", "#003399")),
      logoUrl = makeEuropeLogo.toString,
      whiteLogoUrl = makeEuropeWhiteLogo.toString,
      shareUrl = "",
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
