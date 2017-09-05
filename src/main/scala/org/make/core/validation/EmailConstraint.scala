package org.make.core.validation

object EmailConstraint extends Constraint {
  private val emailRegex =
    """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r

  override def validate(value: Option[String], constraintMessages: Map[String, String] = Map()): Seq[ConstraintError] =
    value match {
      case Some(`emailRegex`()) => Seq()
      case _                    => Seq(ConstraintError(constraintMessages.get("invalid").getOrElse("Invalid email")))
    }
}
