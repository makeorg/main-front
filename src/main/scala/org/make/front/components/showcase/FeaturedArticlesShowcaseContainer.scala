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
import org.make.front.facades.{cajShowcase, cajShowcaseX2, mveShowcase, mveShowcaseX2, vffShowcase, vffShowcaseX2}
import org.make.front.models.{FeaturedArticle => FeaturedArticleModel}

import scala.scalajs.js

object FeaturedArticlesShowcaseContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(FeaturedArticlesShowcase.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => FeaturedArticlesShowcase.FeaturedArticlesShowcaseProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      FeaturedArticlesShowcase.FeaturedArticlesShowcaseProps(
        articles = js.Array(
          FeaturedArticleModel(
            illUrl = cajShowcase.toString,
            ill2xUrl = cajShowcaseX2.toString,
            imageAlt = Some("Une chance pour chaque jeune"),
            label = "Consultation terminée",
            excerpt = "Découvrez prochainement les idées qui ont émergé de notre consultation.",
            seeMoreLabel = "En savoir + sur la consultation",
            seeMoreLink = "https://about.make.org/about-chance-aux-jeunes"
          ),
          FeaturedArticleModel(
            illUrl = mveShowcase.toString,
            ill2xUrl = mveShowcaseX2.toString,
            imageAlt = Some("Comment mieux vivre ensemble ?"),
            label = "Consultation terminée",
            excerpt = "Découvrez prochainement les idées qui ont émergé de notre consultation.",
            seeMoreLabel = "En savoir + sur la consultation",
            seeMoreLink = "https://about.make.org/about-mieux-vivre-ensemble"
          ),
          FeaturedArticleModel(
            illUrl = vffShowcase.toString,
            ill2xUrl = vffShowcaseX2.toString,
            imageAlt = Some("Stop aux violences faîtes aux femmes"),
            label = "Actions",
            excerpt = "Grâce aux idées issues de la consultation, 8 actions nationales ont été identifiées.",
            seeMoreLabel = "Voir le plan d'actions",
            seeMoreLink = "https://about.make.org/about-vff"
          )
        )
      )
    }
}
