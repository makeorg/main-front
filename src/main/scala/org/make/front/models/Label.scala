package org.make.front.models

sealed trait Label  { val name: String }

object Label {
  case object Star extends Label { override val name = "star" }
  case object Hot extends Label { override val name = "hot" }
}

