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
import org.make.front.components.userProfile.UserProfileProposals.UserProfileProposalsProps
import org.make.front.models.{Proposal, User}
import org.make.services.operation.OperationService
import org.make.services.user.UserService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object UserProfileProposalsContainer {

  final case class UserProposalsContainerProps(user: User, userId: String)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(UserProfileProposals.reactClass)

  def selectorFactory
    : Dispatch => (AppState, Props[UserProposalsContainerProps]) => UserProfileProposals.UserProfileProposalsProps =
    (_: Dispatch) => { (_: AppState, props: Props[UserProposalsContainerProps]) =>
      def getProposals: Future[js.Array[Proposal]] = {

        UserService
          .getUserProposals(props.wrapped.userId)
          .flatMap { proposalsSearchResult =>
            OperationService
              .listOperations()
              .map { operations =>
                proposalsSearchResult.results.filter(
                  proposal =>
                    proposal.themeId.isDefined ||
                      proposal.operationId.exists(opId => operations.map(_.operationId.value).contains(opId.value))
                )
              }
              .recover {
                case _ => proposalsSearchResult.results
              }
          }
      }

      UserProfileProposalsProps(user = props.wrapped.user, getProposals = getProposals)
    }

}
