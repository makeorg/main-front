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

sealed trait NotifyAction {
  def message: String
  def autoDismiss: Option[Int]
}

object NotifyAction {
  val defaultAutoDismiss: Int = 50000000
}

case class NotifyInfo(override val message: String,
                      override val autoDismiss: Option[Int] = Some(NotifyAction.defaultAutoDismiss))
    extends Action
    with NotifyAction
case class NotifyAlert(override val message: String,
                       override val autoDismiss: Option[Int] = Some(NotifyAction.defaultAutoDismiss))
    extends Action
    with NotifyAction
case class NotifyError(override val message: String,
                       override val autoDismiss: Option[Int] = Some(NotifyAction.defaultAutoDismiss))
    extends Action
    with NotifyAction
case class NotifySuccess(override val message: String,
                         override val autoDismiss: Option[Int] = Some(NotifyAction.defaultAutoDismiss))
    extends Action
    with NotifyAction

case class DismissNotification(identifier: String) extends Action
