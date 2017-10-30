package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.{LoadPoliticalAction, SetPoliticalAction}
import org.make.front.components.AppState
import org.make.front.facades.{amisdelaterre, artisansdumonde, femmesingenieurs, goodplanet}
import org.make.front.models.PoliticalAction

object PoliticalActionMiddleware {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (appStore: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case LoadPoliticalAction =>
        if (appStore.getState.politicalActions.isEmpty) {
          dispatch(SetPoliticalAction(retrievePoliticalAction()))
        }
      case action => dispatch(action)
  }

  // toDo: replace by an api call
  private def retrievePoliticalAction(): Seq[PoliticalAction] =
    Seq(
      PoliticalAction(
        themeSlug = Some("developpement-durable-energie"),
        imageUrl = goodplanet.toString,
        imageTitle = Some("Fondation Goodplanet"),
        date = None,
        location = None,
        links = Some(
          """<a href="https://www.goodplanet.org/fr/agir-a-nos-cotes/devenir-benevole/" target="_blank">Je deviens bénévole</a> <span style="color: rgba(0, 0, 0, 0.5)">|</span> <a href="https://www.goodplanet.org/fr/agir-a-nos-cotes/ecogestes/" target="_blank">J'éco-agis !</a>"""
        ),
        introduction = Some(
          "Il faut mettre l'écologie et le développement durable au coeur de l'éducation - Isabelle, 53 ans - 86 votes favorables"
        ),
        text =
          "Il suffira d'un geste, un matin - mais aussi quand tu veux ! Notre environnement et la Fondation GoodPlanet te remercieront&nbsp;! "
      ),
      PoliticalAction(
        themeSlug = Some("developpement-durable-energie"),
        imageUrl = amisdelaterre.toString,
        imageTitle = Some("Amis de la terre"),
        date = None,
        location = None,
        links = Some("""<a href="http://www.amisdelaterre.org/-groupes-.html" target="_blank">J'agis !</a>"""),
        introduction = Some(
          "Il faut partager de façon égale les ressources de la terre qui produissent assez de nourriture pour tout le monde de façon saine - Laetitia, 34 ans - 19 votes favorables"
        ),
        text = "Agis avec les Amis de la Terre pour une transition vers des sociétés soutenables "
      ),
      PoliticalAction(
        themeSlug = Some("economie-emploi-travail"),
        imageUrl = artisansdumonde.toString,
        imageTitle = Some("Artisans du monde"),
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.artisansdumonde.org/agir-pour-le-commerce-equitable/s-engager-avec-nous/devenir-benevole" target="_blank">Je m'engage</a>"""
        ),
        introduction =
          Some("Il faut favoriser le commerce équitable (et bio) à bas prix - Arnaud, 50 ans - 38 votes favorables"),
        text = "Contribue aux côtés d'Artisans du Monde au commerce équitable "
      ),
      PoliticalAction(
        themeSlug = Some("economie-emploi-travail"),
        imageUrl = femmesingenieurs.toString,
        imageTitle = Some("Femmes ingénieures"),
        date = None,
        location = None,
        links = Some(
          """<a href="http://www.femmes-ingenieurs.org/82_p_43876/adhesion.html" target="_blank">Je rejoins la communauté&nbsp;!</a>"""
        ),
        introduction = Some(
          "Il faut mettre en place plus de sensibilisation sur l'égalité homme/femme et les stéréotypes de genre - Audrey, 25 ans, Aspremont - 51 votes favorables"
        ),
        text =
          "L'expression \"les filles ne sont pas bonnes en maths\" te donne des boutons ? Tu es un bel exemple du contraire ? Viens par ici&nbsp;! "
      )
    )

}
