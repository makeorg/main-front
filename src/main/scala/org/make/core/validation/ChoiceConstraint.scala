package org.make.core.validation

class ChoiceConstraint(choices: Seq[String]) extends Constraint {
  override def validate(value: Option[String], constraintMessages: Map[String, String] = Map()): Seq[ConstraintError] = {
    if (!value.getOrElse("").isEmpty && !choices.contains(value.getOrElse(""))) {
      Seq(ConstraintError(constraintMessages.get("invalid").getOrElse("The value you selected is not a valid choice")))
    } else {
      Seq()
    }
  }
}
