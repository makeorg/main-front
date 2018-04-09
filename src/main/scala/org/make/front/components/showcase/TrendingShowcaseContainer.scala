package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{Location => LocationModel}
import org.make.services.proposal.ProposalService

object TrendingShowcaseContainer {

  final case class TrendingShowcaseContainerProps(intro: String,
                                                  trending: String,
                                                  title: String,
                                                  maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(TrendingShowcase.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[TrendingShowcaseContainerProps]) => TrendingShowcase.TrendingShowcaseProps =
    (_: Dispatch) => { (appState: AppState, props: Props[TrendingShowcaseContainerProps]) =>
      TrendingShowcase.TrendingShowcaseProps(
        proposals = () =>
          ProposalService
            .searchProposals(
              trending = Some(props.wrapped.trending),
              limit = Some(2),
              sort = Seq.empty,
              skip = None,
              isRandom = Some(false),
              language = Some(appState.language),
              country = Some(appState.country)
          ),
        intro = props.wrapped.intro,
        title = props.wrapped.title,
        maybeLocation = props.wrapped.maybeLocation,
        country = appState.country
      )
    }
}
