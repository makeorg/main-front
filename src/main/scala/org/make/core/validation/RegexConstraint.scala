package org.make.core.validation

import scala.util.matching.Regex

class RegexConstraint(pattern: Regex) extends Constraint {
  override def validate(value: Option[String],
                        constraintMessages: Map[String, String] = Map()): Seq[ConstraintError] = {

    if (value.getOrElse("").isEmpty) {
      Seq.empty
    } else {
      pattern.findFirstIn(value.get) match {
        case Some(_) => Seq.empty
        case None    => Seq(ConstraintError(constraintMessages.getOrElse("invalid", "Value not match")))
      }
    }
  }
}
