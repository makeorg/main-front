package org.make.core.validation

import scala.collection.mutable.ListBuffer

class LengthConstraint(min: Option[Int] = None, max: Option[Int] = None, exact: Option[Int] = None) extends Constraint {
  override def validate(value: Option[String], constraintMessages: Map[String, String] = Map()): Seq[ConstraintError] = {

    var errors = ListBuffer.empty[ConstraintError]

    val valueLength = value.getOrElse("").length

    if (!max.isEmpty && valueLength > max.get) {
      errors += ConstraintError(
        constraintMessages
          .get("maxMessage")
          .getOrElse("This value is too long. It should have %{max} characters or less")
      )
    }
    if (!min.isEmpty && valueLength < min.get) {
      errors += ConstraintError(
        constraintMessages
          .get("minMessage")
          .getOrElse("This value is too short. It should have %{min} characters or more")
      )
    }
    if (!exact.isEmpty && valueLength != exact.get) {
      errors += ConstraintError(
        constraintMessages.get("exactMessage").getOrElse("This value should have exactly %{exact} characters")
      )
    }

    errors
  }
}
