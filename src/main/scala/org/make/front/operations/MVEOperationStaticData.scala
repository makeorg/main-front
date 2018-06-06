package org.make.front.operations

import org.make.front.components.operation.intro.MVEOperationIntro
import org.make.front.components.operation.intro.MVEOperationIntro.MVEOperationIntroProps
import org.make.front.facades.mveLogo
import org.make.front.models._

import scala.scalajs.js

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
      logoWidth = 480,
      shareUrl = "/mieux-vivre-ensemble.html_UTM_#/FR/consultation/mieux-vivre-ensemble/selection",
      wording = js.Array(
        OperationWording(
          language = "fr",
          title = "Mieux Vivre Ensemble",
          question = "Comment mieux vivre ensemble&nbsp;?",
          learnMoreUrl = Some("https://about.make.org/about-mieux-vivre-ensemble")
        )
      ),
      extraSlides = (params: OperationExtraSlidesParams) => {
        js.Array(
          Slides.displaySequenceIntroCard(
            params,
            introWording = OperationIntroWording(duration = Some("Durée: 2 minutes"))
          ),
          Slides.displaySignUpCard(params, !params.isConnected),
          Slides.displayProposalPushCard(params, displayed = false),
          Slides.displayFinalCard(params)
        )
      },
      headerComponent = MVEOperationIntro.reactClass,
      headerProps = (operation) => MVEOperationIntroProps(operation)
    )
  }
}
