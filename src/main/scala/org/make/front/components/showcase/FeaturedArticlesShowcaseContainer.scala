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
import org.make.front.facades.{ainesShowcase, cajShowcase, cultureShowcase, granddebatShowcase}
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
            imageAlt = Some("Comment mieux vivre ensemble ?"),
            label = "Actualité",
            excerpt =
              "Parallèlement au Grand débat national, plusieurs médias lancent, avec Make.org, une grande consultation citoyenne.",
            seeMoreLabel = "Participer à la consultation",
            seeMoreLink = "https://about.make.org/grande-consultation-nationale-quelles-sont-vos-propositions"
          ),
          FeaturedArticleModel(
            illUrl = ainesShowcase.toString,
            imageAlt = Some("Comment mieux prendre soin de nos aînés ?"),
            label = "Consultation terminée",
            excerpt = "Découvrez les idées qui ont émergé de notre consultation sur les aînés.",
            seeMoreLabel = "En savoir + sur la consultation",
            seeMoreLink = "https://about.make.org/about-aines"
          ),
          FeaturedArticleModel(
            illUrl = cultureShowcase.toString,
            imageAlt = Some("Accès à la culture pour tous"),
            label = "Consultation terminée",
            excerpt = "Découvrez les idées qui ont émergé de notre consultation.",
            seeMoreLabel = "En savoir + sur la consultation",
            seeMoreLink = "https://about.make.org/about-culture"
          ),
          FeaturedArticleModel(
            illUrl = cajShowcase.toString,
            imageAlt = Some("Une chance pour chaque jeune"),
            label = "Consultation terminée",
            excerpt = "Découvrez les idées qui ont émergé de notre consultation.",
            seeMoreLabel = "En savoir + sur la consultation",
            seeMoreLink = "https://about.make.org/about-chance-aux-jeunes"
          )
        )
      )
    }
}
