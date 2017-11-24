package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.components.AppState
import org.make.front.components.submitProposal.SubmitProposalAndLogin.SubmitProposalAndLoginProps
import org.make.front.models.{
  RegisterProposal,
  Location        => LocationModel,
  Operation       => OperationModel,
  Sequence        => SequenceModel,
  TranslatedTheme => TranslatedThemeModel
}
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.services.proposal.ProposalService

import scala.concurrent.Future

object SubmitProposalAndLoginContainer {

  case class SubmitProposalAndLoginContainerProps(intro: (ReactElement)  => ReactElement = identity,
                                                  onProposalProposed: () => Unit,
                                                  maybeTheme: Option[TranslatedThemeModel],
                                                  maybeOperation: Option[OperationModel],
                                                  maybeSequence: Option[SequenceModel] /* = None*/,
                                                  maybeLocation: Option[LocationModel] /* = None*/ )

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _ => (_: AppState, props: Props[SubmitProposalAndLoginContainerProps]) =>
      def propose(content: String): Future[RegisterProposal] = {
        val location = props.wrapped.maybeSequence
          .map(sequence => LocationModel.Sequence(sequence.sequenceId))
          .orElse(props.wrapped.maybeTheme.map(theme => LocationModel.ThemePage(theme.id)))
          .orElse(
            props.wrapped.maybeOperation
              .map(operation => LocationModel.OperationPage(operation.operationId))
          )
          .orElse(props.wrapped.maybeLocation)
          .getOrElse(LocationModel.UnknownLocation(s"${props.location.pathname}#${props.location.action}"))
        ProposalService.createProposal(
          content,
          location = location,
          themeId = props.wrapped.maybeTheme.map(_.id.value),
          operation = props.wrapped.maybeOperation.map(_.label)
        )
      }

      SubmitProposalAndLoginProps(
        intro = props.wrapped.intro,
        maybeTheme = props.wrapped.maybeTheme,
        maybeOperation = props.wrapped.maybeOperation,
        onProposalProposed = props.wrapped.onProposalProposed,
        propose = propose
      )
  }(SubmitProposalAndLogin.reactClass)

}
