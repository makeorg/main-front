package org.make.front.operations

import org.make.front.facades.{makeEuropeLogo, makeEuropeWhiteLogo}
import org.make.front.models.{GradientColor, OperationExtraSlidesParams, OperationStaticData, OperationWording}

object MakeEuropeOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData =
    OperationStaticData(
      slug = "make-europe",
      country = "GB",
      color = "#003399",
      gradient = Some(GradientColor("#00A5FF", "#003399")),
      logoUrl = makeEuropeLogo.toString,
      whiteLogoUrl = makeEuropeWhiteLogo.toString,
      shareUrl = "",
      wording = Seq(
        OperationWording(
          language = "en",
          title = "Consultation Européenne (démo)",
          question = "Comment réinventer l'Europe concrètement ?",
          learnMoreUrl = None
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
