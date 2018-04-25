package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action
import org.make.front.models.Location

final case class VoteAction(location: Location) extends Action
