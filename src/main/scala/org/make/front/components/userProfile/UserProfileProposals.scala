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
import org.make.front.models.{Proposal, User => UserModel}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.TextStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import scalacss.internal.ValueT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js
import scala.util.Success

object UserProfileProposals {

  final case class UserProfileProposalsProps(user: UserModel, getProposals: Future[js.Array[Proposal]])
  final case class UserProfileProposalsState(proposals: js.Array[Proposal])

  lazy val reactClass: ReactClass = React.createClass[UserProfileProposalsProps, UserProfileProposalsState](
    displayName = "UserProfileProposals",
    getInitialState = self => UserProfileProposalsState(proposals = js.Array()),
    componentDidMount = { self =>
      self.props.wrapped.getProposals.onComplete {
        case Success(proposals) => self.setState(_.copy(proposals = proposals))
        case _                  =>
      }
    },
    render = self => {
      <("UserProfileProposals")()(
        <.section(^.className := UserProfileProposalsStyles.wrapper)(
          <.header(^.className := UserProfileProposalsStyles.headerWrapper)(
            <.h2(^.className := UserProfileProposalsStyles.title)(I18n.t("user-profile.proposal.title"))
          ),
          if (self.state.proposals.isEmpty) {
            <.div(^.className := UserProfileProposalsStyles.emptyWrapper)(
              <.i(
                ^.className := js.Array(FontAwesomeStyles.lightbulbTransparent, UserProfileProposalsStyles.emptyIcon)
              )(),
              <.p(^.className := UserProfileProposalsStyles.emptyDesc)(
                self.props.wrapped.user.firstName,
                unescape("&nbsp;"),
                I18n.t("user-profile.proposal.empty")
              )
            )
          } else {
            val counter = new Counter()
            <.ul()(self.state.proposals.map { proposal =>
              <.li(^.className := UserProfileProposalsStyles.proposalItem)(
                <.ProposalTileWithoutVoteActionComponent(
                  ^.wrapped := ProposalTileWithoutVoteActionProps(
                    proposal = proposal,
                    index = counter.getAndIncrement(),
                    country = self.props.wrapped.user.country
                  )
                )()
              )
            }.toSeq)
          }
        ),
        <.style()(UserProfileProposalsStyles.render[String])
      )
      <("UserProfileProposals")()(
        <.section(^.className := UserProfileProposalsStyles.wrapper)(
          <.header(^.className := UserProfileProposalsStyles.headerWrapper)(
            <.h2(^.className := UserProfileProposalsStyles.title)(I18n.t("user-profile.proposal.title"))
          ),
          if (self.state.proposals.isEmpty) {
            <.div(^.className := UserProfileProposalsStyles.emptyWrapper)(
              <.i(
                ^.className := js.Array(FontAwesomeStyles.lightbulbTransparent, UserProfileProposalsStyles.emptyIcon)
              )(),
              <.p(^.className := UserProfileProposalsStyles.emptyDesc)(
                self.props.wrapped.user.firstName,
                unescape("&nbsp;"),
                I18n.t("user-profile.proposal.empty")
              )
            )
          } else {
            val counter: Counter = new Counter()
            <.ul()(self.state.proposals.map { proposal =>
              <.li(^.className := UserProfileProposalsStyles.proposalItem)(
                <.ProposalTileWithoutVoteActionComponent(
                  ^.wrapped := ProposalTileWithoutVoteActionProps(
                    proposal = proposal,
                    index = counter.getAndIncrement(),
                    country = self.props.wrapped.user.country
                  ),
                  ^.key := s"proposal_${proposal.id.value}"
                )()
              )
            }.toSeq)
          }
        ),
        <.style()(UserProfileProposalsStyles.render[String])
      )
    }
  )
}

object UserProfileProposalsStyles extends StyleSheet.Inline {

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
