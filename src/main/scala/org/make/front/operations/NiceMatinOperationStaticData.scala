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
import org.make.front.facades.{niceMatin, planClimatAirEnergieTerritorial, planClimatLogoWhite}
import org.make.front.models._

import scala.scalajs.js

object NiceMatinOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    country = "FR",
    slug = "plan-climat",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "#MaSolutionPourLeClimat",
        question = "Comment lutter ensemble contre le réchauffement climatique ?",
        learnMoreUrl = Some("https://about.make.org/about-plan-climat"),
        presentation = Some(
          """Nice-Matin et la Métropole Nice Côte d'Azur lancent avec Make.org, une vaste consultation citoyenne en ligne autour de la question
            | “Comment lutter ensemble contre le réchauffement climatique ?”. Chaque jour un article publié par Nice-Matin
            | s’appuiera sur une proposition citoyenne pour traiter d’un sujet de fond. En fin de consultation,
            | les meilleures propositions seront reprises par la Métropole Nice Côte d’Azur dans le Plan Climat.""".stripMargin
        )
      )
    ),
    color = "#DA815E",
    gradient = Some(GradientColor("#FF5AB2", "#FEDF1C")),
    logoUrl = planClimatLogoWhite.toString,
    whiteLogoUrl = planClimatLogoWhite.toString,
    logoWidth = 245,
    shareUrl = "/FR/consultation/plan-climat/selection_UTM_&language=fr#/FR/consultation/plan-climat/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(
          params,
          introWording = OperationIntroWording(
            explanation2 = None,
            partners = js.Array(
              OperationIntroPartner(name = "Nice Matin", imageUrl = niceMatin.toString),
              OperationIntroPartner(
                name = "Plan climat air énergie territorial",
                imageUrl = planClimatAirEnergieTerritorial.toString
              )
            )
          )
        ),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.redirectToConsultationCard(params, onFocus = () => {
          params.closeSequence()
        })
      )
    },
    startDateActions = None,
    partners = js.Array(),
    isConsultationOnly = true,
    operationTypeRibbon = None
  )
}
