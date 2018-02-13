package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{
  Operation,
  Tag,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.services.operation.OperationService
import org.make.services.proposal.{ProposalService, SearchResult}
import org.make.services.tag.TagService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ProposalContainer {

  case class ProposalAndThemeOrOperationModel(maybeProposal: Option[ProposalModel] = None,
                                              maybeTheme: Option[TranslatedThemeModel] = None,
                                              maybeOperation: Option[OperationModel] = None)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Proposal.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Proposal.ProposalProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {

        val futureProposal: Future[ProposalAndThemeOrOperationModel] = {

          val proposalSlug = props.`match`.params("proposalSlug")

          val futureSearchResult: Future[SearchResult] =
            ProposalService.searchProposals(slug = Some(proposalSlug), limit = Some(1), sort = Seq.empty, skip = None)

          def getProposalAndThemeOrOperationModelFromProposal(
            proposal: ProposalModel
          ): Future[ProposalAndThemeOrOperationModel] = {
            val maybeTheme: Option[TranslatedThemeModel] =
              proposal.themeId.flatMap(themeId => state.themes.find(_.id.value == themeId.value))

            val futureMaybeOperation: Future[Option[OperationModel]] = proposal.operationId match {
              case Some(operationId) =>
                val operationAndTags: Future[(Operation, Seq[Tag])] = for {
                  operation <- OperationService.getOperationById(operationId)
                  tags      <- TagService.getTags
                } yield (operation, tags)
                operationAndTags.map {
                  case (operation, tagsList) =>
                    OperationModel
                      .getOperationExpandedFromOperation(
                        maybeOperation = Some(operation),
                        tagsList,
                        country = state.country
                      )
                }
              case _ => Future.successful(None)
            }

            futureMaybeOperation.map { maybeOperation =>
              ProposalAndThemeOrOperationModel(
                maybeProposal = Some(proposal),
                maybeTheme = maybeTheme,
                maybeOperation = maybeOperation
              )
            }
          }

          val futureProposalAndThemeOrOperationModel: Future[ProposalAndThemeOrOperationModel] =
            futureSearchResult.flatMap {
              case searchResult if searchResult.total > 0 =>
                getProposalAndThemeOrOperationModelFromProposal(searchResult.results.head)
              case _ => Future.successful(ProposalAndThemeOrOperationModel())
            }

          futureProposalAndThemeOrOperationModel.onComplete {
            case Success(_) => // let child handle new results
            case Failure(_) => dispatch(NotifyError(I18n.t("error-message.main")))
          }

          futureProposalAndThemeOrOperationModel
        }

        Proposal.ProposalProps(futureProposal = futureProposal, language = state.language, country = state.country)
      }
    }
}
