package org.make.core.validation

import scala.util.matching.Regex

class RegexConstraint(pattern: Regex) extends Constraint {
  override def validate(value: Option[String], constraintMessages: Map[String, String] = Map()): Seq[ConstraintError] = {

    if (value.getOrElse("").isEmpty) {
      Seq()
    } else {
      pattern.findFirstIn(value.get) match {
        case Some(_) => Seq()
        case None    => Seq(ConstraintError(constraintMessages.get("invalid").getOrElse("Value not match")))
      }
    }
  }
}
