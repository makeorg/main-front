package org.make.front.reducers

import org.make.front.actions.{SetCountryLanguage, SetLanguage}

object LanguageReducer {
  def reduce(maybeLanguage: Option[String], action: Any): Option[String] = {
    action match {
      case SetLanguage(newLanguage)           => Some(newLanguage)
      case SetCountryLanguage(_, newLanguage) => Some(newLanguage)
      case _                                  => maybeLanguage
    }
  }

}
