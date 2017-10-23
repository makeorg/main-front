package org.make.front.models

import org.make.core.StringValue

final case class Tag(tagId: TagId, label: String)

object Tag {
}

final case class TagId(value: String) extends StringValue

object TagId {
}
