package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action
import org.make.front.models.PoliticalAction

import scala.scalajs.js

final case class SetPoliticalAction(politicalActions: js.Array[PoliticalAction]) extends Action
case object LoadPoliticalAction extends Action
