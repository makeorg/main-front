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

package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.{NotifyError, VoteAction}
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  Qualification     => QualificationModel,
  TranslatedTheme   => TranslatedThemeModel,
  Vote              => VoteModel
}
import org.make.services.proposal.ProposalService
import org.make.services.tracking.TrackingService.TrackingContext
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object VoteContainer {

  final case class VoteContainerProps(index: Int,
                                      proposal: ProposalModel,
                                      onSuccessfulVote: (VoteModel)                           => Unit = (_)    => {},
                                      onSuccessfulQualification: (String, QualificationModel) => Unit = (_, _) => {},
                                      guideToVote: Option[String] = None,
                                      guideToQualification: Option[String] = None,
                                      trackingLocation: TrackingLocation,
                                      maybeTheme: Option[TranslatedThemeModel],
                                      maybeOperation: Option[OperationModel],
                                      maybeSequenceId: Option[SequenceId],
                                      maybeLocation: Option[LocationModel],
                                      isProposalSharable: Boolean)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Vote.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[VoteContainerProps]) => Vote.VoteProps =
    (dispatch: Dispatch) => { (_: AppState, props: Props[VoteContainerProps]) =>
      val location = LocationModel.firstByPrecedence(
        location = props.wrapped.maybeLocation,
        sequence = props.wrapped.maybeSequenceId.map(sequenceId => LocationModel.Sequence(sequenceId)),
        themePage = props.wrapped.maybeTheme.map(theme          => LocationModel.ThemePage(theme.id)),
        operationPage =
          props.wrapped.maybeOperation.map(operation => LocationModel.OperationPage(operation.operationId)),
        fallback = LocationModel.UnknownLocation(dom.window.location.href)
      )

      def defaultVoteParameters: Map[String, String] = {
        var parameters = Map.empty[String, String]

        props.wrapped.maybeSequenceId.foreach { sequenceId =>
          parameters += "card-position" -> props.wrapped.index.toString

        }
        parameters
      }

      def defaultVoteInternalOnlyParameters: Map[String, String] = {
        var parameters = Map("proposalId" -> props.wrapped.proposal.id.value)
        props.wrapped.maybeTheme.foreach { theme =>
          parameters += "themeId" -> theme.id.value
        }
        props.wrapped.maybeSequenceId.foreach { sequenceId =>
          parameters += "sequenceId" -> sequenceId.value
        }
        parameters
      }

      def vote(key: String): Future[VoteModel] = {
        val internalOnlyParameters = Map("nature" -> key) ++ defaultVoteInternalOnlyParameters

        TrackingService.track(
          eventName = "click-proposal-vote",
          trackingContext =
            TrackingContext(props.wrapped.trackingLocation, operationSlug = props.wrapped.maybeOperation.map(_.slug)),
          parameters = defaultVoteParameters,
          internalOnlyParameters = internalOnlyParameters
        )
        val future = ProposalService.vote(
          proposalId = props.wrapped.proposal.id,
          key,
          location,
          operation = props.wrapped.maybeOperation
        )

        future.onComplete {
          case Success(response) =>
            props.wrapped.onSuccessfulVote(response) // let child handle new results
            dispatch(VoteAction(location))
          case Failure(_) => dispatch(NotifyError(I18n.t("error-message.main")))
        }
        future
      }

      def unvote(key: String): Future[VoteModel] = {
        val internalOnlyParameters = Map("nature" -> key) ++ defaultVoteInternalOnlyParameters

        TrackingService.track(
          eventName = "click-proposal-unvote",
          trackingContext =
            TrackingContext(props.wrapped.trackingLocation, operationSlug = props.wrapped.maybeOperation.map(_.slug)),
          parameters = defaultVoteParameters,
          internalOnlyParameters = internalOnlyParameters
        )
        val future = ProposalService.unvote(
          proposalId = props.wrapped.proposal.id,
          key,
          location,
          operation = props.wrapped.maybeOperation
        )
        future.onComplete {
          case Success(response) => props.wrapped.onSuccessfulVote(response) // let child handle new results
          case Failure(_)        => dispatch(NotifyError(I18n.t("error-message.main")))
        }
        future
      }

      val qualify: (String, String) => Future[QualificationModel] = { (vote, qualification) =>
        val internalOnlyParameters = Map("type" -> qualification, "nature" -> vote) ++ defaultVoteInternalOnlyParameters

        TrackingService.track(
          eventName = "click-proposal-qualify",
          trackingContext =
            TrackingContext(props.wrapped.trackingLocation, operationSlug = props.wrapped.maybeOperation.map(_.slug)),
          parameters = defaultVoteParameters,
          internalOnlyParameters = internalOnlyParameters
        )

        val future = ProposalService.qualifyVote(
          props.wrapped.proposal.id,
          vote,
          qualification,
          location,
          operation = props.wrapped.maybeOperation
        )
        future.onComplete {
          case Success(response) => props.wrapped.onSuccessfulQualification(vote, response)
          case Failure(_)        => dispatch(NotifyError(I18n.t("error-message.main")))
        }
        future
      }

      val removeQualification: (String, String) => Future[QualificationModel] = { (vote, qualification) =>
        val internalOnlyParameters = Map("type" -> qualification, "nature" -> vote) ++ defaultVoteInternalOnlyParameters

        TrackingService.track(
          eventName = "click-proposal-unqualify",
          trackingContext =
            TrackingContext(props.wrapped.trackingLocation, operationSlug = props.wrapped.maybeOperation.map(_.slug)),
          parameters = defaultVoteParameters,
          internalOnlyParameters = internalOnlyParameters
        )

        val future = ProposalService.removeVoteQualification(
          props.wrapped.proposal.id,
          vote,
          qualification,
          location,
          operation = props.wrapped.maybeOperation
        )
        future.onComplete {
          case Success(response) => props.wrapped.onSuccessfulQualification(vote, response)
          case Failure(_)        => dispatch(NotifyError(I18n.t("error-message.main")))
        }
        future
      }

      Vote.VoteProps(
        proposal = props.wrapped.proposal,
        maybeOperation = props.wrapped.maybeOperation,
        vote = vote,
        unvote = unvote,
        qualifyVote = qualify,
        removeVoteQualification = removeQualification,
        guideToVote = props.wrapped.guideToVote,
        guideToQualification = props.wrapped.guideToQualification,
        index = props.wrapped.index,
        isProposalSharable = props.wrapped.isProposalSharable
      )
    }
}
