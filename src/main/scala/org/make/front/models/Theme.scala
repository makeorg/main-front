package org.make.front.models

import io.circe.{Decoder, Encoder, Json}
import org.make.core.StringValue

final case class GradientColor(from: String, to: String)

final case class Theme(id: ThemeId,
                       slug: String,
                       title: String,
                       actionsCount: Int,
                       proposalsCount: Int,
                       color: String,
                       gradient: Option[GradientColor] = None,
                       tags: Seq[Tag] = Seq.empty)

final case class ThemeId(value: String) extends StringValue

object ThemeId {
  implicit lazy val themeIdEncoder: Encoder[ThemeId] = (a: ThemeId) => Json.fromString(a.value)
  implicit lazy val themeIdDecoder: Decoder[ThemeId] = Decoder.decodeString.map(ThemeId(_))
}
