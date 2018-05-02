package org.make.front.reducers

import org.make.front.actions.SetPoliticalAction
import org.make.front.models.{PoliticalAction => PoliticalActionModel}

import scala.scalajs.js

object PoliticalActionReducer {

  def reduce(maybePoliticalAction: Option[js.Array[PoliticalActionModel]],
             action: Any): js.Array[PoliticalActionModel] = {
    val politicalActions = maybePoliticalAction.getOrElse(js.Array())
    action match {
      case action: SetPoliticalAction => action.politicalActions
      case _                          => politicalActions
    }
  }

}
