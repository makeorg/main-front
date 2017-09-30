package org.make.front.components.search

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.search.SearchResults.SearchResultsProps
import org.make.front.facades.I18n
import org.make.front.helpers.QueryString
import org.make.front.models.{ProposalSearchResult, Proposal => ProposalModel}
import org.make.services.proposal.ProposalService
import org.make.services.proposal.ProposalService.defaultResultsCount

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.URIUtils
import scala.util.{Failure, Success}

object SearchResultsContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SearchResults.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => SearchResultsProps =
    (dispatch: Dispatch) => { (_: AppState, props: Props[Unit]) =>
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
          val result = ProposalService
            .searchProposals(
              content = content,
              sort = Seq.empty,
              limit = Some(defaultResultsCount),
              skip = Some(originalProposals.size)
            )
            .map { proposals =>
              ProposalSearchResult(
                proposals = originalProposals ++ proposals,
                hasMore = proposals.size == defaultResultsCount
              )
            }

          result.onComplete {
            case Success(_) => // let child handle new results
            case Failure(_) => dispatch(NotifyError(I18n.t("error.main")))
          }

          result
        }
        SearchResults.SearchResultsProps(onMoreResultsRequested = getProposals, searchValue = searchValue)
      }
    }
}
