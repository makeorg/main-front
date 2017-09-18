package org.make.front.components.SubmitProposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions._
import org.make.front.components.SubmitProposal.SubmitProposalFormComponent.SubmitProposalFormSelf
import org.make.front.facades.Configuration
import org.make.front.models.{AppState, Theme}
import org.make.services.proposal.ProposalResponses.RegisterProposalResponse
import org.make.services.proposal.ProposalServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Failure, Success}

object SubmitProposalContainerComponent extends ProposalServiceComponent {

  override def apiBaseUrl: String = Configuration.apiUrl

  case class SubmitProposalFormContainerProps(maybeTheme: Option[Theme], handleSavedProposal: () => _)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SubmitProposalFormComponent.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState,
                     Props[SubmitProposalFormContainerProps]) => SubmitProposalFormComponent.SubmitProposalFormProps =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[SubmitProposalFormContainerProps]) =>
      {
        def handleSubmitProposalForm(child: SubmitProposalFormSelf) = {
          val content = child.state.proposalContent

          def registerProposal(): Unit = {
            handleFutureApiResponse(proposalService.createProposal(content), child)
          }

          registerProposal()
          if (appState.connectedUser.isDefined) {
            registerProposal()
          } else {
            dispatch(StorePendingProposal(content = content, registerProposal = registerProposal))
            dispatch(LoginRequired(isProposalFlow = true))
          }
        }

        def handleFutureApiResponse(futureProposalResponse: Future[RegisterProposalResponse],
                                    child: SubmitProposalFormSelf): Unit = {
          futureProposalResponse.onComplete {
            case Success(_) =>
              props.wrapped.handleSavedProposal
            case Failure(_) =>
              dispatch(NotifyError("errors.tryAgain", Some("errors.unexpectedBehaviour")))
              child.setState(child.state.copy(SubmissionError = true))
          }
        }

        SubmitProposalFormComponent.SubmitProposalFormProps(
          maybeTheme = props.wrapped.maybeTheme,
          handleSubmitProposalForm = handleSubmitProposalForm
        )
      }
    }
}
