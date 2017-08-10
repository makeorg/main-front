package org.make.front.reducers

import org.make.front.actions.SetPoliticalAction
import org.make.front.models.PoliticalAction

object PoliticalActionReducer {

  def reduce(maybePoliticalAction: Option[Seq[PoliticalAction]], action: Any): Seq[PoliticalAction] = {
    val politicalActions = maybePoliticalAction.getOrElse(Seq.empty)
    action match {
      case action: SetPoliticalAction => action.politicalActions
      case _ => politicalActions
    }
  }

}
