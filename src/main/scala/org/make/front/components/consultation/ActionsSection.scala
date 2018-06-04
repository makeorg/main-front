package org.make.front.components.consultation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

object ActionsSection {

  case class ActionsSectionState()
  case class ActionsSectionProps()

  lazy val reactClass: ReactClass =
    React
      .createClass[ActionsSectionProps, ActionsSectionState](displayName = "ConsultationSection", getInitialState = {
        _ =>
          ActionsSectionState()
      }, render = { self =>
        <.div()("toDo: actions")
      })
}
