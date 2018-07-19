/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.operation.ResultsInOperation.ResultsInOperationProps
import org.make.front.facades.I18n
import org.make.front.helpers.QueryString
import org.make.front.models.{Proposal, Location => LocationModel, OperationExpanded => OperationModel, Tag => TagModel}
import org.make.services.proposal.ProposalService.defaultResultsCount
import org.make.services.proposal.{ProposalService, SearchResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import scala.util.{Failure, Success}

object ResultsInOperationContainer {

  case class ResultsInOperationContainerProps(currentOperation: OperationModel, maybeLocation: Option[LocationModel])

  case class ResultsInOperationContainerState(currentOperation: OperationModel, results: js.Array[Proposal])

  lazy val reactClass: ReactClass = WithRouter(
    ReactRedux.connectAdvanced(selectorFactory)(ResultsInOperation.reactClass)
  )

  def selectorFactory
    : Dispatch => (AppState, Props[ResultsInOperationContainerProps]) => ResultsInOperation.ResultsInOperationProps =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[ResultsInOperationContainerProps]) =>
      val tagIds: Array[String] = QueryString
        .parse(props.location.search)
        .getOrElse("tagIds", "")
        .split(",")

      val preselectedTag: js.Array[TagModel] =
        props.wrapped.currentOperation.tags.filter(tag => tagIds.contains(tag.tagId.value))

      def getProposals(tags: js.Array[TagModel], skip: Int, seed: Option[Int] = None): Future[SearchResult] = {
        ProposalService
          .searchProposals(
            operationId = Some(props.wrapped.currentOperation.operationId),
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

      ResultsInOperationProps(
        operation = props.wrapped.currentOperation,
        onMoreResultsRequested = nextProposals,
        onTagSelectionChange = searchOnSelectedTags,
        preselectedTags = preselectedTag,
        maybeLocation = props.wrapped.maybeLocation,
        country = appState.country
      )
    }
}
