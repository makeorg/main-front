package org.make.front.models

import org.make.core.Counter

import scala.scalajs.js

@js.native
trait BusinessConfigurationResponse extends js.Object {
  val proposalMinLength: Int
  val proposalMaxLength: Int
  val themes: Seq[BusinessConfigurationTheme]
  val newVisitorCookieDefinition: String
}

case class BusinessConfiguration(proposalMinLength: Int,
                                 proposalMaxLength: Int,
                                 themes: Seq[BusinessConfigurationTheme],
                                 newVisitorCookieDefinition: String) {
  def themesForLocale(country: String, locale: String): Seq[Theme] = {
    val counter = new Counter()
    themes.filter(_.country == country).flatMap(_.toTheme(locale, counter))
  }
}

object BusinessConfiguration {
  def apply(businessConfigurationResponse: BusinessConfigurationResponse) : BusinessConfiguration = {
    BusinessConfiguration(
      proposalMinLength = businessConfigurationResponse.proposalMinLength,
      proposalMaxLength = businessConfigurationResponse.proposalMaxLength,
      themes = businessConfigurationResponse.themes,
      newVisitorCookieDefinition = businessConfigurationResponse.newVisitorCookieDefinition)
  }
}

@js.native
trait BusinessConfigurationTheme extends js.Object {
  val themeId: ThemeId
  val translations: Seq[ThemeTranslation]
  val actionsCount: Int
  val proposalsCount: Int
  val country: String
  val color: String
  val gradient: Option[GradientColor]
  val tags: Seq[Tag]

  def title(language: String): Option[String] = translations.find(_.language == language).map(_.title)
  def slug(language: String): Option[String] = translations.find(_.language == language).map(_.slug)
  def toTheme(locale: String, counter: Counter): Option[Theme] = {
    for {
      title <- title(locale)
      slug  <- slug(locale)
    } yield {
      Theme(
        id = themeId,
        slug = slug,
        title = title,
        actionsCount = actionsCount,
        proposalsCount = proposalsCount,
        color = color,
        gradient = gradient,
        tags = tags,
        order = counter.getAndIncrement()
      )
    }
  }
}

object BusinessConfigurationTheme {
  def apply(themeId: ThemeId,
            translations: Seq[ThemeTranslation],
            actionsCount: Int,
            proposalsCount: Int,
            country: String,
            color: String,
            gradient: js.UndefOr[GradientColor],
            tags: Seq[Tag] = Seq.empty): BusinessConfigurationTheme = {
    js.Dynamic.literal(
      themeId = themeId,
      translations = translations,
      actionsCount = actionsCount,
      proposalsCount = proposalsCount,
      country = country,
      color = color,
      gradient = gradient,
      tags = tags
    ).asInstanceOf[BusinessConfigurationTheme ]
  }
}

@js.native
trait ThemeTranslation extends js.Object {
  val slug: String
  val title: String
  val language: String
}

object ThemeTranslation {
  def apply(slug: String, title: String, language: String): ThemeTranslation = {
    js.Dynamic.literal(slug = slug, title = title, language = language).asInstanceOf[ThemeTranslation]
  }
}