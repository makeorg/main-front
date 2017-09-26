package org.make.front.middlewares

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.front.actions.{LoadConfiguration, NotifyError, SetConfiguration}
import org.make.front.components.AppState
import org.make.front.models._
import org.make.services.ConfigurationService

import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

object ConfigurationMiddleware {

  val handle: (Store[AppState]) => (Dispatch) => (Any) => Any = (appStore: Store[AppState]) =>
    (dispatch: Dispatch) => {
      case LoadConfiguration =>
        if (appStore.getState.configuration.isEmpty) {
          ConfigurationService.fetchConfiguration().onComplete {
            case Success(configuration) => dispatch(SetConfiguration(configuration))
            case Failure(e)             => dispatch(NotifyError(e.getMessage, None))
          }
        }
      case action => dispatch(action)
  }

  // toDo: replace by an api call
  private def retrieveThemes(): Seq[Theme] =
    Seq(
      Theme(
        ThemeId("1"),
        "democratie-vie-politique",
        "DÉMOCRATIE / VIE POLITIQUE",
        3,
        5600,
        "#e91e63",
        Some(GradientColor(from = "#e81e61", to = "#7f2fd0")),
        Seq(
          Tag(TagId("tag-budget"), "budget"),
          Tag(TagId("tag-equipement"), "équipement"),
          Tag(TagId("tag-formation"), "formation"),
          Tag(TagId("tag-alimentation"), "Alimentation"),
          Tag(TagId("tag-bio"), "Bio"),
          Tag(TagId("tag-viande"), "viande"),
          Tag(TagId("tag-permaculture"), "Permaculture")
        )
      ),
      Theme(
        ThemeId("2"),
        "developpement-durable-energie",
        "DÉVELOPPEMENT DURABLE / ENERGIE",
        3,
        5600,
        "#8bc34a",
        Some(GradientColor(from = "#83bb1a", to = "#1fc8f1")),
        Seq(
          Tag(TagId("tag-budget"), "budget"),
          Tag(TagId("tag-bio"), "Bio"),
          Tag(TagId("tag-viande"), "viande"),
          Tag(TagId("tag-permaculture"), "Permaculture")
        )
      ),
      Theme(
        ThemeId("3"),
        "sante-alimentation",
        "SANTÉ / ALIMENTATION",
        3,
        5600,
        "#26a69a",
        Some(GradientColor(from = "#26a69a", to = "#1ceba0"))
      ),
      Theme(
        ThemeId("3"),
        "education",
        "EDUCATION",
        3,
        5600,
        "#673ab7",
        Some(GradientColor(from = "#9173c6", to = "#ee98d7"))
      ),
      Theme(
        ThemeId("4"),
        "economie-emploi-travail",
        "ECONOMIE / EMPLOI / TRAVAIL",
        3,
        5600,
        "#0e75c6",
        Some(GradientColor(from = "#0e75c6", to = "#41ced6"))
      ),
      Theme(
        ThemeId("5"),
        "securite-justice",
        "SÉCURITÉ / JUSTICE",
        3,
        5600,
        "#ff9047",
        Some(GradientColor(from = "#ff9047", to = "#b7588b"))
      ),
      Theme(
        ThemeId("6"),
        "logement",
        "LOGEMENT",
        3,
        5600,
        "#ff9800",
        Some(GradientColor(from = "#ff9800", to = "#ffea9f"))
      ),
      Theme(
        ThemeId("7"),
        "vivre-ensemble-solidarites",
        "VIVRE ENSEMBLE / SOLIDARITÉS",
        3,
        5600,
        "#f9e42a",
        Some(GradientColor(from = "#ecd400", to = "#ff9ffd"))
      ),
      Theme(
        ThemeId("7"),
        "agriculture-ruralite",
        "AGRICULTURE / RURALITÉ",
        3,
        5600,
        "#2e7d32",
        Some(GradientColor(from = "#2e7d32", to = "#8fcf4b"))
      ),
      Theme(
        ThemeId("8"),
        "europe-monde",
        "EUROPE / MONDE",
        3,
        5600,
        "#311b92",
        Some(GradientColor(from = "#311b92", to = "#54a0e3"))
      ),
      Theme(
        ThemeId("9"),
        "transports-deplacement",
        "TRANSPORTS / DÉPLACEMENT",
        3,
        5600,
        "#f5515f",
        Some(GradientColor(from = "#f5515f", to = "#9f031b"))
      ),
      Theme(
        ThemeId("10"),
        "numerique-culture",
        "NUMÉRIQUE / CULTURE",
        3,
        5600,
        "#03a9f4",
        Some(GradientColor(from = "#4fc8ff", to = "#ffdc00"))
      )
    )

}
