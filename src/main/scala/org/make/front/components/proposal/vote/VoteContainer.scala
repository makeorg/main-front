package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{
  Location        => LocationModel,
  Operation       => OperationModel,
  Proposal        => ProposalModel,
  Qualification   => QualificationModel,
  Sequence        => SequenceModel,
  TranslatedTheme => TranslatedThemeModel,
  Vote            => VoteModel
}
import org.make.services.proposal.ProposalService
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
                                      locationFBTracking: Option[String] = None,
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
      def vote(key: String): Future[VoteModel] = {
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
        locationFacebook = props.wrapped.locationFBTracking,
        guideToVote = props.wrapped.guideToVote,
        guideToQualification = props.wrapped.guideToQualification,
        index = props.wrapped.index
      )
    }
}
