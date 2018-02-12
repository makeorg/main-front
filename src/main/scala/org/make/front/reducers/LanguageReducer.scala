package org.make.front.reducers

import org.make.client.MakeApiClient
import org.make.front.actions.{SetCountry, SetLanguage}
import org.make.front.facades.I18n
import org.make.front.models.{BusinessConfiguration, CountryConfiguration}

object LanguageReducer {
  def reduce(maybeLanguage: Option[String],
             maybeBusinessConfiguration: Option[BusinessConfiguration],
             action: Any): Option[String] = {
    action match {
      case SetLanguage(newLanguage) =>
        updateServicesLanguage(newLanguage)
        Some(newLanguage)
      case SetCountry(country) =>
        maybeBusinessConfiguration.flatMap {
          _.supportedCountries.find(_.countryCode == country).map { countryConfiguration =>
            maybeLanguage match {
              case Some(lang) if countryConfiguration.supportedLanguages.contains(lang) => lang
              case _ =>
                updateServicesLanguage(countryConfiguration.defaultLanguage)
                countryConfiguration.defaultLanguage
            }
          }
        }.orElse(maybeLanguage)
      case _ => maybeLanguage
    }
  }

  private def updateServicesLanguage(language: String) = {
    I18n.setLocale(language)
    MakeApiClient.addHeaders(Map(MakeApiClient.languageHeader -> language))
  }
}
