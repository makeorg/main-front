package org.make.core.validation

object EmailConstraint extends Constraint {
  private val emailRegex =
    """^.+\@\S+\.\S+$""".r
  override def validate(value: Option[String],
                        constraintMessages: Map[String, String] = Map()): Seq[ConstraintError] = {

    if (value.getOrElse("").isEmpty) {
      Seq(ConstraintError(constraintMessages.getOrElse("invalid", "Invalid email")))
    } else {
      emailRegex
        .findFirstMatchIn(value.get)
        .map(_ => Seq.empty)
        .getOrElse(Seq(ConstraintError(constraintMessages.getOrElse("invalid", "Invalid email"))))
    }
  }

}
