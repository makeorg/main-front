package org.make.front.actions

import io.github.shogowada.scalajs.reactjs.redux.Action
import org.make.front.models.BusinessConfiguration

final case class SetConfiguration(businessConfiguration: BusinessConfiguration) extends Action
case object LoadConfiguration extends Action
