package org.make.front.components.containers

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions._
import org.make.front.components.presentationals.ProposalSubmitComponent
import org.make.front.components.presentationals.ProposalSubmitComponent.{ProposalSubmitProps, ProposalSubmitState}
import org.make.front.facades.{Configuration, I18n}
import org.make.front.models.{AppState, Theme}
import org.make.services.proposal.ProposalResponses.RegisterProposalResponse
import org.make.services.proposal.ProposalServiceComponent

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ProposalSubmitContainerComponent extends ProposalServiceComponent {

  override def apiBaseUrl: String = Configuration.apiUrl

  case class ProposalSubmitContainerProps(theme: Theme,
                                          isConfirmed: Boolean = false,
                                          proposalPrefix: String,
                                          proposalPlaceHolder: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(ProposalSubmitComponent.reactClass)

  def selectorFactory
    : (Dispatch) => (AppState, Props[ProposalSubmitContainerProps]) => ProposalSubmitComponent.ProposalSubmitProps =
    (dispatch: Dispatch) => { (appState: AppState, props: Props[ProposalSubmitContainerProps]) =>
      {
        def handleProposalSubmit(child: Self[ProposalSubmitProps, ProposalSubmitState]) = {
          val content = child.state.proposalContent
          def registerProposal(): Unit = {
            handleFutureApiResponse(proposalService.postProposal(content), child)
          }
          if (appState.connectedUser.isDefined) {
            registerProposal()
          } else {
            dispatch(StorePendingProposal(content = content, registerProposal = registerProposal))
            dispatch(LoginRequired(isProposalFlow = true))
          }
        }

        def handleFutureApiResponse(futureProposal: Future[RegisterProposalResponse],
                                    child: Self[ProposalSubmitProps, ProposalSubmitState]): Unit = {
          futureProposal.onComplete {
            case Success(_) =>
              child.setState(child.state.copy(confirmationIsOpen = true, proposalContent = "", modalIsOpen = true))
            case Failure(_) =>
              dispatch(NotifyError(I18n.t("errors.tryAgain"), Some(I18n.t("errors.unexpectedBehaviour"))))
              child.setState(child.state.copy(errorMessage = I18n.t("form.proposal.errorSubmitFailed")))
          }
        }

        ProposalSubmitComponent.ProposalSubmitProps(
          proposalPrefix = props.wrapped.proposalPrefix,
          proposalPlaceHolder = props.wrapped.proposalPlaceHolder,
          theme = props.wrapped.theme,
          handleProposalSubmit = handleProposalSubmit
        )

      }
    }
}
