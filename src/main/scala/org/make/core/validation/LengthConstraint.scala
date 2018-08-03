/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.core.validation

import scalajs.js

class LengthConstraint(min: Option[Int] = None, max: Option[Int] = None, exact: Option[Int] = None) extends Constraint {
  override def validate(value: Option[String],
                        constraintMessages: Map[String, String] = Map()): js.Array[ConstraintError] = {

    var errors = js.Array[ConstraintError]()

    val valueLength = value.getOrElse("").length

    max.foreach { maxValue =>
      if (valueLength > maxValue) {
        errors = errors.concat(
          js.Array(
            ConstraintError(
              constraintMessages
                .getOrElse("maxMessage", "This value is too long. It should have %{max} characters or less")
            )
          )
        )
      }
    }
    min.foreach { minValue =>
      if (valueLength < minValue) {
        errors = errors.concat(
          js.Array(
            ConstraintError(
              constraintMessages
                .getOrElse("minMessage", "This value is too short. It should have %{min} characters or more")
            )
          )
        )
      }
    }
    exact.foreach { exactValue =>
      if (valueLength != exactValue) {
        errors = errors.concat(
          js.Array(
            ConstraintError(
              constraintMessages.getOrElse("exactMessage", "This value should have exactly %{exact} characters")
            )
          )
        )
      }
    }

    errors
  }
}
