package org.make.front.operations
import org.make.front.facades.{VFFGBLogo, VFFGBWhiteLogo, VFFITLogo, VFFITWhiteLogo, VFFLogo, VFFWhiteLogo}
import org.make.front.models._
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext

object VFFOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    slug = "vff",
    country = "FR",
    color = "#660779",
    gradient = Some(GradientColor("#AB92CA", "#54325A")),
    logoUrl = VFFLogo.toString,
    whiteLogoUrl = VFFWhiteLogo.toString,
    shareUrl = "/vff.html_UTM_#/FR/consultation/vff/selection",
    wording = Seq(
      OperationWording(
        language = "fr",
        title = "Stop aux Violences Faites aux&nbsp;Femmes",
        question = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
        learnMoreUrl = Some("https://stopvff.make.org/about-vff")
      )
    ),
    extraSlides = (params: OperationExtraSlidesParams) => {
      Seq(
        Slides.displaySequenceIntroCard(params, displayed = false),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.displayFinalCard(params)
      )
    }
  )
}

object VFFITOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = VFFOperationStaticData.data.copy(
    country = "IT",
    logoUrl = VFFITLogo.toString,
    whiteLogoUrl = VFFITWhiteLogo.toString,
    shareUrl = "/vff-it.html_UTM_#/IT/consultation/vff/selection",
    wording = Seq(
      OperationWording(
        language = "it",
        title = "Stop alla violenza sulle donne",
        question = "Come far fronte alla violenza sulle donne?",
        learnMoreUrl = Some("https://about.make.org/it/about-vff")
      )
    ),
    extraSlides = (params: OperationExtraSlidesParams) => {
      val trackingContext = TrackingContext(TrackingLocation.sequencePage, Some(params.operation.slug))
      val defaultTrackingParameters = Map("sequenceId" -> params.sequence.sequenceId.value)
      Seq(
        Slides.displaySequenceIntroCard(params),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.displayFinalCard(params)
      )
    }
  )
}

object VFFGBOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = VFFOperationStaticData.data.copy(
    country = "GB",
    logoUrl = VFFGBLogo.toString,
    whiteLogoUrl = VFFGBWhiteLogo.toString,
    shareUrl = "/vff-gb.html_UTM_#/GB/consultation/vff/selection",
    wording = Seq(
      OperationWording(
        language = "en",
        title = "Stop violence against women",
        question = "How to combat violence against women?",
        learnMoreUrl = Some("https://about.make.org/gb/about-vff")
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
