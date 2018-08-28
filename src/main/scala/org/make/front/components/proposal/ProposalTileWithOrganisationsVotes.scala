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
import org.make.front.components.proposal.ShareOwnProposal.ShareOwnProposalProps
import org.make.front.components.proposal.vote.VoteContainer.VoteContainerProps
import org.make.front.models.{
  SequenceId,
  Location          => LocationModel,
  OperationExpanded => OperationModel,
  Proposal          => ProposalModel,
  Qualification     => QualificationModel,
  TranslatedTheme   => TranslatedThemeModel,
  Vote              => VoteModel
}
import org.make.front.styles._
import org.make.front.styles.base.{TableLayoutStyles, TextStyles}
import org.make.front.styles.ui.TagStyles
import org.make.front.styles.utils._
import org.make.services.tracking.TrackingLocation

import scala.scalajs.js

object ProposalTileWithOrganisationsVotes {

  final case class ProposalTileWithOrganisationsVotesProps(
    proposal: ProposalModel,
    index: Int,
    handleSuccessfulVote: (VoteModel)                           => Unit = (_)    => {},
    handleSuccessfulQualification: (String, QualificationModel) => Unit = (_, _) => {},
    trackingLocation: TrackingLocation,
    maybeTheme: Option[TranslatedThemeModel],
    maybeOperation: Option[OperationModel],
    maybeSequenceId: Option[SequenceId],
    maybeLocation: Option[LocationModel],
    country: String
  )

  final case class ProposalTileWithOrganisationsVotesState(isProposalSharable: Boolean)

  val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ProposalTileWithOrganisationsVotesProps, ProposalTileWithOrganisationsVotesState](
          displayName = "ProposalTileWithTags",
          getInitialState = { self =>
            ProposalTileWithOrganisationsVotesState(isProposalSharable = true)
          },
          render = (self) => {

            val intro: ReactElement =
              <.div(^.className := ProposalTileStyles.proposalInfosWrapper)(
                <.ProposalInfosComponent(
                  ^.wrapped := ProposalInfosProps(
                    proposal = self.props.wrapped.proposal,
                    country = Some(self.props.wrapped.country)
                  )
                )()
              )

            val proposalLink: String = self.props.wrapped.maybeOperation match {
              case Some(operationExpanded) =>
                s"/${self.props.wrapped.country}/consultation/${operationExpanded.slug}/proposal/${self.props.wrapped.proposal.slug}"
              case _ =>
                self.props.wrapped.maybeTheme match {
                  case Some(theme) =>
                    s"/${self.props.wrapped.country}/theme/${theme.slug}/proposal/${self.props.wrapped.proposal.slug}"
                  case _ => s"/${self.props.wrapped.country}/proposal/${self.props.wrapped.proposal.slug}"
                }

            }

            <.article(^.className := ProposalTileStyles.wrapper)(
              <.div(^.className := js.Array(TableLayoutStyles.fullHeightWrapper, ProposalTileStyles.innerWrapper))(
                <.div(^.className := TableLayoutStyles.row)(
                  <.div(^.className := TableLayoutStyles.cell)(
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
                          onSuccessfulVote = self.props.wrapped.handleSuccessfulVote,
                          onSuccessfulQualification = self.props.wrapped.handleSuccessfulQualification,
                          index = self.props.wrapped.index,
                          trackingLocation = self.props.wrapped.trackingLocation,
                          maybeTheme = self.props.wrapped.maybeTheme,
                          maybeOperation = self.props.wrapped.maybeOperation,
                          maybeSequenceId = self.props.wrapped.maybeSequenceId,
                          maybeLocation = self.props.wrapped.maybeLocation,
                          isProposalSharable = self.state.isProposalSharable
                        )
                      )(),
                      <.ProposalActorVotedComponent(
                        ^.wrapped := ProposalActorVotedProps(
                          organisations = self.props.wrapped.proposal.organisations,
                          country = self.props.wrapped.country
                        )
                      )()
                    )
                  )
                ),
                if (self.props.wrapped.proposal.tags.nonEmpty) {
                  <.div(^.className := TableLayoutStyles.row)(
                    <.div(^.className := TableLayoutStyles.cellVerticalAlignBottom)(
                      <.footer(^.className := ProposalTileStyles.footer)(
                        <.ul(^.className := ProposalTileWithOragnisationsVotesStyles.tagList)(
                          self.props.wrapped.proposal.tags
                            .map(
                              tag =>
                                <.li(^.className := ProposalTileWithOragnisationsVotesStyles.tagListItem)(
                                  <.span(^.className := TagStyles.basic)(tag.label)
                              )
                            )
                            .toSeq
                        )
                      )
                    )
                  )
                }
              ),
              <.style()(ProposalTileStyles.render[String], ProposalTileWithOragnisationsVotesStyles.render[String])
            )
          }
        )
    )
}

object ProposalTileWithOragnisationsVotesStyles extends StyleSheet.Inline {

  import dsl._

  val tagList: StyleA =
    style(lineHeight(0), margin(((ThemeStyles.SpacingValue.smaller / 2) * -1).pxToEm()))

  val tagListItem: StyleA =
    style(display.inlineBlock, verticalAlign.middle, margin((ThemeStyles.SpacingValue.smaller / 2).pxToEm()))

}
