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

import org.make.front.facades.{chanceAuxJeunesLogo, chanceAuxJeunesLogoWhite}
import org.make.front.models._

import scala.scalajs.js

object ChanceAuxJeunesOperationStaticData extends StaticDataOfOperation {

  override val data: OperationStaticData = {
    OperationStaticData(
      slug = "chance-aux-jeunes",
      country = "FR",
      color = "#2999d5",
      gradient = Some(GradientColor("#2999D5", "#B6D0CE")),
      logoUrl = chanceAuxJeunesLogo.toString,
      whiteLogoUrl = chanceAuxJeunesLogoWhite.toString,
      shareUrl =
        "/FR/consultation/chance-aux-jeunes/selection_UTM_&language=fr#/FR/consultation/chance-aux-jeunes/selection",
      logoWidth = 446,
      wording = js.Array(
        OperationWording(
          language = "fr",
          title = "Une chance pour chaque jeune",
          question = "Comment donner une chance à chaque jeune&nbsp;?",
          learnMoreUrl = Some("http://www.UneChancePourChaqueJeune.org"),
          presentation = Some(
            "Précarité, chômage, inégalités, désamour du politique... La France ne semble plus offrir de place à ses jeunes. Cette fracture fragilise notre capacité à relever les défis collectifs futurs et nous pousse à imaginer ensemble un moyen d'offrir à chaque jeune une chance de se réaliser."
          )
        )
      ),
      extraSlides = (params: OperationExtraSlidesParams) => {
        js.Array(
          Slides.displaySequenceIntroCard(
            params = params,
            introWording = OperationIntroWording(
              title = Some("Ensemble, changeons l'avenir des jeunes !"),
              explanation1 = Some("Éducation, logement, emploi, pouvoir d'achat... nous devons trouver des solutions."),
              explanation2 =
                Some("Les propositions les + soutenues seront mises en action par Make.org et ses partenaires.")
            )
          ),
          Slides.displaySignUpCard(params, !params.isConnected),
          Slides.displayProposalPushCard(params, displayed = false),
          Slides.displayFinalCard(params)
        )
      },
      startDateActions = None,
      featureSettings = FeatureSettings(action = true, share = true)
    )
  }
}
