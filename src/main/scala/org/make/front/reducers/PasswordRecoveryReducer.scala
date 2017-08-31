package org.make.front.reducers

import org.make.front.actions.{ClosePasswordRecoveryModalAction, OpenPasswordRecoveryModalAction}

object PasswordRecoveryReducer {

  def reduce(maybePasswordRecoveryModalIsOpen: Option[Boolean], action: Any): Boolean = {
    val initialPasswordRecoveryModalIsOpen: Boolean = false
    val passwordRecoveryModalIsOpen: Boolean = maybePasswordRecoveryModalIsOpen.getOrElse(initialPasswordRecoveryModalIsOpen)

    action match {
      case OpenPasswordRecoveryModalAction => true
      case ClosePasswordRecoveryModalAction => false
      case _ => passwordRecoveryModalIsOpen
    }
  }
}
