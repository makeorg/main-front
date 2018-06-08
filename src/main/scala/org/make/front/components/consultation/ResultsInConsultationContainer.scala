package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.consultation.ResultsInConsultation.ResultsInConsultationProps
import org.make.front.facades.I18n
import org.make.front.models.{Proposal, Location => LocationModel, OperationExpanded => OperationModel, Tag => TagModel}
import org.make.services.proposal.ProposalService.defaultResultsCount
import org.make.services.proposal.{ProposalService, SearchResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object ResultsInConsultationContainer {
  case class ResultsInConsultationContainerProps(currentConsultation: OperationModel,
                                                 maybeLocation: Option[LocationModel])

  case class ResultsInConsultationContainerState(currentConsultation: OperationModel, results: js.Array[Proposal])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ResultsInConsultation.reactClass)

  def selectorFactory
    : Dispatch => (AppState,
                   Props[ResultsInConsultationContainerProps]) => ResultsInConsultation.ResultsInConsultationProps =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[ResultsInConsultationContainerProps]) =>
      def getProposals(tags: js.Array[TagModel], skip: Int, seed: Option[Int] = None): Future[SearchResult] = {
        ProposalService
          .searchProposals(
            operationId = Some(props.wrapped.currentConsultation.operationId),
            tagsIds = tags.map(_.tagId),
            content = None,
            seed = seed,
            limit = Some(defaultResultsCount),
            skip = Some(skip),
            language = Some(appState.language),
            country = Some(appState.country)
          )
      }

      def nextProposals(currentProposals: js.Array[Proposal],
                        tags: js.Array[TagModel],
                        seed: Option[Int] = None): Future[SearchResult] = {
        val result = getProposals(tags = tags, skip = currentProposals.size, seed = seed).map { results =>
          results.copy(results = currentProposals ++ results.results)
        }

        result.onComplete {
          case Success(_) => // Let child handle results
          case Failure(_) => dispatch(NotifyError(I18n.t("error-message.main")))
        }

        result
      }

      def searchOnSelectedTags(selectedTags: js.Array[TagModel], seed: Option[Int] = None): Future[SearchResult] = {
        val result = getProposals(tags = selectedTags, skip = 0, seed = seed)

        result.onComplete {
          case Success(_) => // Let child handle results
          case Failure(_) => dispatch(NotifyError(I18n.t("error-message.main")))
        }

        result
      }

      ResultsInConsultationProps(
        operation = props.wrapped.currentConsultation,
        onMoreResultsRequested = nextProposals,
        onTagSelectionChange = searchOnSelectedTags,
        preselectedTags = js.Array(),
        maybeLocation = props.wrapped.maybeLocation,
        country = appState.country
      )
    }

}
