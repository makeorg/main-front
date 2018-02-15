package org.make.front.reducers

import org.make.front.actions.{SetCountry, SetCountryLanguage}

object CountryReducer {
  def reduce(country: Option[String], action: Any): Option[String] = {
    action match {
      case SetCountry(newCountry)            => Some(newCountry)
      case SetCountryLanguage(newCountry, _) => Some(newCountry)
      case _                                 => country
    }
  }
}
