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

package org.make.front.actions
import io.github.shogowada.scalajs.reactjs.redux.Action
import org.make.front.models.{OperationExpanded, OperationList}

final case class SetOperations(operations: OperationList) extends Action
final case class SetCurrentOperationSlug(slug: Option[String]) extends Action
final case class SetCurrentOperation(operation: Option[OperationExpanded]) extends Action
case object LoadOperations extends Action
