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

  final case class UserLikeItProposalsProps(getLikeItProposals: Future[js.Array[Proposal]])
  final case class UserLikeItProposalsState(proposals: js.Array[Proposal])

  lazy val reactClass: ReactClass = {
    React.createClass[UserLikeItProposalsProps, UserLikeItProposalsState](
      displayName = "UserLikeItProposals",
      getInitialState = _ => UserLikeItProposalsState(js.Array()),
      componentDidMount = { self =>
        self.props.wrapped.getLikeItProposals.onComplete {
          case Success(proposals) => self.setState(_.copy(proposals = proposals))
          case _                  =>
        }
      },
      render = self => {
        <("UserLikeItProposals")()(
          <.section(^.className := UserLikeItProposalsStyles.wrapper)(
            <.header(^.className := UserLikeItProposalsStyles.headerWrapper)(
              <.h2(^.className := UserLikeItProposalsStyles.title)(I18n.t("user-profile.likeItproposal.title"))
            ),
            if (self.state.proposals.isEmpty) {
              <.div(^.className := UserLikeItProposalsStyles.emptyWrapper)(
                <.i(^.className := js.Array(FontAwesomeStyles.heart, UserLikeItProposalsStyles.emptyIcon))(),
                <.p(^.className := UserLikeItProposalsStyles.emptyDesc)(I18n.t("user-profile.likeItproposal.empty")),
                <.p(^.className := UserLikeItProposalsStyles.emptyDescAlt)(
                  I18n.t("user-profile.likeItproposal.explanation-first-part"),
                  <.i(^.className := js.Array(FontAwesomeStyles.heart, UserLikeItProposalsStyles.emptyDescHeartIcon))(),
                  I18n.t("user-profile.likeItproposal.explanation-second-part"),
                ),
                <.p(^.className := UserLikeItProposalsStyles.emptyDesc)(
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
              <.ul()(self.state.proposals.map { proposal =>
                <.li(^.className := UserLikeItProposalsStyles.proposalItem)(
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
          <.style()(UserLikeItProposalsStyles.render[String])
        )
      }
    )
  }
}

object UserLikeItProposalsStyles extends StyleSheet.Inline {

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

  val emptyIcon: StyleA =
    style(
      fontSize(42.pxToEm()),
      margin(ThemeStyles.SpacingValue.small.pxToEm(42), `0`),
      color(ThemeStyles.ThemeColor.assertive),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(
          fontSize(72.pxToEm()),
          margin(ThemeStyles.SpacingValue.medium.pxToEm(72), `0`, 20.pxToEm(72))
        )
    )

  val emptyDesc: StyleA =
    style(TextStyles.mediumText, fontWeight.bold)

  val emptyDescAlt: StyleA =
    style(
      emptyDesc,
      marginTop(ThemeStyles.SpacingValue.medium.pxToEm(15)),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(marginTop(ThemeStyles.SpacingValue.medium.pxToEm(18)))
    )

  val emptyDescHeartIcon: StyleA =
    style(color(ThemeStyles.ThemeColor.assertive))

  val emptyDescThumbIcon: StyleA =
    style(color(ThemeStyles.ThemeColor.positive))

  val proposalItem: StyleA =
    style(marginTop(20.pxToEm()))

}
