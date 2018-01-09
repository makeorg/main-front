package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  Qualification     => QualificationModel,
  Sequence          => SequenceModel,
  TranslatedTheme   => TranslatedThemeModel,
  Vote              => VoteModel
}
import org.make.services.proposal.ProposalService
import org.make.services.tracking.{TrackingLocation, TrackingService}
import org.make.services.tracking.TrackingService.TrackingContext
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
                                      maybeSequence: Option[SequenceModel],
                                      maybeLocation: Option[LocationModel])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Vote.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[VoteContainerProps]) => Vote.VoteProps =
    (dispatch: Dispatch) => { (_: AppState, props: Props[VoteContainerProps]) =>
      val location = LocationModel.firstByPrecedence(
        location = props.wrapped.maybeLocation,
        sequence = props.wrapped.maybeSequence.map(sequence => LocationModel.Sequence(sequence.sequenceId)),
        themePage = props.wrapped.maybeTheme.map(theme      => LocationModel.ThemePage(theme.id)),
        operationPage =
          props.wrapped.maybeOperation.map(operation => LocationModel.OperationPage(operation.operationId)),
        fallback = LocationModel.UnknownLocation(dom.window.location.href)
      )

      def defaultVoteParameters: Map[String, String] = {
        var parameters = Map("proposalId" -> props.wrapped.proposal.id.value)
        props.wrapped.maybeTheme.foreach { theme =>
          parameters += "themeId" -> theme.id.value
        }
        props.wrapped.maybeSequence.foreach { sequence =>
          parameters += "sequenceId" -> sequence.sequenceId.value
          parameters += "card-position" -> props.wrapped.index.toString

        }
        parameters
      }

      def vote(key: String): Future[VoteModel] = {
        val parameters = Map("nature" -> key) ++ defaultVoteParameters

        TrackingService.track(
          "click-proposal-vote",
          TrackingContext(props.wrapped.trackingLocation, operationSlug = props.wrapped.maybeOperation.map(_.slug)),
          parameters
        )
        val future = ProposalService.vote(
          proposalId = props.wrapped.proposal.id,
          key,
          location,
          operation = props.wrapped.maybeOperation
        )

        future.onComplete {
          case Success(response) => props.wrapped.onSuccessfulVote(response) // let child handle new results
          case Failure(_)        => dispatch(NotifyError(I18n.t("errors.main")))
        }
        future
      }

      def unvote(key: String): Future[VoteModel] = {
        val parameters = Map("nature" -> key) ++ defaultVoteParameters

        TrackingService.track(
          "click-proposal-unvote",
          TrackingContext(props.wrapped.trackingLocation, operationSlug = props.wrapped.maybeOperation.map(_.slug)),
          parameters
        )
        val future = ProposalService.unvote(
          proposalId = props.wrapped.proposal.id,
          key,
          location,
          operation = props.wrapped.maybeOperation
        )
        future.onComplete {
          case Success(response) => props.wrapped.onSuccessfulVote(response) // let child handle new results
          case Failure(_)        => dispatch(NotifyError(I18n.t("errors.main")))
        }
        future
      }

      val qualify: (String, String) => Future[QualificationModel] = { (vote, qualification) =>
        val parameters = Map("type" -> qualification, "nature" -> vote) ++ defaultVoteParameters

        TrackingService.track(
          "click-proposal-qualify",
          TrackingContext(props.wrapped.trackingLocation, operationSlug = props.wrapped.maybeOperation.map(_.slug)),
          parameters
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
          case Failure(_)        => dispatch(NotifyError(I18n.t("errors.main")))
        }
        future
      }

      val removeQualification: (String, String) => Future[QualificationModel] = { (vote, qualification) =>
        val parameters = Map("type" -> qualification, "nature" -> vote) ++ defaultVoteParameters

        TrackingService.track(
          "click-proposal-unqualify",
          TrackingContext(props.wrapped.trackingLocation, operationSlug = props.wrapped.maybeOperation.map(_.slug)),
          parameters
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
          case Failure(_)        => dispatch(NotifyError(I18n.t("errors.main")))
        }
        future
      }

      Vote.VoteProps(
        proposal = props.wrapped.proposal,
        vote = vote,
        unvote = unvote,
        qualifyVote = qualify,
        removeVoteQualification = removeQualification,
        guideToVote = props.wrapped.guideToVote,
        guideToQualification = props.wrapped.guideToQualification,
        index = props.wrapped.index
      )
    }
}
