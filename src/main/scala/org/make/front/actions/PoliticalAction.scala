package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action
import org.make.front.models.PoliticalAction

final case class SetPoliticalAction(politicalActions: Seq[PoliticalAction]) extends Action
case object LoadPoliticalAction extends Action