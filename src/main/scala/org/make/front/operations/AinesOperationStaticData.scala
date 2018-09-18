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

import org.make.front.components.consultation.partners.AinesPartnersList
import org.make.front.components.operation.intro.AinesOperationIntro
import org.make.front.components.operation.intro.AinesOperationIntro.AinesOperationIntroProps
import org.make.front.facades.{ainesLogo, ainesLogoWhite}
import org.make.front.models._

import scala.scalajs.js

object AinesOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    country = "FR",
    slug = "aines",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "Mieux prendre soin de nos aînés",
        question = "Comment mieux prendre soin de nos aînés ? ",
        learnMoreUrl = Some("https://www.make.org/aines"),
        presentation = Some(
          """"Make.org lance avec le ministère des Solidarités et de la Santé une vaste consultation citoyenne en ligne autour de la question : “Comment mieux prendre soin de nos aînés ?"".
            |Grâce au soutien des partenaires fondateurs Klésia, Korian, OCIRP, Careit et La Poste, les milliers de propositions citoyennes recueillies vont nous permettre de bâtir, aux côtés
            |de l’État, le 1er grand Plan d'actions citoyen en faveur des Aînés. Entreprises, associations, pouvoirs publics, citoyens,
            |nous nous donnons trois ans pour concrétiser les actions de ce Plan et réussir ainsi à mieux prendre soin de nos aînés.""""
        )
      )
    ),
    color = "#6B26E8",
    gradient = Some(GradientColor("#9c6bda", "#8ddcf6")),
    logoUrl = ainesLogo.toString,
    whiteLogoUrl = ainesLogoWhite.toString,
    logoWidth = 295,
    shareUrl = "/FR/consultation/aines/selection_UTM_&language=fr#/FR/aines/culture/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(params, introWording = OperationIntroWording()),
        Slides.displaySignUpCard(params, !params.isConnected),
        Slides.displayProposalPushCard(params, false),
        Slides.redirectToConsultationCard(params, onFocus = () => {
          params.closeSequence()
        })
      )
    },
    headerComponent = AinesOperationIntro.reactClass,
    partnersComponent = AinesPartnersList.reactClass,
    headerProps = operation => AinesOperationIntroProps(operation),
    startDateActions = None,
    consultationVersion = ConsultationVersion.V2
  )
}
