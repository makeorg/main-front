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
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.models.{Proposal => ProposalModel}
import org.make.front.styles.base.TextStyles
import org.make.services.tracking.TrackingLocation

import scala.scalajs.js

object ProposalTileWithoutVote {

  final case class ProposalTileWithoutVoteProps(proposal: ProposalModel)

  val reactClass: ReactClass =
    WithRouter(
      React
        .createClass[ProposalTileWithoutVoteProps, Unit](
          displayName = "ProposalTileWithoutVote",
          render = self => {

            val proposalLink: String =
              s"/${self.props.wrapped.proposal.country}/proposal/${self.props.wrapped.proposal.slug}"

            <.article(^.className := ProposalTileStyles.wrapper)(
              <.div(^.className := ProposalTileStyles.contentWrapper)(
                <.h3(^.className := js.Array(TextStyles.mediumText, TextStyles.boldText))(
                  <.Link(^.to := proposalLink, ^.className := ProposalTileStyles.proposalLinkOnTitle)(
                    self.props.wrapped.proposal.content
                  )
                )
              ),
              <.style()(ProposalTileStyles.render[String])
            )
          }
        )
    )
}
