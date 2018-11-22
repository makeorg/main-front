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
import org.make.front.components.actorProfile.FollowButtonContainer.FollowButtonContainerProps
import org.make.front.components.userProfile.UserProfileInformationsStyles.style
import org.make.front.models.User
import org.make.front.styles.ThemeStyles
import org.make.front.styles.utils._

object UserProfileFollowed {

  final case class UserProfileFollowedProps(user: User)
  final case class UserProfileFollowedState(isFollowed: Boolean)

  lazy val reactClass: ReactClass = {
    React
      .createClass[UserProfileFollowedProps, UserProfileFollowedState](
        displayName = "UserProfileFollowed",
        getInitialState = { _ =>
          UserProfileFollowedState(isFollowed = true)
        },
        render = self => {
          val followedUser: User = self.props.wrapped.user
          val avatarUrl: Option[String] = followedUser.profile.flatMap(_.avatarUrl)
          <("UserProfileFollowed")()(
            <.div(^.className := UserProfileFollowedStyles.wrapper)(
              Seq(
                avatarUrl.map(
                  url =>
                    <.div(^.className := UserProfileFollowedStyles.avatarWrapper)(
                      <.img(
                        ^.src := url,
                        ^.alt := followedUser.organisationName.getOrElse("-"),
                        ^.className := UserProfileFollowedStyles.avatar
                      )()
                  )
                ),
                followedUser.organisationName.orElse(followedUser.firstName).map(name => <.p()(name)),
                followedUser.profile.flatMap(_.description).map(description           => <.p()(description)),
                <.FollowButtonContainerComponent(
                  ^.wrapped := FollowButtonContainerProps(userId = self.props.wrapped.user.userId)
                )(),
                <.style()(UserProfileFollowedStyles.render[String])
              )
            )
          )
        }
      )
  }
}

object UserProfileFollowedStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA = style(backgroundColor(ThemeStyles.BackgroundColor.white), marginTop(30.pxToEm()), display.block)

  val avatar: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      transform := s"translate(-50%, -50%)",
      width(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      minWidth(100.%%),
      minHeight(100.%%),
      maxWidth.none,
      maxHeight.none,
      ThemeStyles.MediaQueries.beyondLargeMedium(width(160.pxToEm()))
    )
  val avatarWrapper: StyleA =
    style(
      position.relative,
      width(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      height(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
      marginTop(-40.pxToEm()),
      marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
      overflow.hidden,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      borderRadius(50.%%),
      border(1.5.px, solid, ThemeStyles.BorderColor.lighter),
      textAlign.center,
      ThemeStyles.MediaQueries.beyondLargeMedium(
        float.none,
        display.block,
        verticalAlign.middle,
        width(80.pxToEm()),
        height(80.pxToEm()),
        marginTop(-20.pxToEm()),
        borderWidth(5.px),
        marginLeft.auto,
        marginRight.auto
      )
    )
  // @toDo
}
