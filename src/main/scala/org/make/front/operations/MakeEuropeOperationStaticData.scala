package org.make.front.operations

import org.make.front.components.operation.intro.MakeEuropeOperationIntro
import org.make.front.components.operation.intro.MakeEuropeOperationIntro.MakeEuropeOperationIntroProps
import org.make.front.components.operation.partners.MakeEuropeOperationPartners
import org.make.front.facades.{makeEuropeLogo, makeEuropeWhiteLogo}
import org.make.front.models._

import scala.scalajs.js

object MakeEuropeOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData =
    OperationStaticData(
      slug = "make-europe",
      country = "GB",
      color = "#003399",
      gradient = Some(GradientColor("#00A5FF", "#003399")),
      logoUrl = makeEuropeLogo.toString,
      whiteLogoUrl = makeEuropeWhiteLogo.toString,
      logoWidth = 306,
      shareUrl = "",
      wording = js.Array(
        OperationWording(
          language = "en",
          title = "Consultation Européenne (démo)",
          question = "How to reinvent Europe concretely?",
          learnMoreUrl = None,
          presentation = None
        )
      ),
      extraSlides = (params: OperationExtraSlidesParams) => {
        js.Array(
          Slides.displaySequenceIntroCard(params, introWording = OperationIntroWording()),
          Slides.displaySignUpCard(params, !params.isConnected),
          Slides.displayProposalPushCard(params),
          Slides.displayFinalCard(params)
        )
      },
      headerComponent = MakeEuropeOperationIntro.reactClass,
      partnersComponent = MakeEuropeOperationPartners.reactClass,
      headerProps = (operation) => MakeEuropeOperationIntroProps(operation),
      startDateActions = None
    )
}
