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

package org.make.front.operations

import org.make.front.components.sequence.Sequence.{DisplayTracker, ExtraSlide}
import org.make.front.components.sequence.contents.IntroductionOfTheSequence.IntroductionOfTheSequenceProps
import org.make.front.components.sequence.contents.PromptingToConnect.PromptingToConnectProps
import org.make.front.components.sequence.contents.PromptingToGoBackToOperation.PromptingToGoBackToOperationProps
import org.make.front.components.sequence.contents.PromptingToGoBackToOperationLight.PromptingToGoBackToOperationLightProps
import org.make.front.components.sequence.contents._
import org.make.front.components.sequence.contents.PromptingToProposeInRelationToOperation.PromptingToProposeInRelationToOperationProps
import org.make.front.models._
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext

import scala.scalajs.js

trait StaticDataOfOperation {
  val data: OperationStaticData
}

object Operations {
  val featuredOperationSlug: String = "vff"

  val operationStaticDataList: js.Array[OperationStaticData] = js.Array(
    VFFOperationStaticData.data,
    VFFITOperationStaticData.data,
    VFFGBOperationStaticData.data,
    ClimatParisOperationStaticData.data,
    LPAEOperationStaticData.data,
    MVEOperationStaticData.data,
    ChanceAuxJeunesOperationStaticData.data,
    CultureOperationStaticData.data,
    AinesOperationStaticData.data,
    NiceMatinOperationStaticData.data
  )
}

object Slides {
  def trackingContext(params: OperationExtraSlidesParams) =
    TrackingContext(TrackingLocation.sequencePage, Some(params.operation.slug))
  def defaultTrackingInternalOnlyParameters(params: OperationExtraSlidesParams) =
    Map("sequenceId" -> params.sequence.sequenceId.value, "operation" -> params.operation.slug)
  def displaySequenceIntroCard(params: OperationExtraSlidesParams,
                               displayed: Boolean = true,
                               introWording: OperationIntroWording): ExtraSlide = {
    ExtraSlide(
      reactClass = IntroductionOfTheSequence.reactClass,
      slideName = "intro",
      props = { (handler: () => Unit) =>
        {
          val onClick: () => Unit = () => {
            TrackingService
              .track(
                eventName = "click-sequence-launch",
                trackingContext = trackingContext(params),
                parameters = Map.empty,
                internalOnlyParameters = defaultTrackingInternalOnlyParameters(params)
              )
            handler()
          }
          IntroductionOfTheSequenceProps(clickOnButtonHandler = onClick, introWording = introWording)
        }
      },
      position = _ => 0,
      displayed = displayed,
      maybeTracker = Some(
        DisplayTracker(
          name = "display-sequence-intro-card",
          context = trackingContext(params),
          parameters = Map.empty,
          internalOnlyParameters = defaultTrackingInternalOnlyParameters(params)
        )
      )
    )
  }

  def displaySignUpCard(params: OperationExtraSlidesParams, displayed: Boolean = true) = ExtraSlide(
    reactClass = PromptingToConnect.reactClass,
    slideName = "register",
    props = { handler =>
      PromptingToConnectProps(
        operation = params.operation,
        sequenceId = params.sequence.sequenceId,
        trackingContext = trackingContext(params),
        trackingParameters = Map.empty,
        trackingInternalOnlyParameters = defaultTrackingInternalOnlyParameters(params),
        clickOnButtonHandler = handler,
        authenticateHandler = () => {}
      )
    },
    position = { slides =>
      slides.size
    },
    displayed = displayed,
    maybeTracker = Some(
      DisplayTracker(
        "display-sign-up-card",
        trackingContext(params),
        Map.empty,
        defaultTrackingInternalOnlyParameters(params)
      )
    )
  )

  def displayProposalPushCard(params: OperationExtraSlidesParams, displayed: Boolean = true): ExtraSlide = {
    ExtraSlide(
      reactClass = PromptingToProposeInRelationToOperation.reactClass,
      slideName = "propose",
      props = { handler =>
        PromptingToProposeInRelationToOperationProps(
          operation = params.operation,
          clickOnButtonHandler = handler,
          proposeHandler = handler,
          sequenceId = params.sequence.sequenceId,
          maybeLocation = params.maybeLocation,
          language = params.language,
          handleCanUpdate = params.handleCanUpdate
        )
      },
      position = { slides =>
        slides.size / 2
      },
      displayed = displayed,
      maybeTracker = Some(
        DisplayTracker(
          "display-proposal-push-card",
          trackingContext(params),
          Map.empty,
          defaultTrackingInternalOnlyParameters(params)
        )
      )
    )
  }

  def displayFinalCard(params: OperationExtraSlidesParams, displayed: Boolean = true, onFocus: () => Unit = () => {}) =
    ExtraSlide(
      reactClass = PromptingToGoBackToOperation.reactClass,
      slideName = "finale",
      props = { handler =>
        PromptingToGoBackToOperationProps(
          operation = params.operation,
          clickOnButtonHandler = handler,
          sequenceId = params.sequence.sequenceId,
          language = params.language,
          country = params.country
        )
      },
      position = { slides =>
        slides.size
      },
      displayed = displayed,
      maybeTracker = Some(
        DisplayTracker(
          "display-finale-card",
          trackingContext(params),
          Map.empty,
          defaultTrackingInternalOnlyParameters(params)
        )
      ),
      onFocus = onFocus
    )

  def redirectToConsultationCard(params: OperationExtraSlidesParams,
                                 displayed: Boolean = true,
                                 onFocus: () => Unit = () => {}) =
    ExtraSlide(
      reactClass = PromptingToGoBackToOperationLight.reactClass,
      slideName = "redirect",
      props = { handler =>
        PromptingToGoBackToOperationLightProps(
          operation = params.operation,
          clickOnButtonHandler = handler,
          sequenceId = params.sequence.sequenceId,
          language = params.language,
          country = params.country
        )
      },
      position = { slides =>
        slides.size
      },
      displayed = displayed,
      maybeTracker = Some(
        DisplayTracker(
          "display-finale-card",
          trackingContext(params),
          Map.empty,
          defaultTrackingInternalOnlyParameters(params)
        )
      ),
      onFocus = onFocus
    )
}
