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
import org.make.front.components.AppState
import org.make.front.components.userProfile.UserLikeItProposals.UserLikeItProposalsProps
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global

object UserLikeItProposalsContainer {

  final case class UserLikeItProposalsContainerProps(userId: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(UserLikeItProposals.reactClass)

  def selectorFactory
    : Dispatch => (AppState, Props[UserLikeItProposalsContainerProps]) => UserLikeItProposals.UserLikeItProposalsProps =
    (_: Dispatch) => { (_: AppState, props: Props[UserLikeItProposalsContainerProps]) =>
      def getLikeItProposals = UserService.getUserLikeItProposals(props.wrapped.userId).map(_.results)

      UserLikeItProposalsProps(getLikeItProposals = getLikeItProposals)
    }

}
