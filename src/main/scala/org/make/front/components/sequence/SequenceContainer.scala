package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.components.sequence.Sequence.ExtraSlide
import org.make.front.models.{Operation => OperationModel, Proposal => ProposalModel, Sequence => SequenceModel}
import org.make.services.proposal.{ContextRequest, ProposalService, SearchResult}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SequenceContainer {

  final case class SequenceContainerProps(sequence: SequenceModel,
                                          progressBarColor: Option[String],
                                          extraSlides: Seq[ExtraSlide])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Sequence.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[SequenceContainerProps]) => Sequence.SequenceProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[SequenceContainerProps]) =>
      {
        val proposals: () => Future[Seq[ProposalModel]] = () => {

          val vffOperation: OperationModel = state.operations.filter(_.operationId.value == "vff").head
          val proposalsResponse: Future[SearchResult] =
            ProposalService.searchProposals(
              context = Some(ContextRequest(operation = Some(vffOperation.label))),
              limit = Some(20)
            )

          proposalsResponse.recover {
            case e => dispatch(NotifyError(e.getMessage))
          }

          proposalsResponse.map(_.results).map { proposals =>
            val (voted, notVoted) = proposals.span(_.votes.exists(_.hasVoted))
            voted ++ notVoted
          }
        }

        val shouldReload: Boolean = state.connectedUser.nonEmpty

        Sequence.SequenceProps(
          sequence = props.wrapped.sequence,
          progressBarColor = props.wrapped.progressBarColor,
          proposals = proposals,
          extraSlides = props.wrapped.extraSlides,
          shouldReload = shouldReload
        )
      }
    }
}
