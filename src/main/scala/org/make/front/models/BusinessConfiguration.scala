package org.make.front.models

import org.make.core.Counter
import org.make.services.proposal.TagResponse

import scala.scalajs.js

@js.native
trait BusinessConfigurationResponse extends js.Object {
  val proposalMinLength: Int
  val proposalMaxLength: Int
  val themes: js.Array[ThemeResponse]
  val newVisitorCookieDefinition: String
}

case class BusinessConfiguration(proposalMinLength: Int,
                                 proposalMaxLength: Int,
                                 themes: Seq[Theme],
                                 newVisitorCookieDefinition: String) {
  def themesForLocale(country: String, locale: String): Seq[TranslatedTheme] = {
    val counter = new Counter()
    themes.filter(_.country == country).flatMap(_.toTranslatedTheme(locale, counter))
  }
}

object BusinessConfiguration {
  def apply(businessConfigurationResponse: BusinessConfigurationResponse): BusinessConfiguration = {
    val seqThemes: Seq[Theme] = businessConfigurationResponse.themes.map(Theme.apply)

    BusinessConfiguration(
      proposalMinLength = businessConfigurationResponse.proposalMinLength,
      proposalMaxLength = businessConfigurationResponse.proposalMaxLength,
      themes = seqThemes,
      newVisitorCookieDefinition = businessConfigurationResponse.newVisitorCookieDefinition
    )
  }
}

@js.native
trait ThemeResponse extends js.Object {
  val themeId: String
  val translations: js.Array[ThemeTranslationResponse]
  val actionsCount: Int
  val proposalsCount: Int
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
