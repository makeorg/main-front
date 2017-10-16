package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.components.AppState
import org.make.front.components.submitProposal.SubmitProposalAndLogin.SubmitProposalAndLoginProps
import org.make.front.models.{Location => LocationModel, Operation => OperationModel, Theme => ThemeModel}
import org.make.services.proposal.ProposalResponses.RegisterProposalResponse
import org.make.services.proposal.ProposalService

import scala.concurrent.Future

object SubmitProposalAndLoginContainer {

  case class SubmitProposalAndLoginContainerProps(intro: (ReactElement) => ReactElement = identity,
                                                  maybeTheme: Option[ThemeModel],
                                                  maybeOperation: Option[OperationModel],
                                                  onProposalProposed: () => Unit)

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _ => (_: AppState, props: Props[SubmitProposalAndLoginContainerProps]) =>
      def propose(content: String, location: LocationModel): Future[RegisterProposalResponse] =
        ProposalService.createProposal(
          content,
          location = props.wrapped.maybeOperation.map(_ => LocationModel.Sequence).getOrElse(location),
          themeId = props.wrapped.maybeTheme.map(_.id.value),
          operation = props.wrapped.maybeOperation.map(_.label)
        )

      SubmitProposalAndLoginProps(
        intro = props.wrapped.intro,
        maybeTheme = props.wrapped.maybeTheme,
        maybeOperation = props.wrapped.maybeOperation,
        onProposalProposed = props.wrapped.onProposalProposed,
        propose = propose
      )
  }(SubmitProposalAndLogin.reactClass)

}
