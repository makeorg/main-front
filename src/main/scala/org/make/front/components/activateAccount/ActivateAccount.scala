/*
 *
 * Make.org Main Front
 * Copyright (C) 2018 Make.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.make.front.components.activateAccount

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

object ActivateAccount {

  case class ActivateAccountProps(handleValidateAccount: (Self[ActivateAccountProps, Unit]) => Unit)

  lazy val reactClass: ReactClass =
    React.createClass[ActivateAccountProps, Unit](displayName = "ActivateAccount", componentWillMount = { self =>
      self.props.wrapped.handleValidateAccount(self)
    }, render = self => {
      <.div.empty
    })
}
