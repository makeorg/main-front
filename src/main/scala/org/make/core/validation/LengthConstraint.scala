package org.make.core.validation

import scalajs.js

class LengthConstraint(min: Option[Int] = None, max: Option[Int] = None, exact: Option[Int] = None) extends Constraint {
  override def validate(value: Option[String],
                        constraintMessages: Map[String, String] = Map()): Seq[ConstraintError] = {

    val errors = js.Array[ConstraintError]()

    val valueLength = value.getOrElse("").length

    if (max.isDefined && valueLength > max.get) {
      errors.concat(
        js.Array(
          ConstraintError(
            constraintMessages
              .getOrElse("maxMessage", "This value is too long. It should have %{max} characters or less")
          )
        )
      )
    }
    if (min.isDefined && valueLength < min.get) {
      errors.concat(
        js.Array(
          ConstraintError(
            constraintMessages
              .getOrElse("minMessage", "This value is too short. It should have %{min} characters or more")
          )
        )
      )
    }
    if (exact.isDefined && valueLength != exact.get) {
      errors.concat(
        js.Array(
          ConstraintError(
            constraintMessages.getOrElse("exactMessage", "This value should have exactly %{exact} characters")
          )
        )
      )
    }

    errors
  }
}
