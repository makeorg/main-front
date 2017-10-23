package org.make.front.models

import org.make.core.Counter

import scala.collection.mutable
import scala.scalajs.js

@js.native
trait BusinessConfigurationResponse extends js.Object {
  val proposalMinLength: Int
  val proposalMaxLength: Int
  val themes: js.Array[BusinessConfigurationTheme]
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
    val seqThemes: mutable.Seq[BusinessConfigurationTheme] = businessConfigurationResponse.themes

    BusinessConfiguration(
      proposalMinLength = businessConfigurationResponse.proposalMinLength,
      proposalMaxLength = businessConfigurationResponse.proposalMaxLength,
      themes = seqThemes,
      newVisitorCookieDefinition = businessConfigurationResponse.newVisitorCookieDefinition
    )
  }
}

@js.native
trait BusinessConfigurationTheme extends js.Object {
  val themeId: String
  val translations: js.Array[ThemeTranslation]
  val actionsCount: Int
  val proposalsCount: Int
  val country: String
  val color: String
  val gradient: js.UndefOr[GradientColor]
  val tags: js.Array[Tag]

  def title(language: String): Option[String] = translations.find(_.language == language).map(_.title)
  def slug(language: String): Option[String] = translations.find(_.language == language).map(_.slug)
  def toTheme(locale: String, counter: Counter): Option[Theme] = {
    for {
      title <- title(locale)
      slug  <- slug(locale)
    } yield {
      Theme(
        id = ThemeId(themeId),
        slug = slug,
        title = title,
        actionsCount = actionsCount,
        proposalsCount = proposalsCount,
        country = locale,
        color = color,
        gradient = gradient.toOption,
        tags = tags.asInstanceOf[mutable.Seq[Tag]],
        order = counter.getAndIncrement()
      )
    }
  }

}


@js.native
trait ThemeTranslation extends js.Object {
  val slug: String
  val title: String
  val language: String
}