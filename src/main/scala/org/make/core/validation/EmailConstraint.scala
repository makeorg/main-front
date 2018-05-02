package org.make.core.validation

import scala.scalajs.js

object EmailConstraint extends Constraint {
  private val emailRegex =
    """^.+\@\S+\.\S+$""".r
  override def validate(value: Option[String],
                        constraintMessages: Map[String, String] = Map()): js.Array[ConstraintError] = {

    if (value.getOrElse("").isEmpty) {
      js.Array(ConstraintError(constraintMessages.getOrElse("invalid", "Invalid email")))
    } else {
      emailRegex
        .findFirstMatchIn(value.get)
        .map(_ => js.Array[ConstraintError]())
        .getOrElse(js.Array(ConstraintError(constraintMessages.getOrElse("invalid", "Invalid email"))))
    }
  }

}
