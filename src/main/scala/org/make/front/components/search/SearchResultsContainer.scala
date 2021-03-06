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
import org.make.front.components.DataLoader.DataLoaderProps
import org.make.front.components.{AppState, DataLoader}
import org.make.front.facades.I18n
import org.make.front.helpers.QueryString
import org.make.front.models.{
  OperationExpanded,
  TranslatedTheme,
  Location  => LocationModel,
  Operation => OperationModel,
  Proposal  => ProposalModel
}
import org.make.services.operation.OperationService
import org.make.services.proposal.ProposalService.defaultResultsCount
import org.make.services.proposal.{ProposalService, SearchResult}
import org.make.services.tag.TagService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.URIUtils
import scala.util.{Failure, Success}

object SearchResultsContainer {

  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(DataLoader.reactClass))

  def selectorFactory
    : (Dispatch) => (AppState, Props[Unit]) => DataLoaderProps[Either[OperationExpanded, TranslatedTheme]] =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[Unit]) =>
      {
        val operationSlug: Option[String] = props.`match`.params.get("operationSlug")
        val themeSlug: Option[String] = props.`match`.params.get("themeSlug")
        val countryCode: String = props.`match`.params.get("country").getOrElse("FR").toUpperCase

        val futureMaybeOperation: Future[Option[OperationModel]] = operationSlug match {
          case Some(slug) => Future.successful(appState.operations.getOperationBySlug(slug))
          case _          => Future.successful(None)
        }

        val futureMaybeOperationExpanded: Future[Option[OperationExpanded]] = for {
          maybeOperation <- futureMaybeOperation
          tags           <- TagService.getTags
        } yield OperationExpanded.getOperationExpandedFromOperation(maybeOperation, tags, appState.country)
        val maybeTheme: Option[TranslatedTheme] = themeSlug.flatMap { slug =>
          appState.findTheme(slug)
        }

        val futureOperationExpandedOrTheme: Future[Option[Either[OperationExpanded, TranslatedTheme]]] = {
          maybeTheme match {
            case Some(theme) => Future.successful(Some(Right(theme)))
            case _ =>
              futureMaybeOperationExpanded.map {
                case Some(operation) => Some(Left(operation))
                case _               => None
              }
          }
        }

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
            operation <- futureMaybeOperation
            proposals <- ProposalService
              .searchProposals(
                maybeLocation = Some(LocationModel.SearchResultsPage),
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

        val shouldOperationUpdate: (Option[Either[OperationExpanded, TranslatedTheme]]) => Boolean = { maybeOperation =>
          maybeOperation.forall(
            operationOrTheme =>
              operationOrTheme match {
                case Left(operationExpanded) =>
                  !operationSlug.contains(operationExpanded.slug) || operationExpanded.country != appState.country
                case Right(translatedTheme) =>
                  !themeSlug.contains(translatedTheme.slug) || translatedTheme.country != appState.country
            }
          )
        }

        DataLoaderProps[Either[OperationExpanded, TranslatedTheme]](
          future = () => futureOperationExpandedOrTheme,
          shouldComponentUpdate = shouldOperationUpdate,
          componentDisplayedMeanwhileReactClass = WaitingForSearchResults.reactClass,
          componentReactClass = SearchResults.reactClass,
          componentProps = { operationExpandedOrTheme =>
            val maybeOperation: Option[OperationExpanded] = operationExpandedOrTheme match {
              case Left(operationExpanded) => Some(operationExpanded)
              case _                       => None
            }
            SearchResults.SearchResultsProps(
              onMoreResultsRequested = getProposals,
              searchValue = searchQueryValue,
              maybeSequence = None,
              maybeOperation = maybeOperation,
              maybeLocation = Some(LocationModel.SearchResultsPage),
              isConnected = appState.connectedUser.isDefined,
              language = appState.language,
              operations = appState.operations
            )
          },
          onNotFound = () => {
            props.history.push("/404")
          }
        )
      }
    }
}
