package org.make.front.components.proposal.vote

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.models.{Proposal => ProposalModel}
import org.make.services.proposal.ProposalResponses.VoteResponse
import org.make.services.proposal.ProposalService

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Failure, Success}

object VoteContainer {

  final case class VoteContainerProps(proposal: ProposalModel)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Vote.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[VoteContainerProps]) => Vote.VoteProps =
    (dispatch: Dispatch) => { (appState: AppState, ownProps: Props[VoteContainerProps]) =>
      def vote(key: String): Future[VoteResponse] = {

        val future = ProposalService.vote(proposalId = ownProps.wrapped.proposal.id, key)

        future.onComplete {
          case Success(_) => // let child handle new results
          case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
        }

        future
      }

      def unvote(key: String): Future[VoteResponse] = {
        val future = ProposalService.unvote(proposalId = ownProps.wrapped.proposal.id, key)

        future.onComplete {
          case Success(_) => // let child handle new results
          case Failure(_) => dispatch(NotifyError(I18n.t("errors.main")))
        }

        future
      }

      Vote.VoteProps(proposal = ownProps.wrapped.proposal, vote = vote, unvote = unvote)
    }
}
