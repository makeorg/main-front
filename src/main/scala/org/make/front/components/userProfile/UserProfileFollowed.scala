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
import org.make.front.models.User
import org.make.front.styles.ThemeStyles
import org.make.front.styles.base.{RWDRulesLargeMediumStyles, TextStyles}
import org.make.front.styles.utils._
import org.make.front.styles.vendors.FontAwesomeStyles
import scalacss.internal.ValueT

import scala.scalajs.js

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
              <.div(^.className := UserProfileFollowedStyles.innerWrapper)(
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
                  <.div()(
                    followedUser.organisationName
                      .orElse(followedUser.firstName)
                      .map(
                        name =>
                          <.h3(^.className := js.Array(TextStyles.smallerTitleAlt, UserProfileFollowedStyles.name))(
                            name,
                            <.i(
                              ^.className := js
                                .Array(FontAwesomeStyles.checkCircle, UserProfileFollowedStyles.checkCircle)
                            )()
                        )
                      ),
                    followedUser.profile
                      .flatMap(_.description)
                      .map(
                        description =>
                          <.p()(
                            <.p(^.className := js.Array(TextStyles.smallerText, UserProfileFollowedStyles.desc))(
                              description
                            ),
                            if (description.length >= 250) {
                              <.span(
                                ^.className := js.Array(
                                  RWDRulesLargeMediumStyles.showBlockBeyondLargeMedium,
                                  TextStyles.smallerText,
                                  UserProfileFollowedStyles.desc
                                )
                              )("...")
                            }
                        )
                      )
                  )
                )
              ),
              <.FollowButtonContainerComponent(
                ^.wrapped := FollowButtonContainerProps(userId = self.props.wrapped.user.userId, actorName = None)
              )(),
              <.style()(UserProfileFollowedStyles.render[String])
            )
          )
        }
      )
  }
}

object UserProfileFollowedStyles extends StyleSheet.Inline {
  import dsl._

  val wrapper: StyleA =
    style(
      display.flex,
      position.relative,
      height(100.%%),
      justifyContent.spaceBetween,
      flexFlow := "column",
      backgroundColor(ThemeStyles.BackgroundColor.white),
      padding(15.pxToEm()),
      boxShadow := "0 1px 1px 0 rgba(0, 0, 0, 0.5)"
    )

  val innerWrapper: StyleA =
    style(display.flex, ThemeStyles.MediaQueries.beyondLargeMedium(flexFlow := "column"))

  val avatar: StyleA =
    style(
      position.absolute,
      top(50.%%),
      left(50.%%),
      transform := s"translate(-50%, -50%)",
      width(100.%%),
      height(100.%%),
      ThemeStyles.MediaQueries.beyondLargeMedium(width(160.pxToEm()))
    )
  val avatarWrapper: StyleA =
    style(
      position.relative,
      width(ThemeStyles.SpacingValue.larger.pxToEm()),
      minWidth(ThemeStyles.SpacingValue.larger.pxToEm()),
      height(ThemeStyles.SpacingValue.larger.pxToEm()),
      marginRight(ThemeStyles.SpacingValue.small.pxToEm()),
      overflow.hidden,
      backgroundColor(ThemeStyles.BackgroundColor.white),
      borderRadius(50.%%),
      border(1.px, solid, ThemeStyles.BorderColor.lighter),
      textAlign.center,
      ThemeStyles.MediaQueries
        .beyondLargeMedium(
          position.absolute,
          left(50.%%),
          top(-10.pxToEm()),
          transform := s"translateX(-50%)",
          width(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
          minWidth(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
          height(ThemeStyles.SpacingValue.evenLarger.pxToEm()),
          marginRight(`0`)
        )
    )

  val name: StyleA =
    style(
      marginBottom(5.pxToEm(13)),
      ThemeStyles.MediaQueries.beyondLargeMedium(textAlign.center, margin(75.pxToEm(16), `0`, 10.pxToEm(16)))
    )

  val blue: ValueT[ValueT.Color] = rgb(74, 144, 226)

  val checkCircle: StyleA =
    style(color(blue), marginLeft(5.pxToEm()))

  val desc: StyleA =
    style(
      color(ThemeStyles.TextColor.lighter),
      ThemeStyles.MediaQueries.beyondLargeMedium(maxHeight(80.pxToEm(14)), overflow.hidden)
    )

}
