package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.theme.ResultsInTheme.ResultsInThemeProps
import org.make.front.facades.I18n
import org.make.front.models.{
  ProposalSearchResult,
  Proposal => ProposalModel,
  Tag      => TagModel,
  Theme    => ThemeModel,
  ThemeId  => ThemeIdModel
}
import org.make.services.proposal.ProposalService
import org.make.services.proposal.ProposalService.defaultResultsCount

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ResultsInThemeContainer {

  case class ResultsInThemeContainerProps(currentTheme: ThemeModel)
  case class ResultsInThemeContainerState(currentTheme: ThemeModel, results: Seq[ProposalModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ResultsInTheme.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ResultsInThemeContainerProps]) => ResultsInTheme.ResultsInThemeProps =
    (dispatch: Dispatch) => { (_: AppState, ownProps: Props[ResultsInThemeContainerProps]) =>
      val themesIds: Seq[ThemeIdModel] = Seq(ownProps.wrapped.currentTheme.id)

      def getProposals(tags: Seq[TagModel], skip: Int): Future[Seq[ProposalModel]] = {
        val proposals: Future[Seq[ProposalModel]] = ProposalService
          .searchProposals(
            themesIds = themesIds,
            tagsIds = tags.map(_.tagId),
            content = None,
            sort = Seq.empty,
            limit = Some(defaultResultsCount),
            skip = Some(skip)
          )
        proposals
      }

      def nextProposals(currentProposals: Seq[ProposalModel], tags: Seq[TagModel]): Future[ProposalSearchResult] = {
        val result = getProposals(tags = tags, skip = currentProposals.size).map { proposals =>
          ProposalSearchResult(
            proposals = currentProposals ++ proposals,
            hasMore = proposals.size == defaultResultsCount
          )
        }

        result.onComplete {
          case Success(_) => // Let child handle results
          case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
        }

        result
      }

      def searchOnSelectedTags(selectedTags: Seq[TagModel]): Future[ProposalSearchResult] = {
        val result = getProposals(tags = selectedTags, skip = 0).map { proposals =>
          ProposalSearchResult(proposals = proposals, hasMore = proposals.size == defaultResultsCount)
        }

        result.onComplete {
          case Success(_) => // Let child handle results
          case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
        }

        result
      }

      ResultsInThemeProps(
        theme = ownProps.wrapped.currentTheme,
        onMoreResultsRequested = nextProposals,
        onTagSelectionChange = searchOnSelectedTags,
        proposals = searchOnSelectedTags(Seq()),
        preselectedTags = Seq()
      )
    }
}
