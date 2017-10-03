package org.make.front.components.showcase

import java.util.concurrent.ThreadLocalRandom

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.services.proposal.ProposalResponses.SearchResponse
import org.make.services.proposal.ProposalService

import scala.concurrent.Future

object ThemeShowcaseContainer {

  final case class ThemeShowcaseContainerProps(introTranslationKey: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Showcase.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[ThemeShowcaseContainerProps]) => Showcase.ShowcaseProps =
    (_: Dispatch) => { (appState: AppState, ownProps: Props[ThemeShowcaseContainerProps]) =>
      val themes = appState.themes
      if (themes.nonEmpty) {
        val theme = themes(ThreadLocalRandom.current().nextInt(themes.size))
        Showcase.ShowcaseProps(
          proposals = ProposalService
            .searchProposals(themesIds = Seq(theme.id), limit = Some(2), sort = Seq.empty, skip = None),
          introTranslationKey = ownProps.wrapped.introTranslationKey,
          title = theme.title
        )
      } else {
        Showcase.ShowcaseProps(
          proposals = Future.successful(SearchResponse(total = 0, results = Seq.empty)),
          introTranslationKey = ownProps.wrapped.introTranslationKey,
          title = ""
        )
      }
    }

}
