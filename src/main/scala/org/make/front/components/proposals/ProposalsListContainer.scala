package org.make.front.components.proposals

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.proposals.ProposalsList.ProposalsListSelf
import org.make.front.facades.Configuration
import org.make.front.models.{Proposal => ProposalModel, Tag => TagModel, ThemeId => ThemeIdModel}
import org.make.services.proposal.{ProposalServiceComponent, SearchOptionsRequest}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ProposalsListContainer extends ProposalServiceComponent {

  override def apiBaseUrl: String = Configuration.apiUrl
  private val resultSetCount = 20

  case class MatrixWrappedProps(themeSlug: Option[String] = None,
                                searchValue: () => Option[String] = () => None,
                                handleResults: Option[(Seq[ProposalModel]) => Unit] = None,
                                showTagsSelect: Boolean = true,
                                noContentText: () => String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ProposalsList.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[MatrixWrappedProps]) => ProposalsList.ProposalsListProps =
    (dispatch: Dispatch) => { (appState: AppState, ownProps: Props[MatrixWrappedProps]) =>
      val mayBeThemesId: Option[ThemeIdModel] = for {
        themeSlug <- ownProps.wrapped.themeSlug
        theme     <- appState.themes.find(_.slug == themeSlug)
      } yield theme.id

      val themesIds: Seq[ThemeIdModel] = mayBeThemesId.map(Seq(_)).getOrElse(Seq.empty)

      def updateMatrix(matrix: ProposalsListSelf,
                       listProposals: Seq[ProposalModel],
                       showSeeMore: Option[Boolean] = None): Unit = {

        ownProps.wrapped.handleResults.foreach(_(listProposals))
        matrix.setState(
          _.copy(
            listProposals = Some(listProposals),
            searchValue = matrix.props.wrapped.searchValue(),
            showSeeMoreButton = showSeeMore.getOrElse(mayHaveMoreResults(listProposals.length))
          )
        )
      }

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

      def handleNewProposals(matrix: ProposalsListSelf, proposals: Future[Seq[ProposalModel]]): Unit = {
        proposals.onComplete {
          case Success(listProposals) => updateMatrix(matrix = matrix, listProposals = listProposals)
          case Failure(_)             => dispatch(NotifyError(message = "errors.tryAgain", title = None))
        }
      }

      def handleSelectedTags(matrix: ProposalsListSelf)(selectedTags: Seq[TagModel]): Unit = {
        handleNewProposals(matrix, getProposals(Some(selectedTags), ownProps.wrapped.searchValue()))
      }

      def handleSearchValueChange(matrix: ProposalsListSelf): Unit = {
        handleNewProposals(matrix, getProposals(Some(matrix.state.selectedTags), ownProps.wrapped.searchValue()))
      }

      def handleNextResults(matrix: ProposalsListSelf): Unit = {
        val skip: Int = matrix.state.listProposals.map(_.length).getOrElse(0)
        getProposals(Some(matrix.state.selectedTags), ownProps.wrapped.searchValue(), skip).onComplete {
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
      }

      val mayBeTags: Option[Seq[TagModel]] = for {
        themeSlug <- ownProps.wrapped.themeSlug
        theme     <- appState.themes.find(_.slug == themeSlug)
      } yield theme.tags

      val tags = mayBeTags.getOrElse(Seq.empty)

      ProposalsList.ProposalsListProps(
        handleSelectedTags = handleSelectedTags,
        handleSearchValueChange = handleSearchValueChange,
        handleNextResults = handleNextResults,
        tags = tags,
        showTagsSelect = ownProps.wrapped.showTagsSelect,
        noContentText = ownProps.wrapped.noContentText,
        searchValue = ownProps.wrapped.searchValue
      )
    }
}
