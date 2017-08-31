package org.make.front.reducers

import org.make.front.actions.{DismissLoginRequired, LoggedInAction, LoginRequired}
import org.make.front.models.TechnicalState

object TechnicalStateReducer {

  def reduce(maybeTechnicalState: Option[TechnicalState], action: Any): TechnicalState = {
    TechnicalState(
      notifications = NotificationReducer.reduce(maybeTechnicalState.map(_.notifications), action),
      showLoginModal = reduceShowLoginModal(maybeTechnicalState.map(_.showLoginModal), action),
      useProposalLoginView = reduceProposalLoginView(maybeTechnicalState.map(_.useProposalLoginView), action),
      pendingProposalAction = ProposalReducer.reduce(maybeTechnicalState.flatMap(_.pendingProposalAction), action),
      passwordRecoveryModalIsOpen = PasswordRecoveryReducer.reduce(maybeTechnicalState.map(_.passwordRecoveryModalIsOpen), action)
    )
  }

  def reduceShowLoginModal(maybeShowLoginModal: Option[Boolean], action: Any): Boolean = {
    val initialShowLoginModal: Boolean = false
    val showLoginModal: Boolean = maybeShowLoginModal.getOrElse(initialShowLoginModal)
    action match {
      case LoginRequired(_)     => true
      case LoggedInAction(_)    => false
      case DismissLoginRequired => false
      case _                    => showLoginModal
    }
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
