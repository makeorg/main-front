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

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.submitProposal.SubmitProposalAndAuthenticateContainer.SubmitProposalAndAuthenticateContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Location => LocationModel, OperationExpanded => OperationModel}
import org.make.front.styles._
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingLocation
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

object SubmitProposal {

  case class SubmitProposalProps(trackingParameters: Map[String, String],
                                 trackingInternalOnlyParameters: Map[String, String],
                                 onProposalProposed: () => Unit,
                                 maybeLocation: Option[LocationModel],
                                 maybeOperation: Option[OperationModel])

  case class SubmitProposalState()

  lazy val reactClass: ReactClass =
    WithRouter(
      React.createClass[SubmitProposalProps, SubmitProposalState](
        displayName = "SubmitProposal",
        getInitialState = { _ =>
          SubmitProposalState()
        },
        render = { self =>
          val intro: (ReactElement) => ReactElement = { element =>
            <.div()(
              <.p(^.className := js.Array(TextStyles.mediumText, TextStyles.intro, SubmitProposalStyles.intro))(
                unescape(I18n.t("submit-proposal.intro"))
              ),
              element,
              <.style()(SubmitProposalStyles.render[String])
            )
          }

          <.SubmitProposalAndAuthenticateContainerComponent(
            ^.wrapped :=
              SubmitProposalAndAuthenticateContainerProps(
                trackingContext = TrackingContext(TrackingLocation.submitProposalPage),
                trackingParameters = self.props.wrapped.trackingParameters,
                trackingInternalOnlyParameters = self.props.wrapped.trackingInternalOnlyParameters,
                intro = intro,
                onProposalProposed = self.props.wrapped.onProposalProposed,
                maybeTheme = None,
                maybeOperation = self.props.wrapped.maybeOperation,
                maybeSequence = None,
                maybeLocation = self.props.wrapped.maybeLocation
              )
          )()
        }
      )
    )
}

object SubmitProposalStyles extends StyleSheet.Inline {

  import dsl._

  val intro: StyleA =
    style(
      textAlign.center,
      marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(15)),
      ThemeStyles.MediaQueries.beyondSmall(marginBottom(ThemeStyles.SpacingValue.medium.pxToEm(18)))
    )
}
