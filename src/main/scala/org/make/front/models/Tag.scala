package org.make.front.models

import io.circe.{Decoder, Encoder, Json}
import org.make.core.StringValue

final case class Tag(tagId: TagId, label: String)

object Tag {
  implicit val decoder: Decoder[Tag] = Decoder.forProduct2("tagId", "label")(Tag.apply)
}

final case class TagId(value: String) extends StringValue

object TagId {
  implicit lazy val tagIdEncoder: Encoder[TagId] =
    (a: TagId) => Json.fromString(a.value)
  implicit lazy val tagIdDecoder: Decoder[TagId] =
    Decoder.decodeString.map(TagId(_))
}
