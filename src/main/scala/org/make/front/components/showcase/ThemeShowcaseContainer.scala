package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{Label, Location => LocationModel, Operation => OperationModel, Sequence => SequenceModel}
import org.make.services.proposal.{ProposalService, SearchResult}

import scala.concurrent.Future

object ThemeShowcaseContainer {

  final case class ThemeShowcaseContainerProps(themeSlug: String,
                                               maybeIntro: Option[String] = None,
                                               maybeNews: Option[String] = None,
                                               maybeOperation: Option[OperationModel],
                                               maybeSequence: Option[SequenceModel],
                                               maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ThemeShowcase.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ThemeShowcaseContainerProps]) => ThemeShowcase.ThemeShowcaseProps =
    (_: Dispatch) => { (appState: AppState, props: Props[ThemeShowcaseContainerProps]) =>
      val themes = appState.themes
      val maybeTheme = themes.find(_.slug == props.wrapped.themeSlug)

      maybeTheme.map { theme =>
        ThemeShowcase.ThemeShowcaseProps(
          proposals = ProposalService
            .searchProposals(
              themesIds = Seq(theme.id),
              labelsIds = Some(Seq(Label.Star.name)),
              limit = Some(4),
              sort = Seq.empty,
              skip = None
            ),
          maybeTheme = Some(theme),
          maybeIntro = props.wrapped.maybeIntro,
          maybeNews = props.wrapped.maybeNews,
          maybeOperation = props.wrapped.maybeOperation,
          maybeSequence = props.wrapped.maybeSequence,
          maybeLocation = props.wrapped.maybeLocation
        )
      }.getOrElse(
        ThemeShowcase.ThemeShowcaseProps(
          proposals = Future.successful(SearchResult(total = 0, results = Seq.empty, seed = None)),
          maybeTheme = None,
          maybeIntro = None,
          maybeNews = None,
          maybeOperation = None,
          maybeSequence = None,
          maybeLocation = None
        )
      )
    }
}
