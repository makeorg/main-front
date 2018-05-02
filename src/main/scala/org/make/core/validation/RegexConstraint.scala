package org.make.core.validation

import scala.scalajs.js
import scala.util.matching.Regex

class RegexConstraint(pattern: Regex) extends Constraint {
  override def validate(value: Option[String],
                        constraintMessages: Map[String, String] = Map()): js.Array[ConstraintError] = {

    if (value.getOrElse("").isEmpty) {
      js.Array()
    } else {
      pattern.findFirstIn(value.get) match {
        case Some(_) => js.Array()
        case None    => js.Array(ConstraintError(constraintMessages.getOrElse("invalid", "Value not match")))
      }
    }
  }
}
