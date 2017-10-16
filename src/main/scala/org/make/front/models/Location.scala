package org.make.front.models

sealed trait Location { val shortName: String }

object Location {
  case object Sequence extends Location { override val shortName = "sequence" }
  case object Homepage extends Location { override val shortName = "homepage" }
  case object ThemePage extends Location { override val shortName = "theme page" }
}
