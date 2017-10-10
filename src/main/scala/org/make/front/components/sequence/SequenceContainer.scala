package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.models.{Proposal => ProposalModel, ProposalId => ProposalIdModel, Sequence => SequenceModel}
import org.make.services.proposal.ProposalService

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Failure, Success}

object SequenceContainer {

  final case class SequenceContainerProps(sequence: SequenceModel)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Sequence.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[SequenceContainerProps]) => Sequence.SequenceProps =
    (dispatch: Dispatch) => { (_: AppState, props: Props[SequenceContainerProps]) =>
      {

        def proposals: Future[Seq[ProposalModel]] = {

          val results = Future.traverse(props.wrapped.sequence.proposals) { proposalId: ProposalIdModel =>
            ProposalService.getProposalById(proposalId = proposalId)
          }

          results.onComplete {
            case Success(_) =>
            case Failure(e) => dispatch(NotifyError(e.getMessage))
          }

          results

        }

        Sequence.SequenceProps(sequence = props.wrapped.sequence, proposals = proposals)
      }
    }
}
