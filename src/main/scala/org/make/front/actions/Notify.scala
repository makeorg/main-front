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

case class DismissNotification(identifier: Int) extends Action
