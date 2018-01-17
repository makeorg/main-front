package org.make.front.components.submitProposal

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import org.make.front.components.AppState
import org.make.front.components.submitProposal.SubmitProposalForm.SubmitProposalFormProps
import org.make.front.models.{TranslatedTheme => TranslatedThemeModel}
import org.make.services.tracking.TrackingService.TrackingContext

object SubmitProposalFormContainer {

  case class SubmitProposalFormContainerProps(trackingContext: TrackingContext,
                                              trackingParameters: Map[String, String],
                                              maybeTheme: Option[TranslatedThemeModel],
                                              errorMessage: Option[String],
                                              handleSubmitProposalForm: (String) => Unit)

  val reactClass: ReactClass =
    ReactRedux.connectAdvanced[AppState, SubmitProposalFormContainerProps, SubmitProposalFormProps] {
      _ => (state, props) =>
        SubmitProposalFormProps(
          trackingContext = props.wrapped.trackingContext,
          trackingParameters = props.wrapped.trackingParameters,
          bait = state.bait,
          proposalContentMaxLength = state.configuration.map(_.proposalMaxLength).getOrElse(140),
          proposalContentMinLength = state.configuration.map(_.proposalMinLength).getOrElse(10),
          maybeTheme = props.wrapped.maybeTheme,
          errorMessage = props.wrapped.errorMessage,
          handleSubmitProposalForm = props.wrapped.handleSubmitProposalForm
        )
    }(SubmitProposalForm.reactClass)

}
