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
import org.make.front.components.proposal.ProposalInfos.ProposalInfosProps
import org.make.front.components.proposal.ShareOwnProposal.ShareOwnProposalProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  TranslatedTheme   => TranslatedThemeModel
}
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.services.tracking.TrackingLocation

import scala.scalajs.js

object ProposalTile {

  case class PostedIn(name: String, link: String)

  final case class ProposalTileProps(proposal: ProposalModel,
                                     index: Int,
                                     maybeTheme: Option[TranslatedThemeModel],
                                     maybeOperation: Option[OperationModel],
                                     maybeSequenceId: Option[SequenceId],
                                     maybeLocation: Option[LocationModel],
                                     trackingLocation: TrackingLocation,
                                     country: String,
                                     maybePostedIn: Option[PostedIn])

  final case class ProposalTileState(isProposalSharable: Boolean)

  val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ProposalTileProps, ProposalTileState](
          displayName = "ProposalTile",
          getInitialState = { self =>
            ProposalTileState(isProposalSharable = true)
          },
          render = (self) => {

            val intro: ReactElement = if (self.props.wrapped.proposal.myProposal) {
              <.div(^.className := ProposalTileStyles.shareOwnProposalWrapper)(
                <.ShareOwnProposalComponent(
                  ^.wrapped := ShareOwnProposalProps(proposal = self.props.wrapped.proposal)
                )()
              )
            } else {
              <.div(^.className := ProposalTileStyles.proposalInfosWrapper)(
                <.ProposalInfosComponent(
                  ^.wrapped := ProposalInfosProps(
                    proposal = self.props.wrapped.proposal,
                    country = Some(self.props.wrapped.country)
                  )
                )()
              )
            }

            val proposalLink: String = self.props.wrapped.maybeOperation match {
              case Some(operationExpanded) =>
                s"/${self.props.wrapped.country}/consultation/${operationExpanded.slug}/proposal/${self.props.wrapped.proposal.slug}"
              case _ =>
                self.props.wrapped.maybeTheme match {
                  case Some(theme) =>
                    s"/${self.props.wrapped.country}/theme/${theme.slug}/proposal/${self.props.wrapped.proposal.slug}"
                  case _ =>
                    s"/${self.props.wrapped.country}/proposal/${self.props.wrapped.proposal.id.value}/${self.props.wrapped.proposal.slug}"
                }

            }

            <.article(^.className := ProposalTileStyles.wrapper)(
              intro,
              <.div(^.className := ProposalTileStyles.contentWrapper)(
                <.h3(^.className := js.Array(TextStyles.mediumText, TextStyles.boldText))(
                  <.Link(^.to := proposalLink, ^.className := ProposalTileStyles.proposalLinkOnTitle)(
                    self.props.wrapped.proposal.content
                  )
                ),
                <.VoteContainerComponent(
                  ^.wrapped := VoteContainerProps(
                    proposal = self.props.wrapped.proposal,
                    index = self.props.wrapped.index,
                    maybeTheme = self.props.wrapped.maybeTheme,
                    maybeOperation = self.props.wrapped.maybeOperation,
                    maybeSequenceId = self.props.wrapped.maybeSequenceId,
                    maybeLocation = self.props.wrapped.maybeLocation,
                    trackingLocation = self.props.wrapped.trackingLocation,
                    isProposalSharable = self.state.isProposalSharable
                  )
                )()
              ),
              self.props.wrapped.maybePostedIn.map { postedIn =>
                <.div()(
                  <.div()(
                    <.footer(^.className := ProposalTileStyles.footer)(
                      <.p(^.className := js.Array(TextStyles.smallerText, ProposalTileStyles.postedInInfo))(
                        unescape(I18n.t("proposal.posted-in")),
                        <.Link(
                          ^.to := postedIn.link,
                          ^.className := js.Array(TextStyles.title, ProposalTileStyles.postedInName)
                        )(postedIn.name)
                      )
                    )
                  )
                )
              },
              <.style()(ProposalTileStyles.render[String])
            )
          }
        )
    )
}
