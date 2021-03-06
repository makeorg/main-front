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

import io.github.shogowada.scalajs.reactjs.redux.Store
import org.make.core.Counter
import org.make.front.components.AppState
import org.make.front.helpers.UndefToOption.undefToOption

import scala.scalajs.js

@js.native
trait GradientColor extends js.Object {
  val from: String
  val to: String
}

object GradientColor {
  def apply(from: String, to: String): GradientColor = {
    js.Dynamic.literal(from = from, to = to).asInstanceOf[GradientColor]
  }
}

final case class TranslatedTheme(id: ThemeId,
                                 slug: String,
                                 title: String,
                                 proposalsCount: Int,
                                 votesCount: Int,
                                 actionsCount: Int,
                                 country: String,
                                 order: Int,
                                 color: String,
                                 gradient: Option[GradientColor] = None,
                                 tags: js.Array[Tag] = js.Array())

object TranslatedTheme {
  val empty = TranslatedTheme(ThemeId("fake"), "", "", 0, 0, 0, "", 0, "", None, js.Array())
}

final case class Theme(id: ThemeId,
                       translations: js.Array[ThemeTranslation],
                       proposalsCount: Int,
                       votesCount: Int,
                       actionsCount: Int,
                       country: String,
                       color: String,
                       gradient: Option[GradientColor] = None,
                       tags: js.Array[Tag] = js.Array()) {

  def title(language: String): Option[String] = translations.find(_.language == language).map(_.title)
  def slug(language: String): Option[String] = translations.find(_.language == language).map(_.slug)
  def toTranslatedTheme(language: String, counter: Counter): Option[TranslatedTheme] = {
    for {
      title <- title(language)
      slug  <- slug(language)
    } yield {
      TranslatedTheme(
        id = id,
        slug = slug,
        title = title,
        proposalsCount = proposalsCount,
        votesCount = votesCount,
        actionsCount = actionsCount,
        country = country,
        color = color,
        gradient = gradient,
        tags = tags,
        order = counter.getAndIncrement()
      )
    }
  }
}

final case class ThemeTranslation(slug: String, title: String, language: String)

object ThemeTranslation {
  def apply(themeTranslationResponse: ThemeTranslationResponse): ThemeTranslation = {
    ThemeTranslation(
      slug = themeTranslationResponse.slug,
      title = themeTranslationResponse.title,
      language = themeTranslationResponse.language
    )
  }
}

@js.native
trait ThemeId extends js.Object {
  val value: String
}

object ThemeId {
  def apply(value: String): ThemeId = {
    js.Dynamic.literal(value = value).asInstanceOf[ThemeId]
  }
}

object Theme {
  def apply(themeResponse: ThemeResponse): Theme = {
    val seqTranslations: js.Array[ThemeTranslation] = themeResponse.translations.map(ThemeTranslation.apply)
    val seqTags: js.Array[Tag] = themeResponse.tags.filter(tag => !tag.label.contains(":")).map(Tag.apply)

    Theme(
      id = ThemeId(themeResponse.themeId),
      translations = seqTranslations,
      proposalsCount = themeResponse.proposalsCount,
      votesCount = 0 /*themeResponse.votesCount*/,
      actionsCount = themeResponse.actionsCount,
      country = themeResponse.country,
      color = themeResponse.color,
      gradient = undefToOption(themeResponse.gradient),
      tags = seqTags
    )
  }

  def getThemeById(id: String, store: Store[AppState]): TranslatedTheme = {
    store.getState.themes.find(theme => theme.id.value == id).get
  }
}
