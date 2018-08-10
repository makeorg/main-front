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

package org.make.front.components.userProfile

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.Components._
import org.make.front.models.{User => UserModel}
import org.make.front.components.userProfile.editingUserProfile.DeleteAccount.DeleteAccountProps

object UserProfileSettings {

  final case class UserProfileSettingsProps(user: UserModel)
  final case class UserProfileSettingsState()

  val reactClass: ReactClass =
    React
      .createClass[UserProfileSettingsProps, UserProfileSettingsState](
        displayName = "UserProfileSettings",
        getInitialState = { self =>
          UserProfileSettingsState()
        },
        render = self => {
          <("UserProfileSettings")()(
            <.div()(
              <.UserProfileFormContainerComponent()(),
              <.UserProfileResetPasswordContainerComponent()(),
              <.UserProfileOptinNewsletterContainerComponent()(),
              <.UserProfileDeleteAccountComponent(^.wrapped := DeleteAccountProps(user = self.props.wrapped.user))()
            )
          )
        }
      )
}
