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

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.components.DataLoader.DataLoaderProps
import org.make.front.components.userProfile.UserProfileFollowed.UserProfileFollowedProps
import org.make.front.components.{AppState, DataLoader}
import org.make.front.models.{UserId, User => UserModel}
import org.make.services.user.UserService

import scala.concurrent.Future

object UserProfileFollowedContainer {

  final case class UserProfileFollowedContainerProps(followedUserId: UserId)
  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(DataLoader.reactClass))

  def selectorFactory: Dispatch => (AppState, Props[UserProfileFollowedContainerProps]) => DataLoaderProps[UserModel] =
    (_: Dispatch) => { (_: AppState, props: Props[UserProfileFollowedContainerProps]) =>
      def getFollowed: () => Future[Option[UserModel]] = () => {
        UserService.getUserById(props.wrapped.followedUserId.value)
      }
      def shouldUserFollowedUpdate: Option[UserModel] => Boolean = { _.isEmpty }

      DataLoaderProps[UserModel](
        future = getFollowed,
        shouldComponentUpdate = shouldUserFollowedUpdate,
        componentDisplayedMeanwhileReactClass = WaitingForUserProfileFollowed.reactClass,
        componentReactClass = UserProfileFollowed.reactClass,
        componentProps = { userModel =>
          UserProfileFollowedProps(user = userModel)
        },
        onNotFound = () => {
          props.history.push("/404")
        }
      )

    }

}
