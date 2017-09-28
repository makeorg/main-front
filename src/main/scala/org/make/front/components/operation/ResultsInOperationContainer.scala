package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.components.operation.ResultsInOperation.ResultsInOperationProps
import org.make.front.models.{
  ProposalSearchResult,
  Operation   => OperationModel,
  OperationId => OperationIdModel,
  Proposal    => ProposalModel,
  Tag         => TagModel
}
import org.make.services.proposal.ProposalService
import org.make.services.proposal.ProposalService.defaultResultsCount

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ResultsInOperationContainer {

  case class ResultsInOperationContainerProps(currentOperation: OperationModel)
  case class ResultsInOperationContainerState(currentOperation: OperationModel, results: Seq[ProposalModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ResultsInOperation.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ResultsInOperationContainerProps]) => ResultsInOperation.ResultsInOperationProps =
    (_: Dispatch) => { (_: AppState, ownProps: Props[ResultsInOperationContainerProps]) =>
      val operationsIds: Seq[OperationIdModel] = Seq(ownProps.wrapped.currentOperation.id)

      def getProposals(tags: Seq[TagModel], skip: Int): Future[Seq[ProposalModel]] = {
        val proposals: Future[Seq[ProposalModel]] = ProposalService
          .searchProposals(
            themesIds = Seq(),
            operationsIds = operationsIds,
            tagsIds = tags.map(_.tagId),
            content = None,
            sort = Seq.empty,
            limit = Some(defaultResultsCount),
            skip = Some(skip)
          )
        proposals
      }

      def nextProposals(currentProposals: Seq[ProposalModel], tags: Seq[TagModel]): Future[ProposalSearchResult] = {
        getProposals(tags = tags, skip = currentProposals.size).map { proposals =>
          ProposalSearchResult(
            proposals = currentProposals ++ proposals,
            hasMore = proposals.size == defaultResultsCount
          )
        }
      }

      def searchOnSelectedTags(selectedTags: Seq[TagModel]) = {
        getProposals(tags = selectedTags, skip = 0).map { proposals =>
          ProposalSearchResult(proposals = proposals, hasMore = proposals.size == defaultResultsCount)
        }
      }

      ResultsInOperationProps(
        onMoreResultsRequested = nextProposals,
        onTagSelectionChange = searchOnSelectedTags,
        proposals = searchOnSelectedTags(Seq()),
        preselectedTags = Seq()
      )
    }
}
