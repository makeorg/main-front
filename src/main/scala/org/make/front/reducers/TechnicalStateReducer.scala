package org.make.front.reducers

import org.make.front.actions.{DismissLoginRequired, LoggedInAction, LoginRequired}
import org.make.front.models.TechnicalState

object TechnicalStateReducer {

  def reduce(maybeTechnicalState: Option[TechnicalState], action: Any): TechnicalState = {
    TechnicalState(
      notifications = NotificationReducer.reduce(maybeTechnicalState.map(_.notifications), action),
      showLoginModal = reduceShowLoginModal(maybeTechnicalState.map(_.showLoginModal), action)
    )
  }

  def reduceShowLoginModal(maybeShowLoginModal: Option[Boolean], action: Any): Boolean = {
    val initialShowLoginModal: Boolean = false
    val showLoginModal: Boolean = maybeShowLoginModal.getOrElse(initialShowLoginModal)
    action match {
      case LoginRequired        => true
      case LoggedInAction(_)    => false
      case DismissLoginRequired => false
      case _                    => showLoginModal
    }
  }

}
