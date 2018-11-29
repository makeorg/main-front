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
import org.make.front.facades.{mipimLogo, mipimPartnersLogo}
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
        ),
        registerTitle = Some("JE M'INSCRIS POUR ÊTRE INFORMÉ(E) DES RÉSULTATS DE LA CONSULTATION")
      )
    ),
    color = "#c42a3c",
    gradient = Some(GradientColor("#000000", "#000000")),
    logoUrl = mipimLogo.toString,
    whiteLogoUrl = mipimLogo.toString,
    logoWidth = 413,
    shareUrl = "/FR/consultation/villededemain/selection_UTM_&language=fr#/FR/consultation/villededemain/selection",
    extraSlides = (params: OperationExtraSlidesParams) => {
      js.Array(
        Slides.displaySequenceIntroCard(
          params,
          introWording = OperationIntroWording(
            explanation2 = Some("Les + soutenues seront transformées en projets d'action lors du MIPIM 2019."),
            partners = js.Array(OperationIntroPartner(name = "MIPIM", imageUrl = mipimPartnersLogo.toString))
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
    operationTypeRibbon = None,
    featureSettings = FeatureSettings(action = false, share = true),
    initiators = js.Array(OperationInitiator(name = "MIPIM", imageUrl = mipimPartnersLogo.toString)),
    showCase = false
  )
}

object MIPIMGBOperationStaticData extends StaticDataOfOperation {
  override val data: OperationStaticData =
    MIPIMFROperationStaticData.data.copy(
      country = "GB",
      slug = "cityoftomorrow",
      wording = js.Array(
        OperationWording(
          language = "en",
          title = "How can we plan the city of tomorrow ?",
          question = "How can we plan the city of tomorrow ?",
          learnMoreUrl = Some("https://about.make.org/about-cityoftomorrow"),
          presentation = Some(
            """How to rethink the urban landscape, essentially how can we fully incorporate you in the process of
                | imagining tomorrow's city? It is the challenge we've set for ourselves by inviting you to the 2019 MIPIM.
                | Prior to the show we will organize an open consultation, in partnership with MIPIM, giving you the
                | opportunity to submit your ideas alongside thousands of other citizens. Turning the most popular ideas
                | into reality supported by a realistic roadmap.""".stripMargin
          ),
          registerTitle = Some("Sign up with your email address")
        )
      ),
      shareUrl = "/GB/consultation/cityoftomorrow/selection_UTM_&language=en#/GB/consultation/cityoftomorrow/selection",
      extraSlides = (params: OperationExtraSlidesParams) => {
        js.Array(
          Slides.displaySequenceIntroCard(
            params,
            introWording = OperationIntroWording(
              title = Some("Thousands of citizens are submitting solution."),
              explanation1 = Some("Take a stand on these solutions and offer yours"),
              explanation2 = Some("The ones with the most support will determine future actions"),
              partners = js.Array(OperationIntroPartner(name = "MIPIM", imageUrl = mipimPartnersLogo.toString))
            )
          ),
          Slides.displaySignUpCard(
            params = params,
            displayed = !params.isConnected,
            registerTitle = Some("Stay tuned on the actions implemented"),
            nextCta = Some("No thanks")
          ),
          Slides.displayProposalPushCard(params),
          Slides.redirectToConsultationCard(params, onFocus = () => {
            params.closeSequence()
          })
        )
      }
    )
}
