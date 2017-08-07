package org.make.front.components.presentationals

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

object HomeComponent {
  lazy val reactClass: ReactClass =
    React.createClass[Unit, Unit](render = (_) => <.div()(<.HomeHeaderComponent.empty))
}