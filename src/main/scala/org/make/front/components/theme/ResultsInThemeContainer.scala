package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.components.theme.ResultsInTheme.ResultsInThemeProps
import org.make.front.facades.Configuration
import org.make.front.models.{Proposal => ProposalModel, Tag => TagModel, Theme => ThemeModel, ThemeId => ThemeIdModel}
import org.make.services.proposal.{ProposalServiceComponent, SearchOptionsRequest}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ResultsInThemeContainer extends ProposalServiceComponent {

  override def apiBaseUrl: String = Configuration.apiUrl
  private val resultSetCount = 20
  case class ProposalSearchResult(proposals: Seq[ProposalModel], hasMore: Boolean)

  case class ResultsInThemeContainerProps(currentTheme: ThemeModel)
  case class ResultsInThemeContainerState(currentTheme: ThemeModel, results: Seq[ProposalModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ResultsInTheme.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ResultsInThemeContainerProps]) => ResultsInTheme.ResultsInThemeProps =
    (_: Dispatch) => { (_: AppState, ownProps: Props[ResultsInThemeContainerProps]) =>
      val themesIds: Seq[ThemeIdModel] = Seq(ownProps.wrapped.currentTheme.id)

      def getProposals(tags: Seq[TagModel], skip: Int): Future[Seq[ProposalModel]] = {
        val proposals: Future[Seq[ProposalModel]] = proposalService
          .searchProposals(
            themesIds = themesIds,
            tagsIds = tags.map(_.tagId),
            content = None,
            options = Some(SearchOptionsRequest(sort = Seq.empty, limit = Some(resultSetCount), skip = Some(skip)))
          )
        proposals
      }

      def nextProposals(currentProposals: Seq[ProposalModel], tags: Seq[TagModel]): Future[ProposalSearchResult] = {
        getProposals(tags = tags, skip = currentProposals.size).map { proposals =>
          ProposalSearchResult(proposals = currentProposals ++ proposals, hasMore = proposals.size == resultSetCount)
        }
      }

      def searchOnSelectedTags(selectedTags: Seq[TagModel]) = {
        getProposals(tags = selectedTags, skip = 0).map { proposals =>
          ProposalSearchResult(proposals = proposals, hasMore = proposals.size == resultSetCount)
        }
      }

      ResultsInThemeProps(
        onMoreResultsRequested = nextProposals,
        onTagSelectionChange = searchOnSelectedTags,
        proposals = searchOnSelectedTags(Seq()),
        preselectedTags = Seq()
      )
    }
}
