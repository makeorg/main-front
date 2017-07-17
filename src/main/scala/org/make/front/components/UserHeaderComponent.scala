package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}

object UserHeaderComponent {
  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div()(
        "User header component"
      )
  )

}
