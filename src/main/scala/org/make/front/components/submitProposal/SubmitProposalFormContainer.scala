/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
                                              trackingInternalOnlyParameters: Map[String, String],
                                              maybeTheme: Option[TranslatedThemeModel],
                                              errorMessage: Option[String],
                                              handleSubmitProposalForm: (String) => Unit)

  val reactClass: ReactClass =
    ReactRedux.connectAdvanced[AppState, SubmitProposalFormContainerProps, SubmitProposalFormProps] {
      _ => (state, props) =>
        SubmitProposalFormProps(
          trackingContext = props.wrapped.trackingContext,
          trackingParameters = props.wrapped.trackingParameters,
          trackingInternalOnlyParameters = props.wrapped.trackingInternalOnlyParameters,
          bait = state.bait,
          proposalContentMaxLength = state.configuration.map(_.proposalMaxLength).getOrElse(140),
          proposalContentMinLength = state.configuration.map(_.proposalMinLength).getOrElse(10),
          maybeTheme = props.wrapped.maybeTheme,
          errorMessage = props.wrapped.errorMessage,
          handleSubmitProposalForm = props.wrapped.handleSubmitProposalForm
        )
    }(SubmitProposalForm.reactClass)

}
