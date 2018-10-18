/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.operations
import org.make.front.facades.{VFFGBLogo, VFFGBWhiteLogo, VFFITLogo, VFFITWhiteLogo, VFFLogo, VFFWhiteLogo}
import org.make.front.models._
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object VFFOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    slug = "vff",
    country = "FR",
    color = "#660779",
    gradient = Some(GradientColor("#AB92CA", "#54325A")),
    logoUrl = VFFLogo.toString,
    whiteLogoUrl = VFFWhiteLogo.toString,
    shareUrl = "/vff.html_UTM_#/FR/consultation/vff/selection",
    logoWidth = 425,
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "Stop aux Violences Faites aux&nbsp;Femmes",
        question = "Comment lutter contre les violences faites aux&nbsp;femmes&nbsp;?",
        learnMoreUrl = Some("https://stopvff.make.org/about-vff"),
        presentation = Some(
          "Les violences faites aux femmes sont au coeur de l’actualité politique et médiatique. Les mentalités sont en train de changer. Mais pour autant tout commence maintenant. À nous de transformer cette prise de conscience généralisée en actions concrètes et d’apporter une réponse décisive face à ce&nbsp;fléau."
        )
      )
    ),
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(params = params, displayed = false, introWording = OperationIntroWording()),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.displayFinalCard(params)
      )
    },
    startDateActions = None
  )
}

object VFFITOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = VFFOperationStaticData.data.copy(
    country = "IT",
    logoUrl = VFFITLogo.toString,
    whiteLogoUrl = VFFITWhiteLogo.toString,
    shareUrl = "/IT/consultation/vff/selection_UTM_&language=it#/IT/consultation/vff/selection",
    wording = js.Array(
      OperationWording(
        language = "it",
        title = "Stop alla violenza sulle donne",
        question = "Come far fronte alla violenza sulle donne?",
        learnMoreUrl = Some("https://about.make.org/it/about-vff"),
        presentation = Some(
          "La violenza sulle donne è al centro dell'attualità politica e mediatica. Le idee stanno cambiando, ma tutto inizia da qui. Sta a noi trasformare questa presa di coscienza generale in azioni concrete e dare una risposta decisiva a questo flagello."
        )
      )
    ),
    extraSlides = (params: OperationExtraSlidesParams) => {
      val trackingContext = TrackingContext(TrackingLocation.sequencePage, Some(params.operation.slug))
      val defaultTrackingParameters = Map("sequenceId" -> params.sequence.sequenceId.value)
      js.Array(
        Slides.displaySequenceIntroCard(params = params, introWording = OperationIntroWording()),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.displayFinalCard(params)
      )
    },
    startDateActions = None
  )
}

object VFFGBOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = VFFOperationStaticData.data.copy(
    country = "GB",
    logoUrl = VFFGBLogo.toString,
    whiteLogoUrl = VFFGBWhiteLogo.toString,
    shareUrl = "/GB/consultation/vff/selection_UTM_&language=en#/GB/consultation/vff/selection",
    wording = js.Array(
      OperationWording(
        language = "en",
        title = "Stop violence against women",
        question = "How to combat violence against women?",
        learnMoreUrl = Some("https://about.make.org/gb/about-vff"),
        presentation = Some(
          "Violence against women is at the heart of political events and news coverage. Mindsets are changing. But it all begins now. It is up to us to transform this generalised awareness into concrete actions and to provide a decisive response to this epidemic."
        )
      )
    ),
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(params = params, introWording = OperationIntroWording()),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.displayFinalCard(params)
      )
    },
    startDateActions = None
  )
}
