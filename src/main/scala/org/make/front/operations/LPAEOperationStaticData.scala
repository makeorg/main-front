package org.make.front.operations

import org.make.front.components.operation.intro.LPAEOperationIntro
import org.make.front.components.operation.intro.LPAEOperationIntro.LPAEOperationIntroProps
import org.make.front.facades._
import org.make.front.models._

import scala.scalajs.js

object LPAEOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    slug = "lpae",
    country = "FR",
    color = "#602a7a",
    gradient = Some(GradientColor("#683577", "#782f8b")),
    logoUrl = lpaeLogo.toString,
    whiteLogoUrl = lpaeLogoWhite.toString,
    shareUrl = "/lpae.html_UTM_#/FR/consultation/lpae/selection",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "La Parole aux Etudiants",
        question = "Vous avez les cl&eacute;s du monde, que changez-vous&nbsp;?",
        learnMoreUrl = Some("https://about.make.org/about-lpae")
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
    headerComponent = LPAEOperationIntro.reactClass,
    headerProps = (operation) => LPAEOperationIntroProps(operation)
  )
}
