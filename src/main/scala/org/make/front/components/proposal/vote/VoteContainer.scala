package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{Proposal => ProposalModel, Qualification => QualificationModel, Vote => VoteModel}
import org.make.services.proposal.ProposalService

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
                                      locationFBTracking: Option[String] = None)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Vote.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[VoteContainerProps]) => Vote.VoteProps =
    (dispatch: Dispatch) => { (_: AppState, ownProps: Props[VoteContainerProps]) =>
      def vote(key: String): Future[VoteModel] = {
        val future = ProposalService.vote(proposalId = ownProps.wrapped.proposal.id, key)

        future.onComplete {
          case Success(response) => ownProps.wrapped.onSuccessfulVote(response) // let child handle new results
          case Failure(_)        => dispatch(NotifyError(I18n.t("errors.main")))
        }
        future
      }

      def unvote(key: String): Future[VoteModel] = {
        val future = ProposalService.unvote(proposalId = ownProps.wrapped.proposal.id, key)
        future.onComplete {
          case Success(response) => ownProps.wrapped.onSuccessfulVote(response) // let child handle new results
          case Failure(_)        => dispatch(NotifyError(I18n.t("errors.main")))
        }
        future
      }

      val qualify: (String, String) => Future[QualificationModel] = { (vote, qualification) =>
        val future = ProposalService.qualifyVote(ownProps.wrapped.proposal.id, vote, qualification)
        future.onComplete {
          case Success(response) => ownProps.wrapped.onSuccessfulQualification(vote, response)
          case Failure(_)        => dispatch(NotifyError(I18n.t("errors.main")))
        }
        future
      }

      val removeQualification: (String, String) => Future[QualificationModel] = { (vote, qualification) =>
        val future =
          ProposalService.removeVoteQualification(ownProps.wrapped.proposal.id, vote, qualification)
        future.onComplete {
          case Success(response) => ownProps.wrapped.onSuccessfulQualification(vote, response)
          case Failure(_)        => dispatch(NotifyError(I18n.t("errors.main")))
        }
        future
      }

      Vote.VoteProps(
        proposal = ownProps.wrapped.proposal,
        vote = vote,
        unvote = unvote,
        qualifyVote = qualify,
        removeVoteQualification = removeQualification,
        guideToVote = ownProps.wrapped.guideToVote,
        guideToQualification = ownProps.wrapped.guideToQualification,
        index = ownProps.wrapped.index,
        locationFacebook = ownProps.wrapped.locationFBTracking
      )
    }
}
