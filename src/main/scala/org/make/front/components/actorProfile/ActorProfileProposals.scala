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
import org.make.front.components.proposal.ProposalTile.ProposalTileProps
import org.make.front.facades.Unescape.unescape
import org.make.front.facades.{I18n, Replacements}
import org.make.front.models.{Proposal, Organisation => OrganisationModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import org.make.services.tracking.TrackingLocation
import scalacss.internal.ValueT

import scala.scalajs.js

object ActorProfileProposals {

  final case class ActorProfileProposalsProps(actor: OrganisationModel, actorProposals: js.Array[Proposal])

  lazy val reactClass: ReactClass =
    React
      .createClass[ActorProfileProposalsProps, Unit](
        displayName = "ActorProfileProposals",
        render = self => {
          <("ActorProfileProposals")()(
            <.section(^.className := ActorProfileProposalsStyles.wrapper)(
              <.header(^.className := ActorProfileProposalsStyles.headerWrapper)(
                <.h2(^.className := ActorProfileProposalsStyles.title)(
                  I18n
                    .t(
                      "actor-profile.proposal.title",
                      replacements =
                        Replacements(("actor-name", self.props.wrapped.actor.organisationName.getOrElse("")))
                    )
                )
              ),
              if (self.props.wrapped.actorProposals.isEmpty) {
                <.div(^.className := ActorProfileProposalsStyles.emptyWrapper)(
                  <.i(
                    ^.className := js
                      .Array(FontAwesomeStyles.lightbulbTransparent, ActorProfileProposalsStyles.emptyIcon)
                  )(),
                  <.p(^.className := ActorProfileProposalsStyles.emptyDesc)(
                    self.props.wrapped.actor.organisationName,
                    unescape("&nbsp;"),
                    I18n.t("actor-profile.proposal.empty")
                  )
                )
              } else {
                val counter = new Counter()
                <.ul()(self.props.wrapped.actorProposals.map { proposal =>
                  <.li(^.className := ActorProfileProposalsStyles.proposalItem)(
                    <.ProposalTileComponent(
                      ^.wrapped :=
                        ProposalTileProps(
                          proposal = proposal,
                          index = counter.getAndIncrement(),
                          maybeTheme = None,
                          maybeOperation = None,
                          maybeSequenceId = None,
                          maybeLocation = None,
                          trackingLocation = TrackingLocation.actorProfile,
                          country = proposal.country
                        )
                    )()
                  )
                }.toSeq)
              }
            ),
            <.style()(ActorProfileProposalsStyles.render[String])
          )
        }
      )
}

object ActorProfileProposalsStyles extends StyleSheet.Inline {

  import dsl._

  val wrapper: StyleA =
    style(padding(20.pxToEm()), ThemeStyles.MediaQueries.beyondLargeMedium(padding(40.pxToEm(), `0`)))

  val headerWrapper: StyleA =
    style(borderBottom(1.pxToEm(), solid, ThemeStyles.BorderColor.lighter))

  val title: StyleA =
    style(
      TextStyles.title,
      fontSize(15.pxToEm()),
      lineHeight(1),
      marginBottom(ThemeStyles.SpacingValue.small.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondSmall(fontSize(18.pxToEm()), marginBottom(ThemeStyles.SpacingValue.small.pxToEm(18)))
    )

  val emptyWrapper: StyleA =
    style(display.flex, flexFlow := s"column", alignItems.center)

  val specialYellow: ValueT[ValueT.Color] = rgb(255, 212, 0)

  val emptyIcon: StyleA =
    style(
      color(specialYellow),
      fontSize(42.pxToEm()),
      margin(ThemeStyles.SpacingValue.small.pxToEm(42), `0`),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(
          fontSize(72.pxToEm()),
          margin(ThemeStyles.SpacingValue.medium.pxToEm(72), `0`, 20.pxToEm(72))
        )
    )

  val emptyDesc: StyleA =
    style(TextStyles.mediumText, fontWeight.bold)

  val proposalItem: StyleA =
    style(marginTop(20.pxToEm()))

}
