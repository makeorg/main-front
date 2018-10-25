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
import org.make.front.facades.{croissancePlusLogo, europeanDigitalChampionsLogoWhite, g9PlusLogo, rolandBergerLogo}
import org.make.front.models._

import scala.scalajs.js

object G9OperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    country = "FR",
    slug = "european-digital-champions",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "Comment faire émerger des champions européens du numérique ?",
        question = "Comment faire émerger des champions européens du numérique ? ",
        learnMoreUrl = Some("https://about.make.org/about-european-digital-champions"),
        presentation = Some(
          """Les élections européennes offrent une formidable opportunité d’un grand réveil numérique européen.
            | Comment faire émerger les champions européens du numérique ? Pour la première fois, l’Institut G9+,
            | Roland Berger, Croissance+ et Make.org vont massivement mobiliser citoyens français et allemands sur
            | cette question, pour écrire un Livre blanc inédit, première véritable feuille de route citoyenne sur le sujet.""".stripMargin
        ),
        registerTitle = Some("JE M'INSCRIS POUR ÊTRE INFORMÉ(E) DES RÉSULTATS DE LA CONSULTATION")
      )
    ),
    color = "#5DA113",
    gradient = Some(GradientColor("#7DB7E3", "#5DA113")),
    logoUrl = europeanDigitalChampionsLogoWhite.toString,
    whiteLogoUrl = europeanDigitalChampionsLogoWhite.toString,
    logoWidth = 375,
    shareUrl =
      "/FR/consultation/european-digital-champions/selection_UTM_&language=fr#/FR/consultation/european-digital-champions/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(
          params,
          introWording = OperationIntroWording(
            title = Some("Des milliers de citoyens européens proposent des solutions."),
            explanation2 = None,
            partners = js.Array(
              OperationIntroPartner(name = "G9+", imageUrl = g9PlusLogo.toString),
              OperationIntroPartner(name = "Roland Berger", imageUrl = rolandBergerLogo.toString),
              OperationIntroPartner(name = "CroissancePlus", imageUrl = croissancePlusLogo.toString)
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
    featureSettings = FeatureSettings(action = false, share = true)
  )
}
