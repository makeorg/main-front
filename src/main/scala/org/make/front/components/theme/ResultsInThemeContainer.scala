package org.make.front.components.theme

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.components.AppState
import org.make.front.facades.Configuration
import org.make.front.models.{Theme, Proposal => ProposalModel, Tag => TagModel, ThemeId => ThemeIdModel}
import org.make.services.proposal.{ProposalServiceComponent, SearchOptionsRequest}

import scala.concurrent.Future

object ResultsInThemeContainer extends ProposalServiceComponent {

  override def apiBaseUrl: String = Configuration.apiUrl
  private val resultSetCount = 20

  case class ResultsInThemeContainerProps(currentTheme: Theme)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ResultsInTheme.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ResultsInThemeContainerProps]) => ResultsInTheme.ResultsInThemeProps =
    (_: Dispatch) => { (_: AppState, ownProps: Props[ResultsInThemeContainerProps]) =>
      val themesIds: Seq[ThemeIdModel] = Seq(ownProps.wrapped.currentTheme.id)

      def getProposals(tags: Option[Seq[TagModel]],
                       searchContent: Option[String],
                       skip: Int = 0): Future[Seq[ProposalModel]] = {
        val proposals: Future[Seq[ProposalModel]] = proposalService
          .searchProposals(
            themesIds = themesIds,
            tagsIds = tags.getOrElse(Seq.empty).map(_.tagId),
            content = searchContent,
            options = Some(SearchOptionsRequest(sort = Seq.empty, limit = Some(resultSetCount), skip = Some(skip)))
          )
        proposals
      }

      /*     def handleNewProposals(matrix: ProposalsListSelf, proposals: Future[Seq[ProposalModel]]): Unit = {
        proposals.onComplete {
          case Success(listProposals) => updateMatrix(matrix = matrix, listProposals = listProposals)
          case Failure(_)             => dispatch(NotifyError(message = "errors.tryAgain", title = None))
        }
      }

      def handleSelectedTags(matrix: ProposalsListSelf)(selectedTags: Seq[TagModel]): Unit = {
        handleNewProposals(matrix, getProposals(Some(selectedTags), None))
      }

      def handleNextResults(matrix: ProposalsListSelf): Unit = {
        val skip: Int = matrix.state.listProposals.map(_.length).getOrElse(0)
        getProposals(Some(matrix.state.selectedTags), None, skip).onComplete {
          case Success(listProposals) =>
            updateMatrix(
              matrix = matrix,
              listProposals = matrix.state.listProposals.getOrElse(Seq.empty) ++ listProposals,
              showSeeMore = if (listProposals.isEmpty) Some(false) else None
            )
          case Failure(_) => dispatch(NotifyError(message = "errors.tryAgain", title = None))
        }
      }

      def mayHaveMoreResults(actualCount: Int): Boolean = {
        actualCount != 0 && actualCount % resultSetCount == 0
      }*/

      ResultsInTheme.ResultsInThemeProps(handleSelectedTags = (_) => { _ =>
        {}
      }, handleNextResults = _ => {}, tags = ownProps.wrapped.currentTheme.tags)
    }
}
