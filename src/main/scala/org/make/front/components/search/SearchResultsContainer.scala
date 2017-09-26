package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.components.AppState
import org.make.front.components.search.SearchResults.SearchResultsProps
import org.make.front.helpers.QueryString
import org.make.services.proposal.{ProposalService, SearchOptionsRequest}
import org.make.front.models.{ProposalSearchResult, Proposal => ProposalModel}
import org.make.services.proposal.ProposalService.defaultResultsCount

import scala.concurrent.Future
import scala.scalajs.js.URIUtils

import scala.concurrent.ExecutionContext.Implicits.global

object SearchResultsContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SearchResults.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => SearchResultsProps =
    (_: Dispatch) => { (_: AppState, props: Props[Unit]) =>
      {
        val queryParams: Map[String, String] = QueryString.parse(props.location.search)

        val searchValue: Option[String] = {
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

        def getProposals(originalProposals: Seq[ProposalModel],
                         content: Option[String]): Future[ProposalSearchResult] = {
          ProposalService
            .searchProposals(
              content = content,
              options = Some(
                SearchOptionsRequest(
                  sort = Seq.empty,
                  limit = Some(defaultResultsCount),
                  skip = Some(originalProposals.size)
                )
              )
            )
            .map { proposals =>
              ProposalSearchResult(
                proposals = originalProposals ++ proposals,
                hasMore = proposals.size == defaultResultsCount
              )
            }
        }
        SearchResults.SearchResultsProps(
          onMoreResultsRequested = getProposals,
          proposals = getProposals(Seq.empty, searchValue),
          searchValue = searchValue
        )
      }
    }
}
