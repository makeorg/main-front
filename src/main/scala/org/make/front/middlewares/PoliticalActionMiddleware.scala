package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.{LoadPoliticalAction, SetPoliticalAction}
import org.make.front.models.{AppState, PoliticalAction}

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
        imageUrl = "http://phantom.make.org/images/blocActionsLogo.svg",
        date = "29/06",
        location = "Place de la République, Paris",
        text = "Marchez pour le climat avec WWF et rejoignez le mouvement où que vous soyez en commandant votre kit #MarcheClimat. "
      ),
      PoliticalAction(
        imageUrl = "http://phantom.make.org/images/logoZeroWaste.png",
        date = "Ce week-end",
        location = "Partout en France",
        text = "Amis joggueurs, \"la Course pour la planète\" est pour vous ! Zero Waste France vous propose d'allier jogging et environnement. "
      ),
      PoliticalAction(
        imageUrl = "http://phantom.make.org/images/logoFNE.png",
        date = "3 au 9 juillet",
        location = "Paris, Nantes, Marseille, Lyon",
        text = "France Nature Environnement vous invite à la semaine de l'éco-mobilité : ateliers, conférences et innovation sont au rdv ! "
      )
    )

}
