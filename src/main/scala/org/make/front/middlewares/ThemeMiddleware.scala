package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.{LoadThemes, SetThemes}
import org.make.front.models.{AppState, GradientColor, Theme}

object ThemeMiddleware {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (appStore: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case LoadThemes =>
        if (appStore.getState.themes.isEmpty) {
          dispatch(SetThemes(retrieveThemes()))
        }
      case action => dispatch(action)
  }

  // toDo: replace by an api call
  private def retrieveThemes(): Seq[Theme] =
    Seq(
      Theme(
        "democratie-vie-politique",
        "DÉMOCRATIE / VIE POLITIQUE",
        3,
        5600,
        "#e91e63",
        Some(GradientColor(from = "#e81e61", to = "#7f2fd0"))
      ),
      Theme(
        "developpement-durable-energie",
        "DÉVELOPPEMENT DURABLE / ENERGIE",
        3,
        5600,
        "#8bc34a",
        Some(GradientColor(from = "#83bb1a", to = "#1fc8f1"))
      ),
      Theme(
        "sante-alimentation",
        "SANTÉ / ALIMENTATION",
        3,
        5600,
        "#26a69a",
        Some(GradientColor(from = "#26a69a", to = "#1ceba0"))
      ),
      Theme("education", "EDUCATION", 3, 5600, "#673ab7", Some(GradientColor(from = "#9173c6", to = "#ee98d7"))),
      Theme(
        "economie-emploi-travail",
        "ECONOMIE / EMPLOI / TRAVAIL",
        3,
        5600,
        "#0e75c6",
        Some(GradientColor(from = "#0e75c6", to = "#41ced6"))
      ),
      Theme(
        "securite-justice",
        "SÉCURITÉ / JUSTICE",
        3,
        5600,
        "#ff9047",
        Some(GradientColor(from = "#ff9047", to = "#b7588b"))
      ),
      Theme("logement", "LOGEMENT", 3, 5600, "#ff9800", Some(GradientColor(from = "#ff9800", to = "#ffea9f"))),
      Theme(
        "vivre-ensemble-solidarites",
        "VIVRE ENSEMBLE / SOLIDARITÉS",
        3,
        5600,
        "#f9e42a",
        Some(GradientColor(from = "#ecd400", to = "#ff9ffd"))
      ),
      Theme(
        "agriculture-ruralite",
        "AGRICULTURE / RURALITÉ",
        3,
        5600,
        "#2e7d32",
        Some(GradientColor(from = "#2e7d32", to = "#8fcf4b"))
      ),
      Theme(
        "europe-monde",
        "EUROPE / MONDE",
        3,
        5600,
        "#311b92",
        Some(GradientColor(from = "#311b92", to = "#54a0e3"))
      ),
      Theme(
        "transports-deplacement",
        "TRANSPORTS / DÉPLACEMENT",
        3,
        5600,
        "#f5515f",
        Some(GradientColor(from = "#f5515f", to = "#9f031b"))
      ),
      Theme(
        "numerique-culture",
        "NUMÉRIQUE / CULTURE",
        3,
        5600,
        "#03a9f4",
        Some(GradientColor(from = "#4fc8ff", to = "#ffdc00"))
      )
    )

}
