package org.make.front.components.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.models.{Label => LabelModel, OperationExpanded => OperationModel}
import org.make.services.proposal.{ProposalService, SearchResult}

import scala.concurrent.Future
import scala.scalajs.js

object OperationShowcaseContainer {
  final case class OperationShowcaseContainerProps(operation: OperationModel)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(OperationShowcase.reactClass)

  def selectorFactory
    : Dispatch => (AppState, Props[OperationShowcaseContainerProps]) => OperationShowcase.OperationShowcaseProps =
    (_: Dispatch) => { (appState: AppState, props: Props[OperationShowcaseContainerProps]) =>
      val proposals: Future[SearchResult] = ProposalService.searchProposals(
        operationId = Some(props.wrapped.operation.operationId),
        limit = Some(3),
        language = Some(appState.language),
        country = Some(appState.country)
      )

      OperationShowcase.OperationShowcaseProps(
        proposals = () => proposals,
        country = appState.country,
        language = appState.language,
        operation = props.wrapped.operation
      )
    }
}
