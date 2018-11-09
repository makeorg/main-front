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
import org.make.front.facades.{
  metropole,
  metropoleInitiator,
  niceMatin,
  niceMatinInitiator,
  planClimatAirEnergieTerritorial,
  planClimatAirEnergieTerritorialInitiator,
  planClimatLogoWhite
}
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
        presentation =
          Some("""La Métropole Nice Côte d'Azur, Nice-Matin et Make.org lançent une vaste consultation citoyenne en ligne autour
            | de la question “Comment lutter ensemble contre le réchauffement climatique ?”. Citoyens et associations,
            | bienvenue sur la plateforme collaborative du Plan Climat air-énergie territorial. Face aux enjeux climatiques,
            | la Métropole Nice Côte d’Azur, accompagné de Nice-Matin et Make.org, s’engage avec vous pour réduire collectivement
            | nos émissions de gaz à effet de serre.""".stripMargin),
        registerTitle = Some("JE M'INSCRIS POUR ÊTRE INFORMÉ(E) DES RÉSULTATS DE LA CONSULTATION")
      )
    ),
    color = "#8c3783",
    gradient = Some(GradientColor("#00B1BA", "#00B1BA")),
    logoUrl = planClimatLogoWhite.toString,
    whiteLogoUrl = planClimatLogoWhite.toString,
    logoWidth = 364,
    shareUrl = "/FR/consultation/plan-climat/selection_UTM_&language=fr#/FR/consultation/plan-climat/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(
          params,
          introWording = OperationIntroWording(
            explanation2 = Some("Les + soutenues seront reprises dans le Plan Climat."),
            partners = js.Array(
              OperationIntroPartner(name = "Nice Matin", imageUrl = niceMatin.toString),
              OperationIntroPartner(
                name = "Plan climat air énergie territorial",
                imageUrl = planClimatAirEnergieTerritorial.toString
              ),
              OperationIntroPartner(name = "Métropole Nice Côte D'Azur", imageUrl = metropole.toString)
            )
          )
        ),
        Slides.displaySignUpCard(
          params = params,
          displayed = !params.isConnected,
          registerTitle = Some("Soyez informés des résultats de la consultation")
        ),
        Slides.displayProposalPushCard(params),
        Slides.redirectToConsultationCard(params, onFocus = () => {
          params.closeSequence()
        })
      )
    },
    startDateActions = None,
    partners = js.Array(),
    operationTypeRibbon = None,
    featureSettings = FeatureSettings(action = false, share = true),
    initiators = js.Array(
      OperationInitiator(name = "Nice matin", imageUrl = niceMatinInitiator.toString),
      OperationInitiator(
        name = "Plan climat air énergie territorial",
        imageUrl = planClimatAirEnergieTerritorialInitiator.toString
      ),
      OperationInitiator(name = "Métropole Nice  Côte D'Azur", imageUrl = metropoleInitiator.toString)
    ),
    showCase = false
  )
}
