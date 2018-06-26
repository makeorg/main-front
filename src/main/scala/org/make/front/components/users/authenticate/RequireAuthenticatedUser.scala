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

package org.make.front.components.users.authenticate

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.make.front.components.Components._
import org.make.front.components.authenticate.LoginOrRegister.LoginOrRegisterProps
import org.make.front.models.OperationId
import org.make.services.tracking.TrackingService.TrackingContext

object RequireAuthenticatedUser {

  case class RequireAuthenticatedUserProps(operationId: Option[OperationId],
                                           trackingContext: TrackingContext,
                                           trackingParameters: Map[String, String],
                                           intro: ReactElement,
                                           registerView: String,
                                           defaultView: String = "register",
                                           onceConnected: () => Unit,
                                           isConnected: Boolean)
  type RequireAuthenticatedUserState = Unit

  val reactClass: ReactClass =
    React.createClass[RequireAuthenticatedUserProps, RequireAuthenticatedUserState](
      displayName = "RequireAuthenticatedUser",
      componentWillMount = { self =>
        if (self.props.wrapped.isConnected) {
          self.props.wrapped.onceConnected()
        }
      },
      componentWillReceiveProps = { (self, nextProps) =>
        if (!self.props.wrapped.isConnected && nextProps.wrapped.isConnected) {
          nextProps.wrapped.onceConnected()
        }
      },
      render = { self =>
        val props = self.props.wrapped

        if (props.isConnected) {
          <.div()(<.SpinnerComponent.empty)
        } else {
          <.div()(
            self.props.wrapped.intro,
            <.LoginOrRegisterComponent(
              // There is no need to use callback here, since the component will be reloaded with different props
              // once the user is connected. if we map it here, the callback will be called twice
              ^.wrapped := LoginOrRegisterProps(
                trackingContext = self.props.wrapped.trackingContext,
                trackingParameters = self.props.wrapped.trackingParameters,
                operationId = self.props.wrapped.operationId,
                registerView = props.registerView,
                displayView = props.defaultView,
                onSuccessfulLogin = () => {}
              )
            )()
          )
        }
      }
    )
}
