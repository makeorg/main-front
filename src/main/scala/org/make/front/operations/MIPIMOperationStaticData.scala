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
import org.make.front.facades.mipimLogo
import org.make.front.models._

import scala.scalajs.js

object MIPIMFROperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    country = "FR",
    slug = "villededemain",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "La ville de demain",
        question = "Comment imaginer la ville de demain ? ",
        learnMoreUrl = Some("https://about.make.org/about-villededemain"),
        presentation = Some(
          """Comment imaginer la ville de demain en vous incluant dans la démarche ? C’est le grand défi citoyen auquel
            | nous vous invitons à l’occasion du MIPIM 2019. Nous allons vous proposer de vous engager massivement en
            | amont de l’édition 2019 du salon, grâce à une consultation citoyenne ouverte, puis nous transformerons vos
            | meilleures idées en projets d’actions concrets. """.stripMargin
        )
      )
    ),
    color = "#000000",
    gradient = Some(GradientColor("#000000", "#000000")),
    logoUrl = mipimLogo.toString,
    whiteLogoUrl = mipimLogo.toString,
    logoWidth = 413,
    shareUrl = "/FR/consultation/villededemain/selection_UTM_&language=fr#/FR/consultation/villededemain/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(params, introWording = OperationIntroWording()),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.redirectToConsultationCard(params, onFocus = () => {
          params.closeSequence()
        })
      )
    },
    startDateActions = None,
    partners = js.Array(),
    operationTypeRibbon = None,
    featureSettings = FeatureSettings(action = false, share = true)
  )
}
