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
    logoWidth = 504,
    shareUrl = "/lpae.html_UTM_#/FR/consultation/lpae/selection",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "La Parole aux Etudiants",
        question = "Vous avez les cl&eacute;s du monde, que changez-vous&nbsp;?",
        learnMoreUrl = Some("https://about.make.org/about-lpae"),
        presentation = Some(
          "Dans une société bousculée, disruptée et en permanente évolution cette consultation est l’occasion pour vous d’exprimer ce que vous voulez changer, de proposer vos idées pour la société de demain, de dessiner les contours du Monde dans lequel vous souhaitez vivre. Vous avez entre 18 et 28 ans, vous avez des choses à dire, c’est le moment&nbsp;!"
        ),
        registerTitle = None
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
    startDateActions = None,
    featureSettings = FeatureSettings(action = true, share = true),
    showCase = false
  )
}
