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
  croissancePlusLogo,
  europeanDigitalChampionsLogoWhite,
  europeanDigitalChampionsLogoWhiteDE,
  g9PlusLogo,
  rolandBergerLogo
}
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
            title = Some("#entrepreneuriat  #europe  #investissement  #fiscalité"),
            explanation1 = Some("Aidez nous à faire émerger des champions européens du numérique"),
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
    partners = js.Array(
      OperationPartner(name = "G9+", imageUrl = g9PlusLogo.toString, isFounder = true),
      OperationPartner(name = "Roland Berger", imageUrl = rolandBergerLogo.toString, isFounder = true),
      OperationPartner(name = "CroissancePlus", imageUrl = croissancePlusLogo.toString, isFounder = true)
    ),
    operationTypeRibbon = None,
    featureSettings = FeatureSettings(action = false, share = true),
    initiators = js.Array(
      OperationInitiator(name = "G9+", imageUrl = g9PlusLogo.toString),
      OperationInitiator(name = "Roland Berger", imageUrl = rolandBergerLogo.toString),
      OperationInitiator(name = "CroissancePlus", imageUrl = croissancePlusLogo.toString)
    ),
    showCase = false
  )
}

object G9DEOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData =
    G9OperationStaticData.data.copy(
      country = "DE",
      wording = js.Array(
        OperationWording(
          language = "de",
          title = "Wie kann man europäische digitale Champions hervorbringen?",
          question = "Wie kann man europäische digitale Champions hervorbringen?",
          learnMoreUrl = Some("https://about.make.org/about-digital-champions-de"),
          presentation = Some(
            """Die Europawahlen bieten eine enorme Chance für ein großes europäisches digitales Erwachen. Wie kann man
              | die europäischen Digitalmeister hervorheben? Zum ersten Mal werden das G9+ Institut, Roland Berger,
              | Croissance+ und Make.org die französischen und deutschen Bürger zu diesem Thema massiv mobilisieren, um
              | ein unveröffentlichtes Weißbuch zu schreiben, die erste echte Bürger-Roadmap zu diesem Thema.""".stripMargin
          ),
          registerTitle = Some("Ich melde mich mit meiner E-Mail Adresse an.")
        )
      ),
      logoUrl = europeanDigitalChampionsLogoWhiteDE.toString,
      whiteLogoUrl = europeanDigitalChampionsLogoWhiteDE.toString,
      shareUrl =
        "/DE/consultation/european-digital-champions/selection_UTM_&language=fr#/DE/consultation/european-digital-champions/selection",
      extraSlides = (params: OperationExtraSlidesParams) => {
        js.Array(
          Slides.displaySequenceIntroCard(
            params,
            introWording = OperationIntroWording(
              title = Some("Tausende Europäer schlagen Lösungen vor."),
              explanation1 = Some("Nehmen Sie Stellung und schlagen Sie Ihre eigenen vor."),
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
            registerTitle = Some("Bleiben Sie über die Ergebnisse der Konsultation informiert."),
            nextCta = Some("Nein danke, ich möchte nicht über die Ergebnisse informiert werden.")
          ),
          Slides.displayProposalPushCard(params),
          Slides.redirectToConsultationCard(params, onFocus = () => {
            params.closeSequence()
          })
        )
      },
      showCase = false
    )
}
