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

package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.facades.{
  ainesShowcase,
  chanceAuxJeunesShowcase,
  granddebatShowcase,
  mebShowcase,
  vffShowcase,
  weeuropeansShowcase
}
import org.make.front.models.{FeaturedArticle => FeaturedArticleModel}

import scala.scalajs.js

object FeaturedArticlesShowcaseContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(FeaturedArticlesShowcase.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => FeaturedArticlesShowcase.FeaturedArticlesShowcaseProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      FeaturedArticlesShowcase.FeaturedArticlesShowcaseProps(
        articles = js.Array(
          FeaturedArticleModel(
            illUrl = granddebatShowcase.toString,
            imageAlt = Some("Grand débat national"),
            label = "CONSULTATION TERMINÉE",
            excerpt = "Découvrez la première synthèse du Grand Débat national faite par les Français eux-mêmes.",
            seeMoreLabel = "Découvrir les résultats",
            seeMoreLink = "https://about.make.org/grande-consultation-nationale-quelles-sont-vos-propositions"
          ),
          FeaturedArticleModel(
            illUrl = chanceAuxJeunesShowcase.toString,
            imageAlt = Some("Comment donner une chance à chaque jeune ?"),
            label = "CONSULTATION TERMINÉE",
            excerpt = "Découvrez les résultats de notre consultation pour donner une chance à chaque jeune.",
            seeMoreLabel = "Découvrir les résultats",
            seeMoreLink = "https://about.make.org/about-chance-aux-jeunes"
          ),
          FeaturedArticleModel(
            illUrl = weeuropeansShowcase.toString,
            imageAlt = Some("Comment réinventer l'Europe, concrétement ?"),
            label = "Consultation terminée",
            excerpt =
              "Après 5 semaines de consultation, découvrez les 10 priorités sélectionnées par les citoyens européens.",
            seeMoreLabel = "Découvrir l’Agenda citoyen",
            seeMoreLink = "https://weeuropeans.eu/"
          ),
          FeaturedArticleModel(
            illUrl = vffShowcase.toString,
            imageAlt = Some("Grâce aux idées issues de la consultation, 8 actions nationales ont été identifiées."),
            label = "Actions",
            excerpt =
              "À l’occasion de la Journée internationale des droits des Femmes, découvrez l’état d’avancement de nos actions.",
            seeMoreLabel = "Voir le plan d'actions",
            seeMoreLink = "https://about.make.org/about-vff"
          ),
          FeaturedArticleModel(
            illUrl = mebShowcase.toString,
            imageAlt = Some("Comment agir pour rendre notre économie plus bienveillante ?"),
            label = "Consultation terminée",
            excerpt = "En attendant la publication des résultats, redécouvrez les enjeux de cette consultation.",
            seeMoreLabel = "En savoir plus",
            seeMoreLink = "https://about.make.org/about-economiebienveillante"
          ),
          FeaturedArticleModel(
            illUrl = ainesShowcase.toString,
            imageAlt = Some("Comment mieux prendre soin de nos aînés ?"),
            label = "Consultation terminée",
            excerpt = "Découvrez les idées qui ont émergé de notre consultation sur les aînés.",
            seeMoreLabel = "Découvrir les résultats",
            seeMoreLink = "https://about.make.org/about-aines"
          )
        )
      )
    }
}
