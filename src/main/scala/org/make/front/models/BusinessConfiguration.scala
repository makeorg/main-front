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
                                 supportedCountries: Seq[CountryConfiguration],
                                 themes: Seq[Theme]) {
  def themesForLocale(country: String, language: String): Seq[TranslatedTheme] = {
    val counter = new Counter()
    themes.filter(_.country == country).flatMap(_.toTranslatedTheme(language, counter))
  }
}

object BusinessConfiguration {
  def apply(businessConfigurationResponse: BusinessConfigurationResponse): BusinessConfiguration = {
    val seqThemes: Seq[Theme] = businessConfigurationResponse.themes.map(Theme.apply)

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
                                supportedLanguages: Seq[String],
                                coreIsAvailable: Boolean,
                                flagUrl: String = "") {
  def languageIsSuppported(language: String) = {
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
  val supportedLanguages: js.Array[String]
  val coreIsAvailable: Boolean
}
