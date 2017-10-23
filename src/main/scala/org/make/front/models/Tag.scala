package org.make.front.models

import org.make.core.StringValue
import org.make.services.proposal.TagResponse

final case class Tag(tagId: TagId, label: String)

object Tag {
  def apply(tagResponse: TagResponse): Tag = {
    Tag(tagId = TagId(tagResponse.tagId), label = tagResponse.label)
  }
}

final case class TagId(value: String) extends StringValue

