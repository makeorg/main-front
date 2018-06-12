package org.make.front.components.operation.intro

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.models.{OperationExpanded => OperationModel}

object CultureOperationIntro {

  case class CultureOperationIntroProps(operation: OperationModel)

  lazy val reactClass: ReactClass =
    React
      .createClass[CultureOperationIntroProps, Unit](displayName = "CultureOperationIntro", render = _ => {
        <.div()()
      })
}
