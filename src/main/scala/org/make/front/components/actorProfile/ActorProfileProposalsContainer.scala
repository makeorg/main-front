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
import io.github.shogowada.scalajs.reactjs.router.RouterProps._
import org.make.front.components.DataLoader.DataLoaderProps
import org.make.front.components.actorProfile.ActorProfileProposals.ActorProfileProposalsProps
import org.make.front.components.{AppState, DataLoader}
import org.make.front.models.{Organisation, Proposal}
import org.make.services.organisation.OrganisationService
import org.make.services.proposal.SearchResult

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

object ActorProfileProposalsContainer {

  final case class ActorProposalsContainerProps(actor: Organisation)

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(DataLoader.reactClass)

  def selectorFactory
    : Dispatch => (AppState, Props[ActorProposalsContainerProps]) => DataLoaderProps[js.Array[Proposal]] =
    (_: Dispatch) => { (_: AppState, props: Props[ActorProposalsContainerProps]) =>
      def getActorProposals: () => Future[Option[js.Array[Proposal]]] = () => {
        OrganisationService.getOrganisationProposals(props.wrapped.actor.organisationId.value).map {
          case SearchResult(total, results, _) if total > 0 => Some(results)
          case _                                            => None
        }
      }

      val shouldActorProposalsUpdate: Option[js.Array[Proposal]] => Boolean = { actorProposals =>
        actorProposals.forall(_.exists(_.userId != props.wrapped.actor.organisationId))
      }

      DataLoaderProps[js.Array[Proposal]](
        future = getActorProposals,
        shouldComponentUpdate = shouldActorProposalsUpdate,
        componentDisplayedMeanwhileReactClass = WaitingForActorProposals.reactClass,
        componentReactClass = ActorProfileProposals.reactClass,
        componentProps = { proposals =>
          ActorProfileProposalsProps(actor = props.wrapped.actor, actorProposals = proposals)
        },
        onNotFound = () => {
          props.history.push("/404")
        }
      )

    }

}
