package org.make.core.validation

object PasswordConstraint extends Constraint {

  val min = 8

  def isValidMin(value: Option[String]): Boolean = {
    value match {
      case Some(password) if (password.length >= min) => true
      case _                                          => false
    }
  }

  override def validate(value: Option[String], constraintMessages: Map[String, String] = Map()): Seq[ConstraintError] = {

    if (value.getOrElse("").isEmpty || isValidMin(value)) {
      Seq.empty
    } else {
      Seq(
        ConstraintError(
          constraintMessages
            .get("minMessage")
            .getOrElse("This value is too short. It should have %{min} characters or more")
        )
      )
    }
  }
}