package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.components.AppState
import org.make.front.components.submitProposal.SubmitProposalAndLogin.SubmitProposalAndLoginProps
import org.make.front.facades.Configuration
import org.make.front.models.{Theme => ThemeModel}
import org.make.services.proposal.ProposalServiceComponent

object SubmitProposalAndLoginContainer extends ProposalServiceComponent {

  override def apiBaseUrl: String = Configuration.apiUrl

  case class SubmitProposalAndLoginContainerProps(bait: String,
                                                  proposalContentMaxLength: Int,
                                                  maybeTheme: Option[ThemeModel],
                                                  onProposalProposed: () => Unit)

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _ => (_: AppState, props: Props[SubmitProposalAndLoginContainerProps]) =>
      SubmitProposalAndLoginProps(
        bait = props.wrapped.bait,
        proposalContentMaxLength = props.wrapped.proposalContentMaxLength,
        maybeTheme = props.wrapped.maybeTheme,
        onProposalProposed = props.wrapped.onProposalProposed,
        propose = proposalService.createProposal
      )
  }(SubmitProposalAndLogin.reactClass)

}
