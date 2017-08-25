package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action

final case class StorePendingProposal(content: String, registerProposal: () => Unit) extends Action
