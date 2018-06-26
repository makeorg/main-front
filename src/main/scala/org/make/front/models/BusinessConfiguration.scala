/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.models

import org.make.core.Counter
import org.make.services.proposal.TagResponse

import scala.scalajs.js

@js.native
trait BusinessConfigurationResponse extends js.Object {
  val proposalMinLength: Int
  val proposalMaxLength: Int
  val themes: js.Array[ThemeResponse]
  val supportedCountries: js.Array[CountryConfigurationResponse]
}

case class BusinessConfiguration(proposalMinLength: Int,
                                 proposalMaxLength: Int,
                                 supportedCountries: js.Array[CountryConfiguration],
                                 themes: js.Array[Theme],
                                 nVotesTriggerConnexion: Int = 5,
                                 maxTriggerConnexion: Int = 101010) {
  def themesForLocale(country: String, language: String): js.Array[TranslatedTheme] = {
    val counter = new Counter()
    themes.filter(_.country == country).flatMap(_.toTranslatedTheme(language, counter))
  }

  def coreIsAvailableForCountry(country: String): Boolean = {
    supportedCountries.find(_.countryCode == country) match {
      case None                => false
      case Some(countryConfig) => countryConfig.coreIsAvailable
    }
  }
}

object BusinessConfiguration {
  def apply(businessConfigurationResponse: BusinessConfigurationResponse): BusinessConfiguration = {
    val seqThemes: js.Array[Theme] = businessConfigurationResponse.themes.map(Theme.apply)

    BusinessConfiguration(
      proposalMinLength = businessConfigurationResponse.proposalMinLength,
      proposalMaxLength = businessConfigurationResponse.proposalMaxLength,
      themes = seqThemes,
      supportedCountries = businessConfigurationResponse.supportedCountries.map(CountryConfiguration.apply)
    )
  }
}

case class CountryConfiguration(countryCode: String,
                                defaultLanguage: String,
                                supportedLanguages: js.Array[String],
                                coreIsAvailable: Boolean,
                                flagUrl: String = "") {
  def languageIsSuppported(language: String): Boolean = {
    defaultLanguage.contains(language)
  }
}
object CountryConfiguration {
  def apply(countryConfigurationResponse: CountryConfigurationResponse): CountryConfiguration = {
    CountryConfiguration(
      countryCode = countryConfigurationResponse.countryCode,
      defaultLanguage = countryConfigurationResponse.defaultLanguage,
      supportedLanguages = countryConfigurationResponse.supportedLanguages,
      coreIsAvailable = countryConfigurationResponse.coreIsAvailable
    )
  }
}
@js.native
trait ThemeResponse extends js.Object {
  val themeId: String
  val translations: js.Array[ThemeTranslationResponse]
  val proposalsCount: Int
  /*val votesCount: Int*/
  val actionsCount: Int
  val country: String
  val color: String
  val gradient: js.UndefOr[GradientColor]
  val tags: js.Array[TagResponse]
}

@js.native
trait ThemeTranslationResponse extends js.Object {
  val slug: String
  val title: String
  val language: String
}

@js.native
trait CountryConfigurationResponse extends js.Object {
  val countryCode: String
  val defaultLanguage: String
  val coreIsAvailable: Boolean
  val supportedLanguages: js.Array[String]
}
