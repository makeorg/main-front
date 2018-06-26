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
import org.make.front.components.operation.intro.ClimatParisOperationIntro
import org.make.front.components.operation.intro.ClimatParisOperationIntro.ClimatParisOperationIntroProps
import org.make.front.components.operation.partners.ClimatParisOperationPartners
import org.make.front.facades._
import org.make.front.models._
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object ClimatParisOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    slug = "climatparis",
    country = "FR",
    color = "#459ba6",
    gradient = Some(GradientColor("#bfe692", "#69afde")),
    logoUrl = climatParisLogo.toString,
    whiteLogoUrl = climatParisLogoWhite.toString,
    shareUrl = "/climatparis.html_UTM_#/FR/consultation/climatparis/selection",
    logoWidth = 358,
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "Climat Paris",
        question = "Comment lutter contre le changement climatique &agrave; Paris&nbsp;?",
        learnMoreUrl = Some("https://climatparis.make.org/about-climatparis"),
        presentation = Some(
          "Les changements climatiques sont au coeur de l’actualité politique et internationale. La COP21 a démontré la volonté des décideurs politiques d’avancer. Un changement de comportement de chaque citoyen est maintenant nécessaire : à nous de transformer la prise de conscience planétaire en idées concrètes pour changer notre rapport à la planète."
        )
      )
    ),
    extraSlides = (params: OperationExtraSlidesParams) => {

      val trackingContext = TrackingContext(TrackingLocation.sequencePage, Some(params.operation.slug))
      val defaultTrackingParameters = Map("sequenceId" -> params.sequence.sequenceId.value)

      js.Array(
        Slides.displaySequenceIntroCard(params, introWording = OperationIntroWording()),
        Slides.displaySignUpCard(params, displayed = !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.displayFinalCard(params)
      )
    },
    headerComponent = ClimatParisOperationIntro.reactClass,
    partnersComponent = ClimatParisOperationPartners.reactClass,
    headerProps = (operation) => ClimatParisOperationIntroProps(operation),
    startDateActions = None
  )
}
