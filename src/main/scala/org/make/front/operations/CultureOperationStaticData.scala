package org.make.front.operations
import org.make.front.components.operation.intro.CultureOperationIntro
import org.make.front.components.operation.intro.CultureOperationIntro.CultureOperationIntroProps
import org.make.front.components.operation.partners.CultureOperationPartners
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
        learnMoreUrl = None,
        presentation = None
      )
    ),
    color = "#6B26E8",
    gradient = Some(GradientColor("#7921DB", "#FEC736")),
    logoUrl = cultureLogo.toString,
    whiteLogoUrl = cultureLogoWhite.toString,
    logoWidth = 295,
    shareUrl = "/culture.html_UTM_#/FR/consultation/vff/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(params, introWording = OperationIntroWording()),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.displayFinalCard(params, onFocus = () => {
          js.timers.setTimeout(3000d) {
            params.redirect(s"/${params.country}/consultationV2/${params.operation.slug}/consultation")
          }
        })
      )
    },
    headerComponent = CultureOperationIntro.reactClass,
    partnersComponent = CultureOperationPartners.reactClass,
    headerProps = (operation) => CultureOperationIntroProps(operation),
    startDateActions = None
  )
}
