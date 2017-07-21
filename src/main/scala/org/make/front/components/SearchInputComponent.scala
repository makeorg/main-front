package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}

object SearchInputComponent {
  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div()(
        "Search input component"
      )
  )

}
