package org.make.front.models

sealed trait Source { val name: String }

object Source {
  case object Core extends Source { override val name = "core" }
}

