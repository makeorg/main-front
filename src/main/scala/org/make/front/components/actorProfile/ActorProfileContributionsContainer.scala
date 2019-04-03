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
import org.make.front.components.actorProfile.ActorProfileContributions.ActorProfileContributionsProps
import org.make.front.components.{AppState, DataLoader}
import org.make.front.models.{Location, Organisation}
import org.make.services.organisation.OrganisationService
import org.make.services.proposal.ProposalResultWithUserVote

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ActorProfileContributionsContainer {

  final case class ActorProfileContributionsContainerProps(actor: Organisation, maybeLocation: Option[Location])

  lazy val reactClass: ReactClass = ReactRedux.connectAdvanced(selectorFactory)(DataLoader.reactClass)

  def selectorFactory
    : Dispatch => (AppState,
                   Props[ActorProfileContributionsContainerProps]) => DataLoaderProps[Seq[ProposalResultWithUserVote]] =
    (_: Dispatch) => { (state: AppState, props: Props[ActorProfileContributionsContainerProps]) =>
      def getProposalsVotedByActor: () => Future[Option[Seq[ProposalResultWithUserVote]]] = () => {
        OrganisationService
          .getOrganisationVotes(
            props.wrapped.actor.organisationId.value,
            Seq("agree", "disagree"),
            Seq.empty,
            props.wrapped.maybeLocation
          )
          .map { searchResult =>
            Some(searchResult.results)
          }
      }

      val shouldProposalsUpdate: Option[Seq[ProposalResultWithUserVote]] => Boolean = { proposalsVotedByActor =>
        proposalsVotedByActor.forall(_.exists(_.proposal.userId != props.wrapped.actor.organisationId))
      }

      DataLoaderProps[Seq[ProposalResultWithUserVote]](
        future = getProposalsVotedByActor,
        shouldComponentUpdate = shouldProposalsUpdate,
        componentDisplayedMeanwhileReactClass = WaitingForActorContributions.reactClass,
        componentReactClass = ActorProfileContributions.reactClass,
        componentProps = { proposals =>
          ActorProfileContributionsProps(actor = props.wrapped.actor, proposals = proposals, country = state.country)
        },
        onNotFound = () => {
          props.history.push("/404")
        }
      )

    }

}
