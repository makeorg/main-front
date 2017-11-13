package org.make.front.components.operation.sequence

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.{LoadConfiguration, NotifyError}
import org.make.front.components.AppState
import org.make.front.models.{
  Operation   => OperationModel,
  OperationId => OperationIdModel,
  Sequence    => SequenceModel,
  SequenceId  => SequenceIdModel
}
import org.make.services.proposal.{ContextRequest, ProposalService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SequenceOfTheOperationContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SequenceOfTheOperation.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => SequenceOfTheOperation.SequenceOfTheOperationProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val operationSlug = props.`match`.params("operationSlug")
        val operationsList: Seq[OperationModel] = state.operations.filter(_.slug == operationSlug)

        def nullProps: SequenceOfTheOperation.SequenceOfTheOperationProps = {
          SequenceOfTheOperation.SequenceOfTheOperationProps(
            OperationModel(OperationIdModel("fake"), "", "", "", "", 0, 0, "", None),
            SequenceModel(SequenceIdModel("fake"), "", ""),
            Future.successful(0)
          )
        }

        if (operationsList.isEmpty) {
          props.history.push("/")
          nullProps
        } else {

          val operation: OperationModel = operationsList.head

          if (operation.sequence.isEmpty) {
            props.history.push("/")
            nullProps
          } else {
            val sequence: SequenceModel = operation.sequence.getOrElse(SequenceModel(SequenceIdModel("fake"), "", ""))

            val numberOfProposals: Future[Int] = {

              val proposalsResponse =
                ProposalService.searchProposals(
                  context = Some(ContextRequest(operation = Some(operationsList.head.label))),
                  limit = Some(20)
                )

              proposalsResponse.recover {
                case e => dispatch(NotifyError(e.getMessage))
              }

              proposalsResponse.map(_.total)
            }

            dispatch(LoadConfiguration)

            SequenceOfTheOperation.SequenceOfTheOperationProps(
              operation = operation,
              sequence = sequence,
              numberOfProposals = numberOfProposals
            )
          }
        }
      }
    }
}
