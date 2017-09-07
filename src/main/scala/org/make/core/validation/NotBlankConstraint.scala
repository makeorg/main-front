package org.make.core.validation

object NotBlankConstraint extends Constraint {
  override def validate(value: Option[String], constraintMessages: Map[String, String] = Map()): Seq[ConstraintError] = {
    if (value.getOrElse("").nonEmpty) {
      Seq.empty
    } else {
      Seq(ConstraintError(constraintMessages.get("notBlank").getOrElse("Field is required")))
    }
  }
}
