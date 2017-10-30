package org.make.front.components.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.models.{
  Operation => OperationModel,
  Theme     => ThemeModel,
  Proposal  => ProposalModel,
  Sequence  => SequenceModel
}
import org.make.services.proposal.{ContextRequest, ProposalService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SequenceContainer {

  final case class SequenceContainerProps(sequence: SequenceModel,
                                          maybeThemeColor: Option[String],
                                          maybeTheme: Option[ThemeModel] = None,
                                          maybeOperation: Option[OperationModel] = None,
                                          intro: ReactClass,
                                          conclusion: ReactClass,
                                          promptingToPropose: ReactClass)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Sequence.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[SequenceContainerProps]) => Sequence.SequenceProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[SequenceContainerProps]) =>
      {
        val proposals: Future[Seq[ProposalModel]] = {

          val vffOperation: OperationModel = state.operations.filter(_.operationId.value == "vff").head
          val proposalsResponse =
            ProposalService.searchProposals(
              context = Some(ContextRequest(operation = Some(vffOperation.label))),
              limit = Some(20)
            )

          proposalsResponse.recover {
            case e => dispatch(NotifyError(e.getMessage))
          }

          proposalsResponse.map(_.results)
        }

        Sequence.SequenceProps(
          sequence = props.wrapped.sequence,
          maybeThemeColor = props.wrapped.maybeThemeColor,
          maybeTheme = props.wrapped.maybeTheme,
          maybeOperation = props.wrapped.maybeOperation,
          proposals = proposals,
          intro = props.wrapped.intro,
          conclusion = props.wrapped.conclusion,
          promptingToPropose = props.wrapped.promptingToPropose
        )
      }
    }
}
