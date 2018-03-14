package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{FeaturedArticle => FeaturedArticleModel}

object FeaturedArticlesShowcaseContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(FeaturedArticlesShowcase.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => FeaturedArticlesShowcase.FeaturedArticlesShowcaseProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      FeaturedArticlesShowcase.FeaturedArticlesShowcaseProps(
        articles = Seq(
          FeaturedArticleModel(
            illUrl = "images/showcase/vff_showcase.png",
            ill2xUrl = "images/showcase/vff_showcase@x2.png",
            imageAlt = Some("Grâce aux idées issues de la consultation, 8 actions nationales ont été identifiées."),
            label = "Grande cause Make.org",
            excerpt = "Grâce aux idées issues de la consultation, 8 actions nationales ont été identifiées.",
            seeMoreLabel = "Voir le plan d'actions",
            seeMoreLink = "https://stopvff.make.org/about-vff"
          ),
          FeaturedArticleModel(
            illUrl = "images/showcase/endometri_showcase.png",
            ill2xUrl = "images/showcase/endometri_showcase@x2.png",
            imageAlt = Some("Endométri - Ose faire valoir tes droits"),
            label = "Action en cours",
            excerpt = "Endométri - Ose faire valoir tes droits",
            seeMoreLabel = "En savoir +",
            seeMoreLink = "https://about.make.org/post/endometri-ose-le-dire"
          ),
          FeaturedArticleModel(
            illUrl = "images/showcase/tromplin_showcase.png",
            ill2xUrl = "images/showcase/tromplin_showcase@x2.png",
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
