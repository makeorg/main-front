package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action

final case class LoginRequired(isProposalFlow: Boolean = false) extends Action
case object DismissLoginRequired extends Action
