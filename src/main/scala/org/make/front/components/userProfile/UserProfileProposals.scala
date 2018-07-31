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
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.make.front.components.Components._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.components.proposal.ProposalTileWithoutVote.ProposalTileWithoutVoteProps
import org.make.front.models.Proposal
import org.make.services.tracking.TrackingLocation

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.Success

object UserProfileProposals {

  final case class UserProfileProposalsProps(getProposals: Future[js.Array[Proposal]])
  final case class UserProfileProposalsState(proposals: js.Array[Proposal])

  lazy val reactClass: ReactClass = React.createClass[UserProfileProposalsProps, UserProfileProposalsState](
    displayName = "UserProposals",
    getInitialState = _ => UserProfileProposalsState(js.Array()),
    componentDidMount = { self =>
      self.props.wrapped.getProposals.onComplete {
        case Success(proposals) => self.setState(_.copy(proposals = proposals))
        case _                  =>
      }
    },
    render = self => {
      <.div()(self.state.proposals.map { proposal =>
        <.ProposalsTileWithoutVoteComponent(^.wrapped := ProposalTileWithoutVoteProps(proposal = proposal))()
      })
    }
  )
}
