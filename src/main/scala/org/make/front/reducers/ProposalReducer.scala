package org.make.front.reducers

import org.make.front.actions.{DismissLoginRequired, LoginRequired, StorePendingProposal}

object ProposalReducer {

  def reduce(maybePendingProposalAction: Option[StorePendingProposal], action: Any): Option[StorePendingProposal] = {
    action match {
      case action: StorePendingProposal => Some(action)
      case DismissLoginRequired         => None
      case action: LoginRequired        => if (!action.isProposalFlow) None else maybePendingProposalAction
      case _                            => maybePendingProposalAction
    }
  }
}
