package org.make.front.reducers

import org.make.front.actions.PasswordRecoveryAction

object PasswordRecoveryReducer {

  def reduce(maybePasswordRecoveryModalIsOpen: Option[Boolean], action: Any): Boolean = {
    val passwordRecoveryModalIsOpen: Boolean = maybePasswordRecoveryModalIsOpen.getOrElse(false)
    action match {
      case action: PasswordRecoveryAction => action.openModal
      case _ => passwordRecoveryModalIsOpen
    }
  }
}
