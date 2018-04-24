package org.make.core.validation

import scala.scalajs.js

object PasswordConstraint extends Constraint {

  val min = 8

  def isValidMin(value: Option[String]): Boolean = {
    value match {
      case Some(password) if password.length >= min => true
      case _                                        => false
    }
  }

  override def validate(value: Option[String],
                        constraintMessages: Map[String, String] = Map()): js.Array[ConstraintError] = {

    if (value.getOrElse("").isEmpty || isValidMin(value)) {
      js.Array()
    } else {
      js.Array(
        ConstraintError(
          constraintMessages
            .getOrElse("minMessage", "This value is too short. It should have %{min} characters or more")
        )
      )
    }
  }
}
