package org.make.front.components

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._

object HeaderComponent {
  def apply() = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit](
    render = (_) =>
      <.div()(
        "The header of the page"
      )
  )

}
