package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.actions.NotifyError
import org.make.front.components.AppState
import org.make.front.models.{Proposal => ProposalModel}
import org.make.services.proposal.ProposalService

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object ProposalContainer {

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(Proposal.reactClass)

  def selectorFactory: (Dispatch) => (AppState, Props[Unit]) => Proposal.ProposalProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[Unit]) =>
      {

        val proposal: Future[ProposalModel] = {

          val proposalSlug = props.`match`.params("proposalSlug")

          val proposalsResponse =
            ProposalService.searchProposals(slug = Some(proposalSlug), limit = Some(1), sort = Seq.empty, skip = None)

          proposalsResponse.recover {
            case e => dispatch(NotifyError(e.getMessage))
          }

          proposalsResponse.map(_.results.head)
        }

        Proposal.ProposalProps(proposal = proposal)
      }
    }
}
