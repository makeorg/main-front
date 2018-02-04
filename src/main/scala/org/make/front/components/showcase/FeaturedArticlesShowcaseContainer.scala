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
            illUrl = "https://placekitten.com/360/200",
            ill2xUrl = "https://placekitten.com/720/400",
            imageAlt = Some("bla bla"),
            label = "action",
            excerpt = "WWF organise la Marche contre le changement climatique",
            seeMoreLabel = "En savoir +",
            seeMoreLink = "#"
          ),
          FeaturedArticleModel(
            illUrl = "https://placebear.com/360/200",
            ill2xUrl = "https://placebear.com/720/400",
            imageAlt = Some("Un million d'euros pour financer vos projets innovants"),
            label = "You made it!",
            excerpt = "Vous étiez 150 000 à soutenir cette proposition, l’AN en a fait une loi !",
            seeMoreLabel = "En savoir +",
            seeMoreLink = "#"
          ),
          FeaturedArticleModel(
            illUrl = "http://www.fillmurray.com/360/200",
            ill2xUrl = "http://www.fillmurray.com/720/400",
            imageAlt = Some("Un million d'euros pour financer vos projets innovants"),
            label = "Le maker de la semaine",
            excerpt = "ITV : Chaque semaine, Make part à la rencontre d’un Maker.",
            seeMoreLabel = "En savoir +",
            seeMoreLink = "#"
          )
        )
      )
    }
}
