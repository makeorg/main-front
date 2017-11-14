package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.operation.ResultsInOperation.ResultsInOperationProps
import org.make.front.facades.I18n
import org.make.front.models.{Proposal, Operation => OperationModel, OperationId => OperationIdModel, Tag => TagModel}
import org.make.services.proposal.ProposalService.defaultResultsCount
import org.make.services.proposal.{ProposalService, SearchResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ResultsInOperationContainer {

  case class ResultsInOperationContainerProps(currentOperation: OperationModel)
  case class ResultsInOperationContainerState(currentOperation: OperationModel, results: Seq[Proposal])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ResultsInOperation.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ResultsInOperationContainerProps]) => ResultsInOperation.ResultsInOperationProps =
    (dispatch: Dispatch) => { (_: AppState, ownProps: Props[ResultsInOperationContainerProps]) =>
      val operationsIds: Seq[OperationIdModel] = Seq(ownProps.wrapped.currentOperation.operationId)

      def getProposals(tags: Seq[TagModel], skip: Int): Future[SearchResult] = {
        val proposals: Future[SearchResult] = ProposalService
          .searchProposals(
            operationsIds = operationsIds,
            tagsIds = tags.map(_.tagId),
            content = None,
            sort = Seq.empty,
            limit = Some(defaultResultsCount),
            skip = Some(skip)
          )
        proposals
      }

      def nextProposals(currentProposals: Seq[Proposal], tags: Seq[TagModel]): Future[SearchResult] = {
        val result = getProposals(tags = tags, skip = currentProposals.size).map { results =>
          results.copy(results = currentProposals ++ results.results)
        }

        result.onComplete {
          case Success(_) => // Let child handle results
          case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
        }

        result
      }

      def searchOnSelectedTags(selectedTags: Seq[TagModel]): Future[SearchResult] = {
        val result = getProposals(tags = selectedTags, skip = 0)

        result.onComplete {
          case Success(_) => // Let child handle results
          case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
        }

        result
      }

      ResultsInOperationProps(
        operation = ownProps.wrapped.currentOperation,
        onMoreResultsRequested = nextProposals,
        onTagSelectionChange = searchOnSelectedTags,
        proposals = searchOnSelectedTags(Seq()),
        preselectedTags = Seq()
      )
    }
}
