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
