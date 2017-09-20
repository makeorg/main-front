package org.make.front.components.proposals

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.proposals.ProposalsList.ProposalsListSelf
import org.make.front.facades.Configuration
import org.make.front.models.{Tag => TagModel, ThemeId => ThemeIdModel}
import org.make.services.proposal.ProposalServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ProposalsListContainer extends ProposalServiceComponent {

  override def apiBaseUrl: String = Configuration.apiUrl

  case class MatrixWrappedProps(themeSlug: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ProposalsList.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[MatrixWrappedProps]) => ProposalsList.ProposalsListProps =
    (dispatch: Dispatch) => { (appState: AppState, ownProps: Props[MatrixWrappedProps]) =>
      val themesIds
        : Seq[ThemeIdModel] = Seq(appState.themes.find(_.slug == ownProps.wrapped.themeSlug).map(_.id)).flatten

      def searchProposalsByTags(matrix: ProposalsListSelf, selectedTags: Seq[TagModel]): Unit =
        proposalService
          .searchProposals(themesIds = themesIds, tagsIds = selectedTags.map(_.tagId))
          .onComplete {
            case Success(listProposals) => matrix.setState(_.copy(listProposals = listProposals))
            case Failure(_)             => dispatch(NotifyError("errors.tryAgain", None))
          }

      val tags: Seq[TagModel] =
        appState.themes.find(_.slug == ownProps.wrapped.themeSlug).map(_.tags).getOrElse(Seq.empty)

      def handleSelectedTags(matrix: ProposalsListSelf)(selectedTags: Seq[TagModel]): Unit = {
        searchProposalsByTags(matrix, selectedTags)
      }

      ProposalsList.ProposalsListProps(handleSelectedTags, tags)

    }

}
