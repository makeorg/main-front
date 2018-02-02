package org.make.front.reducers

import org.make.front.actions.SetLanguage

object LanguageReducer {
  def reduce(language: Option[String], action: Any): Option[String] = {
    action match {
      case SetLanguage(newLanguage) => Some(newLanguage)
      case _                        => language
    }
  }

}
