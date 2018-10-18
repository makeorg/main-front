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
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalTile.PostedIn
import org.make.front.components.proposal.ProposalTileWithoutVoteAction.ProposalTileWithoutVoteActionProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{OperationList, Proposal => ProposalModel, User => UserModel}
import org.make.front.styles.vendors.FontAwesomeStyles
import scalacss.internal.ValueT

import scala.scalajs.js

object UserProfileProposals {

  final case class UserProfileProposalsProps(user: UserModel,
                                             proposals: js.Array[ProposalModel],
                                             operations: OperationList)

  lazy val reactClass: ReactClass =
    React
      .createClass[UserProfileProposalsProps, Unit](
        displayName = "UserProfileProposals",
        render = self => {
          <("UserProfileProposals")()(
            <.section(^.className := ProfileProposalListStyles.wrapper)(
              <.header(^.className := ProfileProposalListStyles.headerWrapper)(
                <.h2(^.className := ProfileProposalListStyles.title)(I18n.t("user-profile.proposal.title"))
              ),
              if (self.props.wrapped.proposals.isEmpty) {
                <.div(^.className := ProfileProposalListStyles.emptyWrapper)(
                  <.i(
                    ^.className := js.Array(FontAwesomeStyles.lightbulbTransparent, ProfileProposalListStyles.emptyIcon)
                  )(),
                  <.p(^.className := ProfileProposalListStyles.emptyDesc)(
                    self.props.wrapped.user.firstName,
                    unescape("&nbsp;"),
                    I18n.t("user-profile.proposal.empty")
                  )
                )
              } else {
                val counter = new Counter()
                <.ul()(self.props.wrapped.proposals.map {
                  proposal =>
                    <.li(^.className := ProfileProposalListStyles.proposalItem)(
                      <.ProposalTileWithoutVoteActionComponent(
                        ^.wrapped := ProposalTileWithoutVoteActionProps(
                          proposal = proposal,
                          index = counter.getAndIncrement(),
                          country = self.props.wrapped.user.country,
                          displayStatus = true,
                          maybePostedIn = PostedIn.fromProposal(
                            proposal = proposal,
                            operations = self.props.wrapped.operations.values
                              .flatMap(_.getOperationExpanded(proposal.tags, proposal.country))
                          )
                        )
                      )()
                    )
                }.toSeq)
              }
            ),
            <.style()(ProfileProposalListStyles.render[String])
          )
          <("UserProfileProposals")()(
            <.section(^.className := ProfileProposalListStyles.wrapper)(
              <.header(^.className := ProfileProposalListStyles.headerWrapper)(
                <.h2(^.className := ProfileProposalListStyles.title)(I18n.t("user-profile.proposal.title"))
              ),
              if (self.props.wrapped.proposals.isEmpty) {
                <.div(^.className := ProfileProposalListStyles.emptyWrapper)(
                  <.i(
                    ^.className := js.Array(
                      FontAwesomeStyles.lightbulbTransparent,
                      ProfileProposalListStyles.emptyIcon,
                      UserProfileProposalsStyles.emptyIcon
                    )
                  )(),
                  <.p(^.className := ProfileProposalListStyles.emptyDesc)(I18n.t("user-profile.proposal.empty"))
                )
              } else {
                val counter: Counter = new Counter()
                <.ul()(self.props.wrapped.proposals.map {
                  proposal =>
                    <.li(^.className := ProfileProposalListStyles.proposalItem)(
                      <.ProposalTileWithoutVoteActionComponent(
                        ^.wrapped := ProposalTileWithoutVoteActionProps(
                          proposal = proposal,
                          index = counter.getAndIncrement(),
                          country = self.props.wrapped.user.country,
                          displayStatus = true,
                          maybePostedIn = PostedIn.fromProposal(
                            proposal = proposal,
                            operations = self.props.wrapped.operations.values
                              .flatMap(_.getOperationExpanded(proposal.tags, proposal.country))
                          )
                        ),
                        ^.key := s"proposal_${proposal.id.value}"
                      )()
                    )
                }.toSeq)
              }
            ),
            <.style()(ProfileProposalListStyles.render[String], UserProfileProposalsStyles.render[String])
          )
        }
      )
}

object UserProfileProposalsStyles extends StyleSheet.Inline {

  import dsl._

  val specialYellow: ValueT[ValueT.Color] = rgb(255, 212, 0)

  val emptyIcon: StyleA =
    style(color(specialYellow))
}
