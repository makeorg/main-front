package org.make.core.validation

import scala.scalajs.js

class ChoiceConstraint(choices: js.Array[String]) extends Constraint {
  override def validate(value: Option[String],
                        constraintMessages: Map[String, String] = Map()): js.Array[ConstraintError] = {
    if (!value.getOrElse("").isEmpty && !choices.contains(value.getOrElse(""))) {
      js.Array(
        ConstraintError(constraintMessages.get("invalid").getOrElse("The value you selected is not a valid choice"))
      )
    } else {
      js.Array()
    }
  }
}
