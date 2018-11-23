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
import io.github.shogowada.scalajs.reactjs.VirtualDOM.{<, ^, _}
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.make.front.Main.CssSettings._
import org.make.front.components.Components._
import org.make.front.components.userProfile.UserProfileFollowedContainer.UserProfileFollowedContainerProps
import org.make.front.facades.I18n
import org.make.front.models.{User, UserId}
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles

import scala.scalajs.js

object UserProfileFollowedList {

  final case class UserProfileFollowedListProps(user: Option[User])
  final case class UserProfileFollowedListState(followedUserIds: Seq[UserId])

  lazy val reactClass: ReactClass = {
    React
      .createClass[UserProfileFollowedListProps, UserProfileFollowedListState](
        displayName = "UserProfileFollowedList",
        getInitialState = { self =>
          UserProfileFollowedListState(
            followedUserIds = self.props.wrapped.user.map(_.followedUsers).getOrElse(Seq.empty)
          )
        },
        render = self => {
          <("UserProfileFollowedList")()(
            <.section(^.className := ProfileProposalListStyles.wrapper)(if (self.state.followedUserIds.isEmpty) {
              <.div(^.className := ProfileProposalListStyles.emptyWrapper)(
                <.i(
                  ^.className := js.Array(
                    FontAwesomeStyles.heart,
                    ProfileProposalListStyles.emptyIcon,
                    UserLikeItProposalsStyles.emptyIcon
                  )
                )(),
                <.p(^.className := ProfileProposalListStyles.emptyDesc)(I18n.t("user-profile.follow.title")),
                <.p(^.className := UserLikeItProposalsStyles.emptyDescAlt)(
                  I18n.t("user-profile.follow.explanation-first")
                ),
                <.p(^.className := UserLikeItProposalsStyles.emptyDescAlt)(
                  I18n.t("user-profile.follow.explanation-second")
                ),
                <.style()(ProfileProposalListStyles.render[String], UserLikeItProposalsStyles.render[String])
              )
            } else {
              <.ul(^.className := UserProfileFollowedListStyles.list)(self.state.followedUserIds.map { followedUserId =>
                <.li(^.className := js.Array(UserProfileFollowedListStyles.item))(
                  <.UserProfileFollowedContainerComponent(
                    ^.wrapped := UserProfileFollowedContainerProps(followedUserId = followedUserId)
                  )()
                )
              }, <.style()(ProfileProposalListStyles.render[String], UserProfileFollowedListStyles.render[String]))
            })
          )
        }
      )
  }
}

object UserProfileFollowedListStyles extends StyleSheet.Inline {

  import dsl._

  val list: StyleA =
    style(
      ThemeStyles.MediaQueries
        .beyondLargeMedium(display.grid, width(100.%%), gridTemplateColumns := "repeat(3, 1fr)", listStyle := "none")
    )

  val item: StyleA =
    style(
      width(100.%%),
      marginBottom(10.pxToEm()),
      ThemeStyles.MediaQueries
        .beyondLargeMedium(display.inlineBlock, padding(`0`, 5.pxToEm()), marginBottom(30.pxToEm()))
    )

}
