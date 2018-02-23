package org.make.front.reducers

import org.make.front.actions.{SetCountryLanguage, SetLanguage}
import org.make.front.facades.I18n

object BaitReducer {
  def reduce(bait: Option[String], action: Any): Option[String] = {
    action match {
      case SetLanguage(_)           => Some(I18n.t("common.bait"))
      case SetCountryLanguage(_, _) => Some(I18n.t("common.bait"))
      case _                        => bait
    }
  }

}
