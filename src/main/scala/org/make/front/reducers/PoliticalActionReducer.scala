package org.make.front.reducers

import org.make.front.actions.SetPoliticalAction
import org.make.front.models.{PoliticalAction => PoliticalActionModel}

object PoliticalActionReducer {

  def reduce(maybePoliticalAction: Option[Seq[PoliticalActionModel]], action: Any): Seq[PoliticalActionModel] = {
    val politicalActions = maybePoliticalAction.getOrElse(Seq.empty)
    action match {
      case action: SetPoliticalAction => action.politicalActions
      case _                          => politicalActions
    }
  }

}
