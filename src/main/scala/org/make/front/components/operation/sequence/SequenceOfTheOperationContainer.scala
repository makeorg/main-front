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
import org.make.services.sequence.SequenceService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SequenceOfTheOperationContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(SequenceOfTheOperation.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => SequenceOfTheOperation.SequenceOfTheOperationProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {
        val operationSlug = props.`match`.params("operationSlug")
        val search = org.scalajs.dom.window.location.search
        val firstProposalSlug = (if (search.startsWith("?")) { search.substring(1) } else { search })
          .split("&")
          .map(_.split("="))
          .find {
            case Array("firstProposal", _) => true
            case _                         => false
          }
          .flatMap {
            case Array(_, value) => Some(value)
            case _               => None
          }

        val maybeOperation: Option[OperationModel] = state.operations.find(_.slug == operationSlug)

        maybeOperation.flatMap { operation =>
          operation.sequence.map { sequence =>
            val numberOfProposals: Future[Int] = {

              val includes = Seq.empty
              val sequenceResponse =
                SequenceService.startSequenceBySlug(sequence.slug, includes)

              sequenceResponse.recover {
                case e => dispatch(NotifyError(e.getMessage))
              }

              sequenceResponse.map(_.proposals.size)
            }

            dispatch(LoadConfiguration)

            SequenceOfTheOperation.SequenceOfTheOperationProps(
              maybeFirstProposalSlug = firstProposalSlug,
              isConnected = state.connectedUser.isDefined,
              operation = operation,
              sequence = Future.successful(sequence), // toDo: sequence should be dynamic
              numberOfProposals = numberOfProposals
            )
          }
        }.getOrElse {
          props.history.push("/")
          SequenceOfTheOperation.SequenceOfTheOperationProps(
            maybeFirstProposalSlug = None,
            isConnected = false,
            OperationModel(OperationIdModel("fake"), "", "", "", "", 0, 0, "", None),
            Future.successful(SequenceModel(SequenceIdModel("fake"), "", "")),
            Future.successful(0)
          )
        }
      }
    }
}
