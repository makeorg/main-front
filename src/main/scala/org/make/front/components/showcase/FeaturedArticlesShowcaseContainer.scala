package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.facades.{
  mveShowcase,
  mveShowcaseX2,
  tromplinShowcase,
  tromplinShowcaseX2,
  vffShowcase,
  vffShowcaseX2
}
import org.make.front.models.{FeaturedArticle => FeaturedArticleModel}

object FeaturedArticlesShowcaseContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(FeaturedArticlesShowcase.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => FeaturedArticlesShowcase.FeaturedArticlesShowcaseProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      FeaturedArticlesShowcase.FeaturedArticlesShowcaseProps(
        articles = Seq(
          FeaturedArticleModel(
            illUrl = mveShowcase.toString,
            ill2xUrl = mveShowcaseX2.toString,
            imageAlt = Some("Votez et proposez des solutions pour mieux vivre ensemble."),
            label = "Grande cause Make.org",
            excerpt = "Votez et proposez des solutions pour mieux vivre ensemble.",
            seeMoreLabel = "Participer",
            seeMoreLink =
              "https://make.org/?utm_source=core&utm_medium=push-hp&utm_campaign=mve#/fr/consultation/mieux-vivre-ensemble/selection"
          ),
          FeaturedArticleModel(
            illUrl = vffShowcase.toString,
            ill2xUrl = vffShowcaseX2.toString,
            imageAlt = Some("Grâce aux idées issues de la consultation, 8 actions nationales ont été identifiées."),
            label = "Grande cause Make.org",
            excerpt = "Grâce aux idées issues de la consultation, 8 actions nationales ont été identifiées.",
            seeMoreLabel = "Voir le plan d'actions",
            seeMoreLink = "https://stopvff.make.org/about-vff"
          ),
          FeaturedArticleModel(
            illUrl = tromplinShowcase.toString,
            ill2xUrl = tromplinShowcaseX2.toString,
            imageAlt = Some("La \"Dotation tremplin\" devient une proposition de loi !"),
            label = "Action en cours",
            excerpt = "La \"Dotation tremplin\" devient une proposition de loi !",
            seeMoreLabel = "En savoir +",
            seeMoreLink = "https://about.make.org/post/dotation-tremplin-dot"
          )
        )
      )
    }
}
