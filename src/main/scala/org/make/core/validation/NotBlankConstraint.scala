package org.make.core.validation

import scala.scalajs.js

object NotBlankConstraint extends Constraint {
  override def validate(value: Option[String],
                        constraintMessages: Map[String, String] = Map()): js.Array[ConstraintError] = {
    if (value.getOrElse("").nonEmpty) {
      js.Array()
    } else {
      js.Array(ConstraintError(constraintMessages.getOrElse("notBlank", "Field is required")))
    }
  }
}
