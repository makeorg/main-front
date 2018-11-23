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
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import org.make.front.actions.ReloadUserAction
import org.make.front.components.AppState
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.UserId
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global

object FollowButtonContainer {

  case class FollowButtonContainerProps(userId: UserId, actorName: Option[String])
  case class ShowModal(value: Boolean)

  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(FollowButton.reactClass))

  def selectorFactory: Dispatch => (AppState, Props[FollowButtonContainerProps]) => FollowButton.FollowButtonProps =
    (dispatch: Dispatch) => { (state: AppState, props: Props[FollowButtonContainerProps]) =>
      val isFollowedByUserValue: Boolean =
        state.connectedUser.exists(_.followedUsers.exists(_.value == props.wrapped.userId.value))

      def triggerFollow: () => ShowModal = () => {
        state.connectedUser match {
          case Some(_) if !isFollowedByUserValue =>
            UserService.followUser(props.wrapped.userId.value).map(_ => dispatch(ReloadUserAction))
            ShowModal(false)
          case Some(_) =>
            UserService.unfollowUser(props.wrapped.userId.value).map(_ => dispatch(ReloadUserAction))
            ShowModal(false)
          case None =>
            ShowModal(true)
        }
      }

      FollowButton.FollowButtonProps(
        isFollowedByUser = isFollowedByUserValue,
        triggerFollowToggle = triggerFollow,
        actorName = props.wrapped.actorName.getOrElse(unescape(I18n.t("actor-profile.contributions.title")))
      )
    }

}
