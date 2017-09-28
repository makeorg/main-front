package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.components.AppState
import org.make.front.components.submitProposal.SubmitProposalAndLogin.SubmitProposalAndLoginProps
import org.make.front.models.{Theme => ThemeModel, Operation => OperationModel}
import org.make.services.proposal.ProposalService

object SubmitProposalAndLoginContainer {

  case class SubmitProposalAndLoginContainerProps(maybeTheme: Option[ThemeModel],
                                                  maybeOperation: Option[OperationModel],
                                                  onProposalProposed: () => Unit)

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _ => (state: AppState, props: Props[SubmitProposalAndLoginContainerProps]) =>
      SubmitProposalAndLoginProps(
        maybeTheme = props.wrapped.maybeTheme,
        maybeOperation = props.wrapped.maybeOperation,
        onProposalProposed = props.wrapped.onProposalProposed,
        propose = ProposalService.createProposal
      )
  }(SubmitProposalAndLogin.reactClass)

}
