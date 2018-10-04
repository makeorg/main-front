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
import org.make.front.components.proposal.ProposalTileWithoutVoteAction.ProposalTileWithoutVoteActionProps
import org.make.front.facades.I18n
import org.make.front.facades.Unescape.unescape
import org.make.front.models.Proposal
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.Success

object UserLikeItProposals {

  final case class UserLikeItProposalsProps(proposals: js.Array[Proposal])

  lazy val reactClass: ReactClass = {
    React
      .createClass[UserLikeItProposalsProps, Unit](
        displayName = "UserLikeItProposals",
        render = self => {
          <("UserLikeItProposals")()(
            <.section(^.className := ProfileProposalListStyles.wrapper)(
              <.header(^.className := ProfileProposalListStyles.headerWrapper)(
                <.h2(^.className := ProfileProposalListStyles.title)(I18n.t("user-profile.likeItproposal.title"))
              ),
              if (self.props.wrapped.proposals.isEmpty) {
                <.div(^.className := ProfileProposalListStyles.emptyWrapper)(
                  <.i(
                    ^.className := js.Array(
                      FontAwesomeStyles.heart,
                      ProfileProposalListStyles.emptyIcon,
                      UserLikeItProposalsStyles.emptyIcon
                    )
                  )(),
                  <.p(^.className := ProfileProposalListStyles.emptyDesc)(I18n.t("user-profile.likeItproposal.empty")),
                  <.p(^.className := UserLikeItProposalsStyles.emptyDescAlt)(
                    I18n.t("user-profile.likeItproposal.explanation-first-part"),
                    <.i(
                      ^.className := js.Array(FontAwesomeStyles.heart, UserLikeItProposalsStyles.emptyDescHeartIcon)
                    )(),
                    I18n.t("user-profile.likeItproposal.explanation-second-part"),
                  ),
                  <.p(^.className := ProfileProposalListStyles.emptyDesc)(
                    I18n.t("user-profile.likeItproposal.explanation-third-part"),
                    unescape("&nbsp;"),
                    <.i(
                      ^.className := js.Array(FontAwesomeStyles.thumbsUp, UserLikeItProposalsStyles.emptyDescThumbIcon)
                    )(),
                    I18n.t("user-profile.likeItproposal.explanation-fourth-part")
                  ),
                  <.FakeProposalTileComponent()()
                )
              } else {
                val counter: Counter = new Counter()
                <.ul()(self.props.wrapped.proposals.map { proposal =>
                  <.li(^.className := ProfileProposalListStyles.proposalItem)(
                    <.ProposalTileWithoutVoteActionComponent(
                      ^.wrapped := ProposalTileWithoutVoteActionProps(
                        proposal = proposal,
                        index = counter.getAndIncrement(),
                        country = proposal.country
                      ),
                      ^.key := s"proposal_${proposal.id.value}"
                    )()
                  )
                }.toSeq)
              }
            ),
            <.style()(ProfileProposalListStyles.render[String], UserLikeItProposalsStyles.render[String])
          )
        }
      )
  }
}

object UserLikeItProposalsStyles extends StyleSheet.Inline {

  import dsl._

  val emptyIcon: StyleA =
    style(color(ThemeStyles.ThemeColor.assertive))

  val emptyDescAlt: StyleA =
    style(
      ProfileProposalListStyles.emptyDesc,
      marginTop(ThemeStyles.SpacingValue.medium.pxToEm(15)),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(marginTop(ThemeStyles.SpacingValue.medium.pxToEm(18)))
    )

  val emptyDescHeartIcon: StyleA =
    style(color(ThemeStyles.ThemeColor.assertive))

  val emptyDescThumbIcon: StyleA =
    style(color(ThemeStyles.ThemeColor.positive))

}
