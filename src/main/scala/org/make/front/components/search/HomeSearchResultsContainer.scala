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

package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.actions.NotifyError
import org.make.front.components.search.SearchResults.SearchResultsProps
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.helpers.QueryString
import org.make.front.models.{Location => LocationModel, Proposal => ProposalModel}
import org.make.services.operation.OperationService
import org.make.services.proposal.ProposalService.defaultResultsCount
import org.make.services.proposal.{ProposalService, SearchResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.URIUtils
import scala.util.{Failure, Success}

object HomeSearchResultsContainer {

  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(SearchResults.reactClass))

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => SearchResultsProps =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        val operationSlug: Option[String] = props.`match`.params.get("operationSlug")
        val themeSlug: Option[String] = props.`match`.params.get("themeSlug")

        val queryParams: Map[String, String] = QueryString.parse(props.location.search)

        val searchQueryValue: Option[String] = {
          queryParams
            .get("q")
            .flatMap { value =>
              if (value.isEmpty) {
                None
              } else {
                Some(value)
              }
            }
            .map(URIUtils.decodeURI)
        }

        def getProposals(originalProposals: js.Array[ProposalModel], content: Option[String]): Future[SearchResult] = {

          val result: Future[SearchResult] = for {
            operation <- operationSlug match {
              case None                     => Future.successful(None)
              case Some(operationSlugValue) => OperationService.getOperationBySlug(operationSlugValue)
            }
            proposals <- ProposalService
              .searchProposals(
                content = content,
                limit = Some(defaultResultsCount),
                skip = Some(originalProposals.size),
                isRandom = Some(false),
                language = Some(appState.language),
                country = Some(appState.country),
                operationId = operation.map(_.operationId),
                themesIds = themeSlug match {
                  case None => js.Array()
                  case Some(themeSlugValue) =>
                    appState.findTheme(themeSlugValue) match {
                      case None        => js.Array()
                      case Some(theme) => js.Array(theme.id)
                    }
                }
              )

          } yield proposals

          result.map { searchResults =>
            searchResults.copy(results = originalProposals ++ searchResults.results)
          }

          result.onComplete {
            case Success(_) => // let child handle new results
            case Failure(_) => dispatch(NotifyError(I18n.t("error-message.main")))
          }

          result
        }

        SearchResults.SearchResultsProps(
          onMoreResultsRequested = getProposals,
          searchValue = searchQueryValue,
          maybeSequence = None,
          maybeOperation = None,
          maybeLocation = Some(LocationModel.SearchResultsPage),
          isConnected = appState.connectedUser.isDefined,
          language = appState.language
        )
      }
    }
}
