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

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.core.Counter
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalTile.{PostedIn, ProposalTileProps}
import org.make.front.components.userProfile.ProfileProposalListStyles
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Operation, OperationExpanded, Proposal, Organisation => OrganisationModel}
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingLocation
import scalacss.internal.ValueT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.{Failure, Success}

object ActorProfileProposals {

  final case class ActorProfileProposalsProps(actor: OrganisationModel,
                                              actorProposals: js.Array[Proposal],
                                              getOperations: () => Future[js.Array[Operation]])

  final case class ActorProfileProposalsState(operations: js.Array[Operation])

  lazy val reactClass: ReactClass =
    React
      .createClass[ActorProfileProposalsProps, ActorProfileProposalsState](
        displayName = "ActorProfileProposals",
        getInitialState = _ => ActorProfileProposalsState(js.Array()),
        componentDidMount = self => {
          self.props.wrapped.getOperations().onComplete {
            case Success(operationList) =>
              self.setState(self.state.copy(operations = operationList))
            case Failure(_) =>
          }
        },
        render = self => {
          <("ActorProfileProposals")()(
            <.section(^.className := ProfileProposalListStyles.wrapper)(
              <.header(^.className := ProfileProposalListStyles.headerWrapper)(
                <.h2(^.className := ProfileProposalListStyles.title)(
                  I18n
                    .t(
                      "actor-profile.proposal.title",
                      replacements =
                        Replacements(("actor-name", self.props.wrapped.actor.organisationName.getOrElse("")))
                    )
                )
              ),
              if (self.props.wrapped.actorProposals.isEmpty) {
                <.div(^.className := ProfileProposalListStyles.emptyWrapper)(
                  <.i(
                    ^.className := js
                      .Array(
                        FontAwesomeStyles.lightbulbTransparent,
                        ProfileProposalListStyles.emptyIcon,
                        ActorProfileProposalsStyles.emptyIcon
                      )
                  )(),
                  <.p(^.className := ProfileProposalListStyles.emptyDesc)(
                    self.props.wrapped.actor.organisationName,
                    unescape("&nbsp;"),
                    I18n.t("actor-profile.proposal.empty")
                  )
                )
              } else {
                val counter = new Counter()
                <.ul()(self.props.wrapped.actorProposals.map {
                  actorProposal =>
                    <.li(^.className := ProfileProposalListStyles.proposalItem)(
                      <.ProposalTileComponent(
                        ^.wrapped :=
                          ProposalTileProps(
                            proposal = actorProposal,
                            index = counter.getAndIncrement(),
                            maybeTheme = None,
                            maybeOperation = None,
                            maybeSequenceId = None,
                            maybeLocation = None,
                            trackingLocation = TrackingLocation.actorProfile,
                            country = actorProposal.country,
                            maybePostedIn = PostedIn.fromProposal(
                              proposal = actorProposal,
                              operations = self.state.operations.flatMap(
                                ope =>
                                  OperationExpanded.getOperationExpandedFromOperation(
                                    Some(ope),
                                    actorProposal.tags,
                                    actorProposal.country
                                )
                              )
                            )
                          )
                      )()
                    )
                }.toSeq)
              }
            ),
            <.style()(ProfileProposalListStyles.render[String], ActorProfileProposalsStyles.render[String])
          )
        }
      )
}

object ActorProfileProposalsStyles extends StyleSheet.Inline {

  import dsl._

  val specialYellow: ValueT[ValueT.Color] = rgb(255, 212, 0)

  val emptyIcon: StyleA =
    style(color(specialYellow))

}
