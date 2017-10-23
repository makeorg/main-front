package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.services.proposal.ProposalService

object LabelShowcaseContainer {

  final case class LabelShowcaseContainerProps(intro: String, label: String, title: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(TrendingShowcase.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[LabelShowcaseContainerProps]) => TrendingShowcase.TrendingShowcaseProps =
    (_: Dispatch) => { (_: AppState, ownProps: Props[LabelShowcaseContainerProps]) =>
      TrendingShowcase.TrendingShowcaseProps(
        proposals = ProposalService
          .searchProposals(
            labelsIds = Some(Seq(ownProps.wrapped.label)),
            limit = Some(2),
            sort = Seq.empty,
            skip = None
          ),
        intro = ownProps.wrapped.intro,
        title = ownProps.wrapped.title
      )
    }
}
