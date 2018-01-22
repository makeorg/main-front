package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.components.AppState
import org.make.front.components.submitProposal.SubmitProposalAndAuthenticate.SubmitProposalAndAuthenticateProps
import org.make.front.models.{
  RegisterProposal,
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Sequence          => SequenceModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.services.proposal.ProposalService
import org.make.services.tracking.TrackingService.TrackingContext
import org.scalajs.dom

import scala.concurrent.Future

object SubmitProposalAndAuthenticateContainer {

  case class SubmitProposalAndAuthenticateContainerProps(intro: (ReactElement) => ReactElement = identity,
                                                         trackingContext: TrackingContext,
                                                         onProposalProposed: () => Unit,
                                                         maybeTheme: Option[TranslatedThemeModel],
                                                         maybeOperation: Option[OperationModel],
                                                         maybeSequence: Option[SequenceId],
                                                         maybeLocation: Option[LocationModel])

  val reactClass: ReactClass = ReactRedux.connectAdvanced {
    _ => (_: AppState, props: Props[SubmitProposalAndAuthenticateContainerProps]) =>
      def propose(content: String): Future[RegisterProposal] = {
        val location = LocationModel.firstByPrecedence(
          location = props.wrapped.maybeLocation,
          sequence = props.wrapped.maybeSequence.map(sequenceId => LocationModel.Sequence(sequenceId)),
          themePage = props.wrapped.maybeTheme.map(theme        => LocationModel.ThemePage(theme.id)),
          operationPage =
            props.wrapped.maybeOperation.map(operation => LocationModel.OperationPage(operation.operationId)),
          fallback = LocationModel.UnknownLocation(dom.window.location.href)
        )
        ProposalService.createProposal(
          content,
          location = location,
          themeId = props.wrapped.maybeTheme.map(_.id.value),
          operation = props.wrapped.maybeOperation
        )
      }

      SubmitProposalAndAuthenticateProps(
        trackingContext = props.wrapped.trackingContext,
        intro = props.wrapped.intro,
        maybeTheme = props.wrapped.maybeTheme,
        maybeOperation = props.wrapped.maybeOperation,
        onProposalProposed = props.wrapped.onProposalProposed,
        propose = propose
      )
  }(SubmitProposalAndAuthenticate.reactClass)

}
