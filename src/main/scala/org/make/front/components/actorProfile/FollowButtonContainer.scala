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

package org.make.front.components.actorProfile

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.actions.{ReloadUserAction, TriggerSignUpAction}
import org.make.front.components.AppState
import org.make.front.models.Location.UnknownLocation
import org.make.front.models.UserId
import org.make.services.user.UserService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FollowButtonContainer {

  case class FollowButtonContainerProps(userId: UserId)

  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(FollowButton.reactClass))

  def selectorFactory: Dispatch => (AppState, Props[FollowButtonContainerProps]) => FollowButton.FollowButtonProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[FollowButtonContainerProps]) =>
      val isFollowedByUserValue: Boolean =
        state.connectedUser.exists(_.followedUsers.exists(_.value == props.wrapped.userId.value))

      def triggerFollow = () => {
        state.connectedUser match {
          case Some(user) if !isFollowedByUserValue =>
            UserService.followUser(props.wrapped.userId.value).map(_ => dispatch(ReloadUserAction))
          case Some(user) => UserService.unfollowUser(props.wrapped.userId.value).map(_ => dispatch(ReloadUserAction))
          case None =>
            dispatch(TriggerSignUpAction(UnknownLocation(props.location.pathname)))
            Future.successful({})
        }
      }

      FollowButton.FollowButtonProps(isFollowedByUser = isFollowedByUserValue, triggerFollowToggle = triggerFollow)
    }

}
