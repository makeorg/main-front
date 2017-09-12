package org.make.front.components.ActivateAccount

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

object ActivateAccountComponent {

  case class ActivateAccountProps(handleValidateAccount: (Self[ActivateAccountProps, Unit]) => Unit)

  lazy val reactClass: ReactClass =
    React.createClass[ActivateAccountProps, Unit](displayName = getClass.getSimpleName, componentWillMount = { self =>
      self.props.wrapped.handleValidateAccount(self)
    }, render = self => {
      <.div()()
    })
}
