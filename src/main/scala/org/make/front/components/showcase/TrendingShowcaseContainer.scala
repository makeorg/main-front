package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.services.proposal.ProposalService

object TrendingShowcaseContainer {

  final case class TrendingShowcaseContainerProps(introTranslationKey: String, trending: String, title: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Showcase.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[TrendingShowcaseContainerProps]) => Showcase.ShowcaseProps =
    (_: Dispatch) => { (appState: AppState, ownProps: Props[TrendingShowcaseContainerProps]) =>
      Showcase.ShowcaseProps(
        proposals = ProposalService
          .searchProposals(trending = Some(ownProps.wrapped.trending), limit = Some(2), sort = Seq.empty, skip = None),
        introTranslationKey = ownProps.wrapped.introTranslationKey,
        title = ownProps.wrapped.title
      )
    }

}
