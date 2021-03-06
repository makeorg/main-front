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
import org.make.front.components.proposal.ProposalTileWithActorPosition.ProposalTileWithActorPositionProps
import org.make.front.components.userProfile.ProfileProposalListStyles
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Organisation => OrganisationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.proposal.ProposalResultWithUserVote

import scala.scalajs.js

object ActorProfileContributions {

  final case class ActorProfileContributionsProps(actor: OrganisationModel,
                                                  proposals: Seq[ProposalResultWithUserVote],
                                                  country: String)

  lazy val reactClass: ReactClass =
    React
      .createClass[ActorProfileContributionsProps, Unit](
        displayName = "ActorProfileContributions",
        render = self => {
          <("ActorProfileContributions")()(
            <.section(^.className := ProfileProposalListStyles.wrapper)(
              <.header(^.className := ProfileProposalListStyles.headerWrapper)(
                <.h2(^.className := ProfileProposalListStyles.title)(
                  I18n
                    .t(
                      "actor-profile.contributions.title",
                      replacements =
                        Replacements(("actor-name", self.props.wrapped.actor.organisationName.getOrElse("")))
                    )
                )
              ),
              if (self.props.wrapped.proposals.isEmpty) {
                <.div(^.className := ProfileProposalListStyles.emptyWrapper)(
                  <.i(
                    ^.className := js
                      .Array(
                        FontAwesomeStyles.thumbsUp,
                        ProfileProposalListStyles.emptyIcon,
                        ActorProfileContributionsStyles.emptyIcon
                      )
                  )(),
                  <.p(^.className := ProfileProposalListStyles.emptyDesc)(
                    self.props.wrapped.actor.organisationName,
                    unescape("&nbsp;"),
                    I18n.t("actor-profile.contributions.empty")
                  )
                )
              } else {
                val counter = new Counter()
                <.ul()(self.props.wrapped.proposals.map {
                  proposalWithVote =>
                    val proposal = proposalWithVote.proposal.copy(
                      organisations = proposalWithVote.proposal.organisations
                        .filter(_.organisationId.value != self.props.wrapped.actor.organisationId.value)
                    )
                    <.li(^.className := ProfileProposalListStyles.proposalItem)(
                      <.ProposalTileWithActorPositionComponent(
                        ^.wrapped :=
                          ProposalTileWithActorPositionProps(
                            actor = self.props.wrapped.actor,
                            proposal = proposal,
                            vote = proposalWithVote.vote,
                            country = self.props.wrapped.country,
                            index = counter.getAndIncrement(),
                          )
                      )()
                    )
                })
              }
            ),
            <.style()(ProfileProposalListStyles.render[String], ActorProfileContributionsStyles.render[String])
          )
        }
      )
}

object ActorProfileContributionsStyles extends StyleSheet.Inline {

  import dsl._

  val animThumb =
    keyframes(
      0.%%   -> keyframe(color(ThemeStyles.ThemeColor.positive), transform := s"rotate(0deg)"),
      25.%%  -> keyframe(color(ThemeStyles.ThemeColor.negative), transform := s"rotate(180deg)"),
      50.%%  -> keyframe(color(ThemeStyles.ThemeColor.negative), transform := s"rotate(180deg)"),
      75.%%  -> keyframe(color(ThemeStyles.ThemeColor.positive), transform := s"rotate(0deg)"),
      100.%% -> keyframe(color(ThemeStyles.ThemeColor.positive), transform := s"rotate(0deg)")
    )

  val emptyIcon: StyleA =
    style(
      animationName(animThumb),
      animationTimingFunction.easeIn,
      animationDuration :=! s"5s",
      animationIterationCount.infinite,
      transition := s"all 5s ease-out"
    )
}
