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

import org.make.front.models._

import scala.scalajs.js

object WeEuropeansOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    country = "FR",
    slug = "weeuropeans",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "WeEuropeans",
        question = "Comment réinventer l'europe, concrètement ?",
        learnMoreUrl = Some("https://www.weeuropeans.eu/fr/fr/about"),
        presentation = None,
        registerTitle = None,
        legalNote = Some(
          """En vous inscrivant, vous acceptez nos
            | <a href=\\\"https://about.make.org/conditions-dutilisation\\\" target=\\\"_blank\\\">conditions générales d'utilisation</a>
            | ainsi que de recevoir ponctuellement des emails de&nbsp;Make.org ou de nos partenaires associatifs.""".stripMargin
        )
      )
    ),
    color = "#3839ca",
    gradient = Some(GradientColor("#3839ca", "#3839ca")),
    logoUrl = "",
    whiteLogoUrl = "",
    logoWidth = 0,
    shareUrl = "/FR/consultation/weeuropeans/selection_UTM_&language=fr#/FR/consultation/weeuropeans/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(
          params = params,
          introWording = OperationIntroWording(explanation2 = Some("Les meilleures détermineront nos actions."))
        ),
        Slides.displaySignUpCard(params = params, displayed = !params.isConnected),
        Slides.displayProposalPushCard(params),
        Slides.redirectToConsultationCard(params, onFocus = () => {
          params.closeSequence()
        })
      )
    },
    startDateActions = None,
    partners = js.Array(),
    featureSettings = FeatureSettings(action = false, share = true),
    showCase = false
  )
}
