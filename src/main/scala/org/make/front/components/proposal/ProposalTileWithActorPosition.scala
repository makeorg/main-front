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
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.proposal.ProposalTileWithOrganisationsVotes.ProposalTileWithOrganisationsVotesProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.{Organisation => OrganisationModel, Proposal => ProposalModel}
import org.make.front.styles.ThemeStyles.BorderColor
import org.make.front.styles._
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingLocation

import scala.scalajs.js

object ProposalTileWithActorPosition {

  final case class ProposalTileWithActorPositionProps(actor: OrganisationModel,
                                                      proposal: ProposalModel,
                                                      vote: String,
                                                      country: String,
                                                      index: Int)
  final case class ProposalTileWithActorPositionState(listProposals: js.Array[ProposalModel])

  val reactClass: ReactClass = {
    React
      .createClass[ProposalTileWithActorPositionProps, ProposalTileWithActorPositionState](
        displayName = "ProposalTileWithActorPosition",
        render = { self =>
          val buttonClasses =
            js.Array(ProposalTileWithActorPositionStyles.voteButton.htmlClass, self.props.wrapped.vote match {
                case "agree" =>
                  ProposalTileWithActorPositionStyles.agree.htmlClass
                case "disagree" =>
                  ProposalTileWithActorPositionStyles.disagree.htmlClass
                case "neutral" =>
                  ProposalTileWithActorPositionStyles.neutral.htmlClass
                case _ =>
                  ProposalTileWithActorPositionStyles.neutral.htmlClass
              })
              .mkString(" ")

          val thumbIconClasses =
            js.Array(ProposalTileWithActorPositionStyles.voteIcon.htmlClass, self.props.wrapped.vote match {
                case "disagree" =>
                  FontAwesomeStyles.thumbsDown.htmlClass
                case _ =>
                  FontAwesomeStyles.thumbsUp.htmlClass
              })
              .mkString(" ")

          <("ProposalTileWithActorPosition")()(
            <.section(^.className := ProposalTileWithActorPositionStyles.wrapper)(
              <.header(^.className := ProposalTileWithActorPositionStyles.headerWrapper)(
                <.div(^.className := buttonClasses)(<.i(^.className := thumbIconClasses)()),
                <.span(^.className := ProposalInfosStyles.infos)(
                  (self.props.wrapped.actor.slug, self.props.wrapped.country) match {
                    case (Some(slug), country) =>
                      <.Link(^.className := ProposalInfosStyles.actorLink, ^.to := s"/$country/profile/$slug")(
                        self.props.wrapped.actor.organisationName
                      )
                    case _ => self.props.wrapped.actor.organisationName
                  },
                ),
                <.i(^.className := js.Array(FontAwesomeStyles.checkCircle, ProposalInfosStyles.checkCircle))(),
                unescape("&nbsp;"),
                <.span(^.className := ProposalTileWithActorPositionStyles.commitment)(
                  I18n.t(s"actor-profile.contributions.${self.props.wrapped.vote}")
                )
              ),
              <.ProposalTileWithOrganisationsVotesComponent(
                ^.wrapped :=
                  ProposalTileWithOrganisationsVotesProps(
                    proposal = self.props.wrapped.proposal,
                    index = self.props.wrapped.index,
                    maybeTheme = None,
                    maybeOperation = None,
                    maybeSequenceId = None,
                    maybeLocation = None,
                    trackingLocation = TrackingLocation.actorProfile,
                    country = self.props.wrapped.country
                  )
              )()
            ),
            <.style()(ProposalTileWithActorPositionStyles.render[String])
          )
        }
      )
  }
}

object ProposalTileWithActorPositionStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(
      backgroundColor(ThemeStyles.BackgroundColor.blackVeryTransparent),
      padding(
        ThemeStyles.SpacingValue.small.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm(),
        5.pxToEm(),
        ThemeStyles.SpacingValue.small.pxToEm()
      ),
      boxShadow := s"0 1px 1px 0 rgba(0, 0, 0, 0.5)"
    )

  val headerWrapper: StyleA =
    style(marginBottom(ThemeStyles.SpacingValue.smaller.pxToEm()))

  val commitment: StyleA =
    style(ThemeStyles.Font.circularStdBook, fontSize(14.pxToEm()), lineHeight(30.pxToEm()))

  val voteButton: StyleA =
    style(
      width(30.pxToEm()),
      height(30.pxToEm()),
      display.inlineBlock,
      textAlign.center,
      borderStyle :=! s"solid",
      borderWidth(1.pxToEm()),
      boxShadow := "0 0 0 0 rgba(0, 0, 0, 0)",
      borderRadius(50.%%),
      marginRight(5.pxToEm())
    )

  val voteIcon: StyleA =
    style(lineHeight(28.pxToEm()), color(ThemeStyles.TextColor.white))

  val agree: StyleA =
    style(backgroundColor(ThemeStyles.ThemeColor.positive), borderColor(ThemeStyles.ThemeColor.positive))

  val disagree: StyleA =
    style(backgroundColor(ThemeStyles.ThemeColor.negative), borderColor(ThemeStyles.ThemeColor.negative))

  val neutral: StyleA =
    style(
      backgroundColor(ThemeStyles.TextColor.grey),
      borderColor(ThemeStyles.TextColor.grey),
      transform := s"rotate(-90deg)"
    )
}
