package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{
  SequenceId,
  Label             => LabelModel,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.services.proposal.{ProposalService, SearchResult}

import scala.concurrent.Future

object ThemeShowcaseContainer {

  final case class ThemeShowcaseContainerProps(themeSlug: String,
                                               maybeIntro: Option[String] = None,
                                               maybeNews: Option[String] = None,
                                               maybeOperation: Option[OperationModel] = None,
                                               maybeSequenceId: Option[SequenceId] = None,
                                               maybeLocation: Option[LocationModel] = None)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ThemeShowcase.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ThemeShowcaseContainerProps]) => ThemeShowcase.ThemeShowcaseProps =
    (_: Dispatch) => { (appState: AppState, props: Props[ThemeShowcaseContainerProps]) =>
      val themes = appState.themes
      val maybeTheme = themes.find(_.slug == props.wrapped.themeSlug)

      maybeTheme.map { theme =>
        ThemeShowcase.ThemeShowcaseProps(
          proposals = () =>
            ProposalService
              .searchProposals(
                themesIds = Seq(theme.id),
                labelsIds = Some(Seq(LabelModel.Star.name)),
                limit = Some(3),
                sort = Seq.empty,
                skip = None,
                language = Some(appState.language),
                country = Some(appState.country)
            ),
          theme = theme,
          maybeIntro = props.wrapped.maybeIntro,
          maybeNews = props.wrapped.maybeNews,
          maybeOperation = props.wrapped.maybeOperation,
          maybeSequenceId = props.wrapped.maybeSequenceId,
          maybeLocation = props.wrapped.maybeLocation,
          country = appState.country
        )
      }.getOrElse(
        ThemeShowcase.ThemeShowcaseProps(
          proposals = () => Future.successful(SearchResult(total = 0, results = Seq.empty, seed = None)),
          theme = TranslatedThemeModel.empty,
          country = appState.country
        )
      )
    }
}
