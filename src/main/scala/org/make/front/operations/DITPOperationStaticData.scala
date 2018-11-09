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
import org.make.front.facades.{ditpLogo, ditpPartnerLogo, ditpPartnerSequenceLogo}
import org.make.front.models._

import scala.scalajs.js

object DITPOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData = OperationStaticData(
    country = "FR",
    slug = "ditp",
    wording = js.Array(
      OperationWording(
        language = "fr",
        title = "Agents publics : comment lever les blocages que vous rencontrez au quotidien ? ",
        question = "Agents publics : comment lever les blocages que vous rencontrez au quotidien ?",
        learnMoreUrl = Some("https://about.make.org/about-ditp"),
        presentation =
          Some("""« Agents publics : comment lever les blocages que vous rencontrez au quotidien ? ». 
              |Le ministre de l’Action et des comptes publics a choisi d’adopter une nouvelle méthode
              |pour conduire la transformation de l’État, une méthode “engageante”. Pour la première fois,
              |nous allons donner à très grande échelle la parole à tous les agents publics de France,
              |sur leur quotidien, les freins qui bloquent leur travail et les inciter à proposer leurs solutions qui,
              |demain, transformeront l’action publique.""".stripMargin),
        registerTitle = Some(" JE M'INSCRIS AVEC MON EMAIL")
      )
    ),
    color = "#ED2939",
    gradient = Some(GradientColor("#8ADCD7", "#367ABC")),
    logoUrl = ditpLogo.toString,
    whiteLogoUrl = ditpLogo.toString,
    logoWidth = 540,
    shareUrl = "/FR/consultation/ditp/selection_UTM_&language=fr#/FR/consultation/ditp/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(
          params,
          introWording = OperationIntroWording(
            title = Some("Des milliers d'agents publics proposent des solutions."),
            explanation1 = Some("Prenez position sur ces solutions ou proposez les vôtres."),
            explanation2 = Some("Vos contributions seront restituées à la DITP de manière anonyme."),
            partners = js.Array(OperationIntroPartner(name = "DITP", imageUrl = ditpPartnerSequenceLogo.toString))
          )
        ),
        Slides.displaySignUpCard(
          params = params,
          displayed = !params.isConnected,
          registerTitle = Some("Inscrivez vous pour enregistrer vos contributions"),
          nextCta = Some("Non merci, je ne souhaite pas que mes contributions soient enregistrées")
        ),
        Slides.displayProposalPushCard(params),
        Slides.redirectToConsultationCard(params, onFocus = () => {
          params.closeSequence()
        })
      )
    },
    startDateActions = None,
    operationTypeRibbon = None,
    featureSettings = FeatureSettings(action = false, share = false),
    initiators = js.Array(OperationInitiator(name = "DITP", imageUrl = ditpPartnerLogo.toString)),
    additionalFields =
      Seq(SignUpField.FirstName, SignUpField.Age, SignUpField.Job, SignUpField.PostalCode, SignUpField.HiddenOptOut),
    showCase = false
  )

}
