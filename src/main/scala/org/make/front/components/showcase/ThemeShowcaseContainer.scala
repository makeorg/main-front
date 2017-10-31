package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.Label
import org.make.services.proposal.ProposalResponses.SearchResponse
import org.make.services.proposal.ProposalService

import scala.concurrent.Future

object ThemeShowcaseContainer {

  final case class ThemeShowcaseContainerProps(themeSlug: String,
                                               maybeIntro: Option[String] = None,
                                               maybeNews: Option[String] = None)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ThemeShowcase.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ThemeShowcaseContainerProps]) => ThemeShowcase.ThemeShowcaseProps =
    (_: Dispatch) => { (appState: AppState, ownProps: Props[ThemeShowcaseContainerProps]) =>
      val themes = appState.themes
      val maybeTheme = themes.find(_.slug == ownProps.wrapped.themeSlug)

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
          maybeIntro = ownProps.wrapped.maybeIntro,
          maybeNews = ownProps.wrapped.maybeNews
        )
      }.getOrElse(
        ThemeShowcase.ThemeShowcaseProps(
          proposals = Future.successful(SearchResponse(total = 0, results = Seq.empty)),
          maybeTheme = None,
          maybeIntro = None,
          maybeNews = None
        )
      )
    }
}
