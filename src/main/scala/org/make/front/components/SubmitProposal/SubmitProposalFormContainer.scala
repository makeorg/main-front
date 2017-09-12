package org.make.front.components.SubmitProposal

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions._
import org.make.front.components.presentationals.SubmitProposalFormComponent
import org.make.front.components.presentationals.SubmitProposalFormComponent.{
  SubmitProposalFormProps,
  SubmitProposalFormState
}
import org.make.front.facades.{Configuration, I18n}
import org.make.front.models.{AppState, Theme}
import org.make.services.proposal.ProposalResponses.RegisterProposalResponse
import org.make.services.proposal.ProposalServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object SubmitProposalFormContainerComponent extends ProposalServiceComponent {

  override def apiBaseUrl: String = Configuration.apiUrl

  case class SubmitProposalFormContainerProps(theme: Theme,
                                              isConfirmed: Boolean = false,
                                              proposalPrefix: String,
                                              proposalPlaceHolder: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SubmitProposalFormComponent.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState,
                     Props[SubmitProposalFormContainerProps]) => SubmitProposalFormComponent.SubmitProposalFormProps =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[SubmitProposalFormContainerProps]) =>
      {
        def handleSubmitProposalForm(child: Self[SubmitProposalFormProps, SubmitProposalFormState]) = {
          val content = child.state.proposalContent
          def registerProposal(): Unit = {
            handleFutureApiResponse(proposalService.createProposal(content), child)
          }
          if (appState.connectedUser.isDefined) {
            registerProposal()
          } else {
            dispatch(StorePendingProposal(content = content, registerProposal = registerProposal))
            dispatch(LoginRequired(isProposalFlow = true))
          }
        }

        def handleFutureApiResponse(futureProposal: Future[RegisterProposalResponse],
                                    child: Self[SubmitProposalFormProps, SubmitProposalFormState]): Unit = {
          futureProposal.onComplete {
            case Success(_) =>
              child.setState(child.state.copy(confirmationIsOpen = true, proposalContent = "", modalIsOpen = true))
            case Failure(_) =>
              dispatch(NotifyError("errors.tryAgain", Some("errors.unexpectedBehaviour")))
              child.setState(child.state.copy(errorMessage = I18n.t("form.proposal.errorSubmitFailed")))
          }
        }

        SubmitProposalFormComponent.SubmitProposalFormProps(
          proposalPrefix = props.wrapped.proposalPrefix,
          proposalPlaceHolder = props.wrapped.proposalPlaceHolder,
          theme = props.wrapped.theme,
          handleSubmitProposalForm = handleSubmitProposalForm
        )

      }
    }
}
