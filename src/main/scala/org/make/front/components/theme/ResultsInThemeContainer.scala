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
  Proposal,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel,
  Tag               => TagModel,
  ThemeId           => ThemeIdModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.services.proposal.ProposalService.defaultResultsCount
import org.make.services.proposal.{ProposalService, SearchResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ResultsInThemeContainer {

  case class ResultsInThemeContainerProps(currentTheme: TranslatedThemeModel,
                                          maybeOperation: Option[OperationModel],
                                          maybeSequence: Option[SequenceModel],
                                          maybeLocation: Option[LocationModel])
  case class ResultsInThemeContainerState(currentTheme: TranslatedThemeModel, results: Seq[Proposal])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ResultsInTheme.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ResultsInThemeContainerProps]) => ResultsInTheme.ResultsInThemeProps =
    (dispatch: Dispatch) => { (_: AppState, props: Props[ResultsInThemeContainerProps]) =>
      val themesIds: Seq[ThemeIdModel] = Seq(props.wrapped.currentTheme.id)

      def getProposals(tags: Seq[TagModel], skip: Int, seed: Option[Int] = None): Future[SearchResult] = {
        ProposalService
          .searchProposals(
            themesIds = themesIds,
            tagsIds = tags.map(_.tagId),
            content = None,
            seed = seed,
            sort = Seq.empty,
            limit = Some(defaultResultsCount),
            skip = Some(skip)
          )
      }

      def nextProposals(currentProposals: Seq[Proposal],
                        tags: Seq[TagModel],
                        seed: Option[Int] = None): Future[SearchResult] = {
        val result = getProposals(tags = tags, skip = currentProposals.size, seed = seed).map { results =>
          results.copy(results = currentProposals ++ results.results)
        }

        result.onComplete {
          case Success(_) => // Let child handle results
          case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
        }

        result
      }

      def searchOnSelectedTags(selectedTags: Seq[TagModel], seed: Option[Int] = None): Future[SearchResult] = {
        val result = getProposals(tags = selectedTags, skip = 0, seed = seed)

        result.onComplete {
          case Success(_) => // Let child handle results
          case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
        }

        result
      }

      ResultsInThemeProps(
        theme = props.wrapped.currentTheme,
        onMoreResultsRequested = nextProposals,
        onTagSelectionChange = searchOnSelectedTags,
        proposals = searchOnSelectedTags(Seq()),
        preselectedTags = Seq(),
        maybeOperation = props.wrapped.maybeOperation,
        maybeSequence = props.wrapped.maybeSequence,
        maybeLocation = props.wrapped.maybeLocation
      )
    }
}
