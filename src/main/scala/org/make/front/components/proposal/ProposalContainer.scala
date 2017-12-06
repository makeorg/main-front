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
  Proposal        => ProposalModel,
  TranslatedTheme => TranslatedThemeModel,
  Operation       => OperationModel
}
import org.make.services.proposal.ProposalService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ProposalContainer {

  case class MaybeProposalAndThemeOrOperationModel(maybeProposal: Option[ProposalModel] = None,
                                                   maybeTheme: Option[TranslatedThemeModel] = None,
                                                   maybeOperation: Option[OperationModel] = None)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Proposal.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Proposal.ProposalProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {

        val futureProposal: Future[MaybeProposalAndThemeOrOperationModel] = {

          val proposalSlug = props.`match`.params("proposalSlug")

          val proposalsResponse =
            ProposalService
              .searchProposals(slug = Some(proposalSlug), limit = Some(1), sort = Seq.empty, skip = None)
              .map { proposalsResponse =>
                if (proposalsResponse.results.nonEmpty) {
                  proposalsResponse.results.head
                  val proposal = proposalsResponse.results.head

                  val maybeTheme: Option[TranslatedThemeModel] =
                    proposal.themeId.flatMap(themeId => state.themes.find(_.id.value == themeId.value))

                  /* TO-DO : why no operationId ? */
                  val maybeOperation: Option[OperationModel] =
                    proposal.operationId.flatMap { operationId =>
                      state.operations.find(_.operationId.value == operationId.value)
                    }

                  MaybeProposalAndThemeOrOperationModel(
                    maybeProposal = Some(proposal),
                    maybeTheme = maybeTheme,
                    maybeOperation = maybeOperation
                  )
                } else {
                  MaybeProposalAndThemeOrOperationModel()
                }
              }

          proposalsResponse.onComplete {
            case Success(_) => // let child handle new results
            case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
          }

          proposalsResponse
        }

        Proposal.ProposalProps(futureProposal = futureProposal)
      }
    }
}
