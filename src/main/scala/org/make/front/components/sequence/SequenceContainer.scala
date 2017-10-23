package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.models.{Operation, Proposal, Sequence => SequenceModel}
import org.make.services.proposal.{ContextRequest, ProposalService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SequenceContainer {

  final case class SequenceContainerProps(sequence: SequenceModel,
                                          maybeThemeColor: Option[String],
                                          intro: ReactClass,
                                          conclusion: ReactClass)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Sequence.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[SequenceContainerProps]) => Sequence.SequenceProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[SequenceContainerProps]) =>
      {
        val proposals: Future[Seq[Proposal]] = {

          val vffOperation: Operation = state.operations.filter(_.operationId.value == "vff").head
          val proposalsResponse =
            ProposalService.searchProposals(context = Some(ContextRequest(operation = Some(vffOperation.label))))

          proposalsResponse.recover {
            case e => dispatch(NotifyError(e.getMessage))
          }

          proposalsResponse.map(_.results)
        }

        Sequence.SequenceProps(
          sequence = props.wrapped.sequence,
          maybeThemeColor = props.wrapped.maybeThemeColor,
          proposals = proposals,
          intro = props.wrapped.intro,
          conclusion = props.wrapped.conclusion
        )
      }
    }
}
