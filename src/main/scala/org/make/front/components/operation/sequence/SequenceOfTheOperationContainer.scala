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

              val proposalsResponse =
                ProposalService.searchProposals(
                  context = Some(ContextRequest(operation = Some(maybeOperation.head.label))),
                  limit = Some(20)
                )

              proposalsResponse.recover {
                case e => dispatch(NotifyError(e.getMessage))
              }

              proposalsResponse.map(_.total)
            }

            dispatch(LoadConfiguration)

            SequenceOfTheOperation.SequenceOfTheOperationProps(
              maybeFirstProposalSlug = firstProposalSlug,
              isConnected = state.connectedUser.isDefined,
              operation = operation,
              sequence = sequence,
              numberOfProposals = numberOfProposals
            )
          }
        }.getOrElse {
          props.history.push("/")
          SequenceOfTheOperation.SequenceOfTheOperationProps(
            maybeFirstProposalSlug = None,
            isConnected = false,
            OperationModel(OperationIdModel("fake"), "", "", "", "", 0, 0, "", None),
            SequenceModel(SequenceIdModel("fake"), "", ""),
            Future.successful(0)
          )
        }
      }
    }
}
