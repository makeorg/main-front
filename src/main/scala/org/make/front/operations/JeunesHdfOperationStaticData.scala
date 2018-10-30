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

import org.make.front.components.authenticate.register.SignUpField
import org.make.front.facades.{hdfLogo, jeunesHdfLogo}
import org.make.front.models._

import scala.scalajs.js

object JeunesHdfOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    country = "FR",
    slug = "jeunesse-hautsdefrance",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "Construire l’avenir des jeunes en Hauts-de-France ",
        question = "Comment vous aider à construire votre avenir en Hauts-de-France ?",
        learnMoreUrl = Some("https://about.make.org/about-jeunesse-hautsdefrance"),
        presentation = Some(
          """
            |15/30 ans, vous habitez en #HDF, faites entendre votre voix !
            |
            |Les Hauts-de-France vont initier avec Make.org une consultation totalement inédite : plusieurs dizaines de
            | milliers de jeunes vont en effet pouvoir répondre très librement à la question
            | « Comment vous aider à construire votre avenir en Hauts-de-France ? ». Les résultats de la consultation
            | seront le point de départ d’une mobilisation plus ample de toutes les forces vives de la région,
            | débouchant sur un plan d’actions concrètes. """.stripMargin
        ),
        registerTitle = Some("JE M'INSCRIS POUR ÊTRE INFORMÉ(E) DES RÉSULTATS DE LA CONSULTATION")
      )
    ),
    color = "#3dbfff",
    gradient = Some(GradientColor("#fea8fe", "#35bfff")),
    logoUrl = jeunesHdfLogo.toString,
    whiteLogoUrl = jeunesHdfLogo.toString,
    logoWidth = 463,
    shareUrl =
      "/FR/consultation/jeunesse-hautsdefrance/selection_UTM_&language=fr#/FR/consultation/jeunesse-hautsdefrance/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(
          params,
          introWording = OperationIntroWording(
            title = Some("Les jeunes des Hauts-de-France proposent des solutions."),
            explanation2 = Some("Les plus soutenues seront mises en oeuvre par la Région."),
            partners = js.Array(OperationIntroPartner(name = "Région Hauts-de-France", imageUrl = hdfLogo.toString))
          )
        ),
        Slides.displaySignUpCard(
          params = params,
          displayed = !params.isConnected,
          registerTitle = Some("Soyez informés des résultats de la consultation"),
          nextCta = Some("Non merci, je ne souhaite pas être informé des résultats")
        ),
        Slides.displayProposalPushCard(params),
        Slides.redirectToConsultationCard(params, onFocus = () => {
          params.closeSequence()
        })
      )
    },
    startDateActions = None,
    partners =
      js.Array(OperationPartner(name = "Région Hauts-de-France", imageUrl = hdfLogo.toString, isFounder = true)),
    operationTypeRibbon = None,
    featureSettings = FeatureSettings(action = false, share = true),
    initiators = js.Array(OperationInitiator(name = "Région Hauts-de-France", imageUrl = hdfLogo.toString)),
    additionalFields =
      Seq(SignUpField.FirstName, SignUpField.Age, SignUpField.PostalCode, SignUpField.Csp, SignUpField.Gender)
  )
}
