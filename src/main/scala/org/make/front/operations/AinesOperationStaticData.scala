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
  ainesLogo,
  ainesLogoWhite,
  armeeDuSalut,
  associationFranceDependance,
  careit,
  croixRouge,
  klesia,
  korian,
  laposte,
  lesTalentsDalphonse,
  ocirp,
  siel
}
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
        learnMoreUrl = Some("https://about.make.org/about-aines"),
        presentation = Some(
          """Make.org lance avec le ministère des Solidarités et de la Santé une vaste consultation citoyenne en ligne autour de la question Comment mieux prendre soin de nos aînés ?
            |Grâce au soutien des partenaires fondateurs Klesia, la fondation Korian, La Poste, l'OCIRP, Primonial, CAREIT, la Croix-Rouge française, les milliers de propositions citoyennes
            |recueillies vont nous permettre de bâtir, aux côtés de l’État, le 1er grand Plan d'actions citoyen en faveur des Aînés. Entreprises, associations,
            |pouvoirs publics, citoyens, nous nous donnons trois ans pour concrétiser les actions de ce Plan et réussir ainsi à mieux prendre soin de nos aînés.""".stripMargin
        ),
        registerTitle = None,
        legalNote = Some(
          """En vous inscrivant, vous acceptez nos
            | <a href=\\\"https://about.make.org/conditions-dutilisation\\\" target=\\\"_blank\\\">conditions générales d'utilisation</a>
            | ainsi que de recevoir ponctuellement des emails de&nbsp;Make.org ou de nos partenaires associatifs.""".stripMargin
        )
      )
    ),
    color = "#9C6BDA",
    gradient = Some(GradientColor("#9c6bda", "#8ddcf6")),
    logoUrl = ainesLogo.toString,
    whiteLogoUrl = ainesLogoWhite.toString,
    logoWidth = 295,
    shareUrl = "/FR/consultation/aines/selection_UTM_&language=fr#/FR/consultation/aines/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(params = params, introWording = OperationIntroWording()),
        Slides.displaySignUpCard(params = params, displayed = !params.isConnected),
        Slides.displayProposalPushCard(params, false),
        Slides.redirectToConsultationCard(params, onFocus = () => {
          params.closeSequence()
        })
      )
    },
    startDateActions = None,
    partners = js
      .Array(
        OperationPartner(name = "Klesia", imageUrl = klesia.toString, isFounder = true),
        OperationPartner(name = "Fondation Korian pour le Bien-vieillir", imageUrl = korian.toString, isFounder = true),
        OperationPartner(name = "La Poste", imageUrl = laposte.toString, isFounder = true),
        OperationPartner(name = "OCIRP", imageUrl = ocirp.toString, isFounder = true),
        OperationPartner(name = "CAREIT", imageUrl = careit.toString, isFounder = true),
        OperationPartner(name = "Les Talents d'Alphonse", imageUrl = lesTalentsDalphonse.toString, isFounder = false),
        OperationPartner(name = "Siel Bleu", imageUrl = siel.toString, isFounder = false),
        OperationPartner(name = "Fondation de l'Armée du Salut", imageUrl = armeeDuSalut.toString, isFounder = false),
        OperationPartner(name = "Croix-Rouge", imageUrl = croixRouge.toString, isFounder = false),
        OperationPartner(
          name = "Association France Dépendance",
          imageUrl = associationFranceDependance.toString,
          isFounder = false
        )
      ),
    featureSettings = FeatureSettings(action = true, share = true)
  )
}
