package org.make.core.validation

import scala.scalajs.js

case class ConstraintError(message: String)

trait Constraint {
  def validate(value: Option[String], messages: Map[String, String] = Map()): js.Array[ConstraintError]
  def &(other: Constraint): Constraint = {
    val self = this
    (value: Option[String], messages: Map[String, String]) =>
      {
        self.validate(value, messages) ++ other.validate(value, messages)
      }
  }
}
