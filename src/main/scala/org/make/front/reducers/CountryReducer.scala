package org.make.front.reducers

import org.make.client.MakeApiClient
import org.make.front.actions.SetCountry

object CountryReducer {
  def reduce(country: Option[String], action: Any): Option[String] = {
    action match {
      case SetCountry(newCountry) =>
        MakeApiClient.addHeaders(Map(MakeApiClient.countryHeader -> newCountry))
        Some(newCountry)
      case _ => country
    }
  }
}
