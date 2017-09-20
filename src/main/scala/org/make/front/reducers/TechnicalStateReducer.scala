package org.make.front.reducers

import org.make.front.actions.LoginRequired
import org.make.front.components.TechnicalState

object TechnicalStateReducer {

  def reduce(maybeTechnicalState: Option[TechnicalState], action: Any): TechnicalState = {
    TechnicalState(
      notifications = NotificationReducer.reduce(maybeTechnicalState.map(_.notifications), action),
      useProposalLoginView = reduceProposalLoginView(maybeTechnicalState.map(_.useProposalLoginView), action),
      pendingProposalAction = ProposalReducer.reduce(maybeTechnicalState.flatMap(_.pendingProposalAction), action)
    )
  }

  def reduceProposalLoginView(maybeProposalLoginView: Option[Boolean], action: Any): Boolean = {
    val initialProposalLoginView: Boolean = false
    val proposalLoginView: Boolean = maybeProposalLoginView.getOrElse(initialProposalLoginView)
    action match {
      case action: LoginRequired => action.isProposalFlow
      case _                     => proposalLoginView
    }
  }

}
