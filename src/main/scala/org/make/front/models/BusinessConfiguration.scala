package org.make.front.models

import io.circe.Decoder

case class BusinessConfiguration(proposalMinLength: Int,
                                 proposalMaxLength: Int,
                                 themes: Seq[BusinessConfigurationTheme],
                                 newVisitorCookieDefinition: String) {
  def themesForLocale(locale: String): Seq[Theme] = {
    themes.flatMap(_.toTheme(locale))
  }
}

object BusinessConfiguration {
  implicit val decoder: Decoder[BusinessConfiguration] =
    Decoder.forProduct4("proposalMinLength", "proposalMaxLength", "themes", "newVisitorCookieDefinition")(
      BusinessConfiguration.apply
    )
}

final case class BusinessConfigurationTheme(themeId: ThemeId,
                                            translations: Seq[ThemeTranslation],
                                            actionsCount: Int,
                                            proposalsCount: Int,
                                            country: String,
                                            color: String,
                                            gradient: Option[GradientColor] = None,
                                            tags: Seq[Tag] = Seq.empty) {

  def toTheme(locale: String): Option[Theme] = {
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
        tags = tags
      )
    }
  }

  def title(language: String): Option[String] = translations.find(_.language == language).map(_.title)
  def slug(language: String): Option[String] = translations.find(_.language == language).map(_.slug)

}

object BusinessConfigurationTheme {
  implicit val decoder: Decoder[BusinessConfigurationTheme] =
    Decoder.forProduct8(
      "themeId",
      "translations",
      "actionsCount",
      "proposalsCount",
      "country",
      "color",
      "gradient",
      "tags"
    )(BusinessConfigurationTheme.apply)
}

final case class ThemeTranslation(slug: String, title: String, language: String)

object ThemeTranslation {
  implicit val decoder: Decoder[ThemeTranslation] =
    Decoder.forProduct3("slug", "title", "language")(ThemeTranslation.apply)
}
