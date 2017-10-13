package org.make.front.components.operation

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.RouterProps._

object RedirectToVFFSequence {

  lazy val reactClass: ReactClass =
    React
      .createClass[Unit, Unit](displayName = "RedirectToVFFSequence", componentWillMount = { self =>
        self.props.history.push("/consultation/comment-lutter-contre-les-violences-faites-aux-femmes")
      }, render = (self) => {
        <.div()()
      })
}
