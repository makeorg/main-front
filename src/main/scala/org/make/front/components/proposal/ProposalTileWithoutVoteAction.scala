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

package org.make.front.components.proposal

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components.{RichVirtualDOMElements, _}
import org.make.front.components.proposal.ProposalActorVoted.ProposalActorVotedProps
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.vote.DisplayVotesData.DisplayVotesDataProps
import org.make.front.models.{Qualification, Proposal => ProposalModel, Vote => VoteModel}
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}

import scala.scalajs.js

object ProposalTileWithoutVoteAction {

  final case class ProposalTileWithoutVoteActionProps(proposal: ProposalModel, index: Int)

  final case class ProposalTileWithoutVoteActionState(votes: js.Array[VoteModel],
                                                      voteKeyMap: Map[String, String],
                                                      voteCountMap: Map[String, Int],
                                                      qualification: js.Array[Qualification])

  val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ProposalTileWithoutVoteActionProps, ProposalTileWithoutVoteActionState](
          displayName = "ProposalTileWithoutVoteAction",
          getInitialState = { self =>
            ProposalTileWithoutVoteActionState(
              votes = self.props.wrapped.proposal.votes.map(vote => vote),
              voteKeyMap = self.props.wrapped.proposal.votes.map(vote   => vote.key -> vote.key).toMap,
              voteCountMap = self.props.wrapped.proposal.votes.map(vote => vote.key -> vote.count).toMap,
              qualification = self.props.wrapped.proposal.votes.flatMap(vote => vote.qualifications)
            )
          },
          render = (self) => {

            val intro: ReactElement =
              <.div(^.className := ProposalTileStyles.proposalInfosWrapper)(
                <.ProposalInfosComponent(^.wrapped := ProposalInfosProps(proposal = self.props.wrapped.proposal))()
              )

            val proposalLink: String =
              s"/${self.props.wrapped.proposal.country}/proposal/${self.props.wrapped.proposal.slug}"

            <.article(^.className := ProposalTileStyles.wrapper)(
              <.div(^.className := js.Array(TableLayoutStyles.fullHeightWrapper, ProposalTileStyles.innerWrapper))(
                <.div(^.className := TableLayoutStyles.row)(
                  <.div(^.className := TableLayoutStyles.cell)(
                    intro,
                    <.div(^.className := ProposalTileStyles.contentWrapper)(
                      <.h3(
                        ^.className := js
                          .Array(TextStyles.mediumText, TextStyles.boldText, ProposalTileStyles.proposalLinkOnTitle)
                      )(if (self.props.wrapped.proposal.isAccepted) {
                        <.Link(^.to := proposalLink, ^.className := ProposalTileStyles.proposalLinkOnTitle)(
                          self.props.wrapped.proposal.content
                        )
                      } else {
                        self.props.wrapped.proposal.content
                      }),
                      <.DisplayVotesDataComponent(
                        ^.wrapped := DisplayVotesDataProps(
                          vote = self.state.votes,
                          voteKeyMap = self.state.voteKeyMap,
                          voteCountMap = self.state.voteCountMap,
                          index = self.props.wrapped.index
                        )
                      )(),
                      <.ProposalActorVotedComponent(
                        ^.wrapped := ProposalActorVotedProps(organisations = self.props.wrapped.proposal.organisations)
                      )()
                    )
                  )
                )
              ),
              <.style()(ProposalTileStyles.render[String])
            )
          }
        )
    )
}
