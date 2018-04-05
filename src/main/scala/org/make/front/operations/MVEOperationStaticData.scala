package org.make.front.operations

import org.make.front.facades.mveLogo
import org.make.front.models._

object MVEOperationStaticData extends StaticDataOfOperation {

  override val data: OperationStaticData = {
    OperationStaticData(
      slug = "mieux-vivre-ensemble",
      country = "FR",
      color = "#f16481",
      /*TODO: remove this commented option after nico validation*/
      /*gradient = Some(GradientColor("#d97388", "#4d99ff")),*/ /*http://www.0to255.com/ #f6dee3 -8,  #d5e7ff -8 */
      gradient = Some(GradientColor("#EE6380", "#77C4D1")), /*colors from logo*/
      logoUrl = mveLogo.toString,
      whiteLogoUrl = mveLogo.toString,
      shareUrl = "/mieux-vivre-ensemble.html_UTM_#/FR/consultation/mieux-vivre-ensemble/selection",
      wording = Seq(
        OperationWording(
          language = "fr",
          title = "Mieux Vivre Ensemble",
          question = "Comment mieux vivre ensemble&nbsp;?",
          learnMoreUrl = Some("https://about.make.org/about-mieux-vivre-ensemble")
        )
      ),
      extraSlides = (params: OperationExtraSlidesParams) => {
        Seq(
          Slides.displaySequenceIntroCard(params),
          Slides.displaySignUpCard(params, !params.isConnected),
          Slides.displayProposalPushCard(params, displayed = false),
          Slides.displayFinalCard(params)
        )
      }
    )
  }
}
