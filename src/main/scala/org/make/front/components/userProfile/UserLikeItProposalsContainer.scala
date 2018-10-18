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
import org.make.front.components.userProfile.UserLikeItProposals.UserLikeItProposalsProps
import org.make.front.components.{AppState, DataLoader}
import org.make.front.models.{Proposal, User => UserModel}
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object UserLikeItProposalsContainer {

  final case class UserLikeItProposalsContainerProps(user: UserModel)
  lazy val reactClass: ReactClass = WithRouter(ReactRedux.connectAdvanced(selectorFactory)(DataLoader.reactClass))

  def selectorFactory
    : Dispatch => (AppState, Props[UserLikeItProposalsContainerProps]) => DataLoaderProps[js.Array[Proposal]] =
    (_: Dispatch) => { (state: AppState, props: Props[UserLikeItProposalsContainerProps]) =>
      def getLikeItProposals: () => Future[Option[js.Array[Proposal]]] = () => {

        UserService
          .getUserLikeItProposals(props.wrapped.user.userId.value)
          .map { proposalsSearchResult =>
            Option(
              proposalsSearchResult.results.filter(
                proposal =>
                  proposal.themeId.isDefined ||
                    proposal.operationId.exists(opId => state.operations.containsOperationId(opId))
              )
            )
          }
      }

      val shouldUserLikeitProposalsUpdate: Option[js.Array[Proposal]] => Boolean = { userProposals =>
        userProposals.forall(_.exists(_.userId.value == props.wrapped.user.userId.value))
      }

      DataLoaderProps[js.Array[Proposal]](
        future = getLikeItProposals,
        shouldComponentUpdate = shouldUserLikeitProposalsUpdate,
        componentDisplayedMeanwhileReactClass = WaitingForUserLikeItProposals.reactClass,
        componentReactClass = UserLikeItProposals.reactClass,
        componentProps = { proposals =>
          UserLikeItProposalsProps(proposals = proposals, operations = state.operations)
        },
        onNotFound = () => {
          props.history.push("/404")
        }
      )

    }

}
